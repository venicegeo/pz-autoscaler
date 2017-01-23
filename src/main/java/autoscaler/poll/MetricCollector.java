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
import org.springframework.stereotype.Component;

/**
 * Collects metrics and stores in-memory based on communications with the polled Metadata. Based on this recorded metric
 * data, this will make recommendations if the Application should be scaled up or down.
 * 
 * @author Patrick.Doody
 *
 */
@Component
public class MetricCollector {
	/**
	 * Records the data from a poll of a Piazza Service Job queue.
	 * 
	 * @param dateTime
	 *            The time of the poll.
	 * @param jobCount
	 *            The number of Jobs in the queue at that time.
	 */
	public void record(DateTime dateTime, int jobCount) {

	}

	/**
	 * Based on the metrics, determines if the App requires the instance count to be incremented.
	 * 
	 * @return True if increment should occur, false if not
	 */
	public boolean doesRequireIncrement() {
		return false;
	}

	/**
	 * Based on the metrics, determines if the App requires the instance count to be decremented.
	 * 
	 * @return True if decrement should occur, false if not
	 */
	public boolean doesRequireDecrement() {
		return false;
	}
}
