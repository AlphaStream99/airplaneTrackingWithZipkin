<!-- DESCRIPTION -->
## Description and scope of our project:

For the course "Special Topics - Cloud Computing from a Software Engineering Perspective", we would like to realize a project which is very similar to the following idea: "Microservice communication observed with OpenTelemetry & Jaeger". However, instead of using Jaeger, we would like to rely on Zipkin, instead. Zipkin is quite similar to Jaeger and is also very capable. In order to communicate with Zipkin, however, we would have to use a collection of tracing libraries. This would mean, we would probably not rely on OpenTelemetry. The project we would use consists of 3 Microservices and is provided by one of my team members, who would also redesign it in such a way that we would have 4 Microservices instead of three. The reason for that is that each of us should instrument the communication channel between two services.

The project and Zipkin will be deployed on the cloud. We thought that we would have one container per two services + one container for the Zipkin server + one for Kafka, which makes four containers in total. 

<!-- MILESTONES -->
## Milestone 1 - Set up the actual microservices locally 
* Prepare the base project (3 working microservices that we have from a previous project)
* Configure Kafka through which they communicate
* Add an additional, 4th microservice so the workload can be split into equal pieces
* Set up the working system locally

## Milestone 2 - Set it up in the cloud
* Take the newly implemented and finished project and deploy it in the cloud
* The idea, for now, is to have 1 container per 2 services, 1 container for Zipkin and 1 for Kafka

## Milestone 3 - Implement the tracing
* We would have four microservices by then, and each of us would instrument one of the three interfaces. (interface = where communication to another microservice is needed)
* Each “coherent” operation (meaning a collection of actions which belong together to fulfil a specific task) should get one trace, and for each action (or where it makes sense), * a new Span should be created for the respective trace. Then those should be sent to Zipkin

## Milestone 4 - Prepare a demo project for presentation
* It should include load generation without human intervention.
* Everything should happen on the cloud, of course.
* The traces/spans generated during load generation should be visible in Zipkin. 

## Milestone 5 - Do the presentation
* Set up the demo
* Make the diagrams
* Hopefully, pass and have a fun time while doing so!

<!-- RESPONSIBILITIES -->
## Responsibilities - Who will do what?
* Prankur - Will be our IT expert -> will take over the setup of the environment in general (without Zipkin, but including docker configuration, etc.)
* Benno - Will be our Zipkin expert -> prepare the Zipkin server + the libraries for communicating with Zipkin (+ integration of those libraries to our project)
* Dunja - Will be our Software Architect -> setting up the initial project (the 3+1 microservice and their communication through Kafka) + diagrams
* All of us: instrument one interface each

And all of us together will share the load of using more advanced Zipkin features, depending on what we understand better/like more. The presentation and demo will be presented, of course, by all of us.

We are also open to any suggestions and improvements you might have.

