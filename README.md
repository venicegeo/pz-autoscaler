#Piazza Autoscaler

This application will autoscale an individual Cloud Foundry App based on the number of Jobs in a Piazza Service Jobs Queue.

##Environment

This application depends on the following variables:
- _cf.apiHost_ - The API host for Cloud Foundry
- _cf.organization_ - The organization the App resides in
- _cf.space_ - The space the App resides in
- _cf.appName_ - The name of the application to scale
- _cf.username_ - The username of the CF account with permissions to adjust instance counts
- _cf.password_ - The password of the CF account
- _pz.hostUrl_ - The Host URL for Piazza; should include protocol, hostname, and port (if necessary)
- _pz.serviceId_ - The ID of the Service
- _pz.apiKey_ - The API Key of the Piazza account to check Service Metadata
- _scale.minInstances_ - The minimum number of instances for the App
- _scale.maxInstances_ - The maximum number of instances for the App
- _scale.jobHighThreshold_ - The upper-limit for number of Jobs in queue before scaling up
- _scale.jobLowThreshold_ - The lower-limit for number of Jobs in the queue before scaling down
- _scale.intervalSeconds_ - The number of seconds in between each potential scaling interval check

##Running

`mvn clean install` to build this project.

`mvn spring-boot:run` to run this project. 

##Usage

Once running, this application will begin polling the Piazza Task-Managed Service queue for the specified Service ID. If the number of Jobs in that Jobs queue is less than the lower threshold, then this will initiate an instance decrement for the specified App Name. Likewise, if the number of the Jobs in that Jobs queue is greater than the upper upper threshold, then this will initiate an instance increment for the specified App Name.

##Rationale

While the Pivotal App Autoscaler is sufficient for cases of autoscaling based off of CPU or HTTP metrics, it does not provide any facility for autoscaling based on custom metrics. This repository aims to provide a simple, customizable app that demonstrates how one can autoscale a Cloud Foundry application based on some set of custom metrics - in this particular case - the number of Jobs in a Piazza Service Jobs Queue.