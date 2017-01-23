/**
 * Copyright 2016, RadiantBlue Technologies, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package autoscaler.cf;

import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationDetail;
import org.cloudfoundry.operations.applications.GetApplicationRequest;
import org.cloudfoundry.operations.applications.ScaleApplicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Broker to communicate with Cloud Foundry via the Cloud Foundry Operations API to scale instances of the application
 * up or down.
 * 
 * @author Patrick.Doody
 *
 */
@Component
public class InstanceBroker {
	@Value("${cf.appName}")
	private String appName;
	@Value("${scale.minInstances}")
	private Integer minInstances;
	@Value("${scale.maxInstances}")
	private Integer maxInstances;

	@Autowired
	private DefaultCloudFoundryOperations cfOps;

	private final static Logger LOGGER = LoggerFactory.getLogger(InstanceBroker.class);

	/**
	 * Increments the number of instances of the App by 1.
	 */
	public void increment() {
		Integer currentInstances = getInstanceCount();
		if (currentInstances < maxInstances) {
			cfOps.applications().scale(ScaleApplicationRequest.builder().name(appName).instances(getInstanceCount() + 1).build()).block();
			LOGGER.info(String.format("Incremented App Count to %s", currentInstances + 1));
		} else {
			LOGGER.info(String.format("Upper limit of %s Instances Reached. Could not scale up.", maxInstances));
		}
	}

	/**
	 * Decrements the number of instances of the App by 1.
	 */
	public void decrement() {
		Integer currentInstances = getInstanceCount();
		if (currentInstances > minInstances) {
			cfOps.applications().scale(ScaleApplicationRequest.builder().name(appName).instances(getInstanceCount() - 1).build()).block();
			LOGGER.info(String.format("Decremented App Count to %s", currentInstances - 1));
		} else {
			LOGGER.info(String.format("Lower limit of %s Instances Reached. Could not scale down.", maxInstances));
		}
	}

	/**
	 * Gets the current number of instances for the app.
	 * 
	 * @return App instances
	 */
	private Integer getInstanceCount() {
		ApplicationDetail appDetails = cfOps.applications().get(GetApplicationRequest.builder().name(appName).build()).block();
		LOGGER.info(String.format("Checked current Instance Count, was %s", appDetails.getInstances()));
		return appDetails.getInstances();
	}
}
