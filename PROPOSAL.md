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

## For the resubmission:
### Why do we want to use Zipkin
Zipkin came before Jaeger, and Jaeger was inspired by Zipkin and Dapper (tracing from google)
#### Disadvantages compared to Jaeger: 
-> Does not officially support OpenTelemetry but we would have to implement a mapping between Zipkin calls and OpenTelemetry (can be difficult)
-> Because of the first point, it does not support as many languages as Jaeger
#### Advantages compared to Jaeger: 
-> Jaeger is a system which consists of many different units, while Zipkin ships its entire architecture including Collectors, Storage and UI and thus, works out of the box.
-> Also, if a company decides to switch from Zipkin to Jaeger for whatever reason in the future, no problem at all - code must be only minimally adapted since Zipkin Traces are compatible with Jaeger (and its libraries).
### Why put multiple services into the same container
We were thinking of reducing the workload and grouping similar microservices together. However, since it’s a small project, we have decided to take your advice and put every microservice independently.
        
### Event-based?
Yes, the architecture is event-based and we “make” the events by sending data from Postman and there is no real user interaction (because also at airports, we cannot really influence how the planes go, it’s mostly just seeing where they go from, how much the ticket costs, etc).

### The architecture diagram
![Architecture diagram](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/proposed-arch-diagram.jpg)

The idea is to send requests from Postman to the Airplane service which initially receives the data and then sends it to a corresponding topic in Kafka in the format of an Avro scheme we will also specify. FlightApproval service takes the messages in the previously mentioned topic and then does some calculations based on the distance of the airports, weather, etc. and feeds a new topic with its results. The CancelledFlight service will provide information about flights that have been cancelled or rescheduled. “To be seen” service we still have to think about a bit more but an idea we had was to do something similar to what RyanAir does - gives out very cheap flights for less popular destinations, especially off-season. Or, it could be a ControlTower service, aggregating the flights and saying when each of them can take off and land.


### Provide a deployment architecture
We would like to use Kubernetes, sorry for not mentioning it in the previous e-mail.
The boxes represent nodes and most of the application will be stored in a 1 node 1 pod way, except Kafka and friend because we feel like they make a logical whole and should be kept in the same node.

![Deployment diagram here](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/proposed-deployment-diagram.jpg)


