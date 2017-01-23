Piazza Autoscaler. Autoscales a Cloud Foundry app based on the number of Jobs in a Piazza Service Jobs Queue.

This application depends on the following variables:
- cf.apiHost - The API host for Cloud Foundry
- cf.organization - The organization the App resides in
- cf.space - The space the App resides in
- cf.appName - The name of the application to scale
- cf.username - The username of the CF account with permissions to adjust instance counts
- cf.password - The password of the CF account
- pz.hostUrl - The Host URL for Piazza; should include protocol, hostname, and port (if necessary)
- pz.serviceId - The ID of the Service
- pz.apiKey - The API Key of the Piazza account to check Service Metadata
- scale.minInstances - The minimum number of instances for the App
- scale.maxInstances - The maximum number of instances for the App
- scale.jobHighThreshold - The upper-limit for number of Jobs in queue before scaling up
- scale.jobLowThreshold - The lower-limit for number of Jobs in the queue before scaling down
- scale.intervalSeconds - The number of seconds in between each potential scaling interval check