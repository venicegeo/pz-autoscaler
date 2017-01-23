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

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Handles HTTP communication with Piazza in order to collect metadata relevant to scaling. In this case, it queries for
 * the number of Jobs in a Piazza Service Queue.
 * 
 * @author Patrick.Doody
 *
 */
@Component
public class Pollster {
	@Value("${scale.intervalSeconds}")
	private Integer intervalSeconds;

	private PollServiceTask pollTask = new PollServiceTask();
	private Timer pollTimer = new Timer();
	private final static Logger LOGGER = LoggerFactory.getLogger(Pollster.class);

	/**
	 * Begins scheduled polling of Service Metadata
	 */
	@PostConstruct
	public void startPolling() {
		// Begin polling at the determined frequency after a short delay
		pollTimer.schedule(pollTask, 5000, intervalSeconds * 1000);
		LOGGER.info("Begin polling for Service Jobs.");
	}

	/**
	 * Halts scheduled polling.
	 */
	public void stopPolling() {
		pollTimer.cancel();
		LOGGER.info("Polling Halted.");
	}

	/**
	 * Repeated Task that will continuously query for the number of Jobs in the Service Queue, and feed those metrics
	 * into the Collector.
	 * 
	 * @author Patrick.Doody
	 */
	@Component
	public class PollServiceTask extends TimerTask {
		@Value("${pz.serviceId}")
		private String serviceId;
		@Value("${pz.apiKey}")
		private String apiKey;
		@Value("${pz.hostUrl}")
		private String pzHost;
		@Value("${cf.space}")
		private String space;

		@Autowired
		private RestTemplate restTemplate;
		@Autowired
		private MetricCollector metricCollector;

		/**
		 * Gets the Service metadata response from Piazza for the bound service. This metadata will contain, at least,
		 * the number of Jobs in the queue.
		 */
		@Override
		public void run() {
			// Query Piazza for the Service Metadata
			String url = String.format("%s/service/%s/task/metadata", pzHost, serviceId);
			ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {
			};
			try {
				LOGGER.info(String.format("Querying Service Metadata at %s", url));
				ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
				// Send data to the Metrics Collector
				metricCollector.onMetricGathered(new DateTime(), Integer.parseInt(response.getBody().get("totalJobCount").toString()));
			} catch (HttpClientErrorException | HttpServerErrorException exception) {
				// Handle any HTTP Errors
				LOGGER.error(String.format("HTTP Error status encountered : %s. Response : was %s", exception.getStatusCode().toString(),
						exception.getResponseBodyAsString()), exception);
			} catch (NumberFormatException exception) {
				// Handle Formatting or Parse errors
				LOGGER.error(String.format("Error parsing Server response : %s", exception.getMessage()), exception);
			}
		}
	}
}
