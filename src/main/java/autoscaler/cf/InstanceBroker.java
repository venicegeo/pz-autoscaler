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
	/**
	 * Increments the number of instances of the App by 1.
	 */
	public void increment() {

	}

	/**
	 * Decrements the number of instances of the App by 1.
	 */
	public void decrement() {

	}
}
