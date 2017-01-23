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
package autoscaler.poll;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import autoscaler.cf.InstanceBroker;

/**
 * Collects metrics and stores in-memory based on communications with the polled Metadata. Based on this recorded metric
 * data, this will make recommendations if the Application should be scaled up or down.
 * 
 * @author Patrick.Doody
 *
 */
@Component
public class MetricCollector {
	@Value("${scale.jobHighThreshold}")
	private Integer jobHighThreshold;
	@Value("${scale.jobLowThreshold}")
	private Integer jobLowThreshold;

	@Autowired
	private InstanceBroker instanceBroker;

	/**
	 * Records the data from a poll of a Piazza Service Job queue, and determines if scaling is appropriate.
	 * 
	 * @param dateTime
	 *            The time of the poll.
	 * @param jobCount
	 *            The number of Jobs in the queue at that time.
	 */
	public void onMetricGathered(DateTime dateTime, Integer jobCount) {
		if (jobCount > jobHighThreshold) {
			// Do we need to scale up?
			instanceBroker.increment();
		} else if (jobCount < jobLowThreshold) {
			// Do we need to scale down?
			instanceBroker.decrement();
		}
	}
}
