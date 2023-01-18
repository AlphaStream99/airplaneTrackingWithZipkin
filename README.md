# Clound Computing from a Software Engineering Perspective
## Airplane Tracking with Zipkins

### Contents
* [About the Project](#about-the-project)
    * [Architecture of the System](#architecture-of-the-system)
    * [Deployment Plan](#deployment-plan)
* [Zipkin](#zipkin)
   * [What do we need to run the Zipkin Server?](#what-do-we-need-to-run-the-zipkin-server)
   * [How to include Zipkin to the project under observation?](#how-to-include-zipkin-to-the-project-under-observation)
   * [How we use Zipkin in our project](#how-we-use-zipkin-in-our-project)
* [Summary of Research](#summary-of-research)
* [Summary of Lessons Learnt](#summary-of-lessons-learnt)
* [How to Run](#how-to-run)
* [What we were able to get running](#what-we-were-able-to-get-running)
* [References](#references)
* [Submission 1 - Proposal](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/PROPOSAL.md)

---
### About the project
This project is about integrating Zipkin trace generation in our system. The system is basically a microservice based architure with services namely - *Airplane Servcie, Flight Approval Service & Reporting Service* which together helps in checking the status and location of the flight. We use Kafka as the message broker.

#### Architecture of the System
Our current architecture looks like the following:  
We have Postman as the subsystem which creates data and feeds it into the Airplane Service (actually a microservice). The mentioned microservice processes the message, and forwards it into Kafka. When the Flight Approval Service is online, it will continuously read from the flight-request-topic, process the read data and send it toward the next kafka topic which is called flight-response-topic. From that topic, the third microservice called Reporting Service will read the messages, process them and discard them.

We were actually interested in what kind of performance Kafka has as a message broker in our system. This is the reason why we were tracing the messages from the moment when the service puts them into the queue until the other one removes them again. Naturally, we tried to keep the latency inside the microservices as small as possible, so that it won't influence our measurements. The reason for that is, that we pack Zipkin related data into our messages like traceId, parentSpanId, startTimestamp (of the span) and  serviceName, and we do not want a message to stay in the queue for an unncecessarily long time, as it would invalidate the timestamps of the traces. 

![Architecture Diagram](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/architecture_diagram.png)

#### Deployment Plan
We would be using Kubernetes to deploy the differnet microservices and helper-services like Kafka, Zookeeper, etc. In the diagram below, the box represents nodes and all the microservices will be in a 1 node 1 pod way including Zipkin. Whereas, Kafka, Zookeeper & Schema Registry would make a logical whole and would be kept together in 1 node.

![Deployment Diagram](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/deplyment-diagram.png)

### Zipkin
Zipkin is a distributed tracing system. Its task is to collect `spans`, which are created and sent by the instrumented services. By adding the necessary information to these spans inside the services, we can fulfill use cases ranging from *"What services will a message be sent to? Is the communication flow correct?"*, up to *"How long do the observed operations take? Which methods inside the service require the longest? Where are exactly our bottlenecks?"*

We are mostly using Zipkin because of multiple reasons (using Benno’s Bachelor Thesis [1], and [2] as reference):  

* Zipkin is much easier to deploy. It is out-of-the-box and comes with each component we need.
* Zipkin is more mature as Jaeger and has a bigger community. According to [2], it “is used in a wide range of industries and is well suited for the enterprise IT world, which primarily uses Java. Choose Zipkin if you want to go with the mainstream, widely adopted solution.”
* Benno already has experience with Zipkin, as he wrote his bachelor thesis in integrating it to the Milo Server, which is a Java Implementation of OPC-UA (at least, it is implemented for OPC-UA specific Remote Procedure Calls). 
* There are no special hardships in realizing tracing for the project we are using, as it allows passing the necessary information easily (i.e., TraceID, ParentSpanID, Timestamp, ServiceName). The difficulty in the bachelor thesis was, how to get these things to the server without disrupting the message format (as a C++ implementation should still be able to understand the same messages).

#### What do we need to run the Zipkin Server? 
Basically, running the Zipkin server is easy, as the deployment comes in one single, ready2use docker container. For that, you just need to execute “docker pull openzipkin/zipkin” and then “docker run -d -p 9411:9411 openzipkin/zipkin”. Once that is done and the container is running, you can connect to the port 9411 of the machine it is running on, and you should see the query page of Zipkin. It should look like this:

![Zipkin Home](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/zipkin-home.png)
 
#### How to include Zipkin to the project under observation?
Unfortunately, and this is a real drawback of Zipkin, the documentation consists of a single readme, which can be found here: [ZipkinDocumentation](https://hub.docker.com/r/openzipkin/zipkin). Therefore, it is difficult to find all the dependencies when doing it for the first time. Naturally, it was easier now because we already knew which ones to use and where to find them. Basically, we have added the following dependencies to the build.gradle file:
* ```compile "io.zipkin.zipkin2:zipkin:2.21.6"```
* ```compile "io.zipkin.reporter2:zipkin-sender-urlconnection:2.16.3"```
* ```compile "io.zipkin.reporter2:zipkin-reporter:2.15.0"```
* ```compile "io.zipkin.brave:brave:5.12.4"``` (used for MutableSpans; an alternative to Span.builder())

Once this is done, we import following Classes in general:
* ```zipkin2.reporter.AsyncReporter```
* ```zipkin2.reporter.urlconnection.URLConnectionSender```
* ```zipkin2.Span```
* ```zipkin2.Endpoint```

Furthermore, we have implemented a minimal demo project to realize communication with Zipkin:

![Zipkin Sample App](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/zipkin-sample-app.png)


In the image above we can see that in the main method, we create and send two spans. Even though we could add it as boiler-plate code, like in the first part, there is also a way to package everything neatly in a function, since normally, the procedure is always the same. It is very important to report the spans to Zipkin via AsyncReporter.   

We would like to note that we did not add an Endpoint to the span builder in this example (which also requires a builder like Endpoint.builder()), as we tried to keep things as trivial as possible. However, it would be possible to add that, and with that, we could even append information to Zipkin like ServiceName or IP Address. But in our project, we did add it of course.  

#### How we use Zipkin in our project
We are creating spans the moment before sending the messages to kafka. The reason for that is, that we wished to figure out what kind of impact Kafka has on our prototype, and whether we should stay with Kafka for further development cycles, or to switch to a different technology. In this case, we are more interested in the impact of performance.   

Each bar that we see in Zipkin means the time it spent on the wire and the queue (plus in the low-level code of send/receive). The blank spaces between the blocks mean how much time it has spent in a service, which is not our focus right now.  

### Summary of Research
We have successfully integrated Zipkin tracing into our microservices. More details about the same have already been discussed in the above sections. Talking about the resuts/conclusions from the trace generated, we can say that Kafka seems to have performance issues for our use case and hence as a further improvement, we can switch to other message brokers like AWS SQS, OPCUA, etc. 

![First Trace](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/trace.png)

### Summary of Lessons Learnt
In this course, while implementing the project we learnt the following things:
* How to create and devleop microservices
* Basics of Docker (creating images, running said images in containers and using repositories like [Docker Hub](https://hub.docker.com/))
* How to run microservice architecture with Docker on local system as well as via Kubernetes
* Implementing tracing via Zipkin

### How to Run
* Basic libraries to be installed before running:
   * docker
   * kubernetes 
   * java 11
   * gradle 6.6.1
   * openzipkin/zipkin
* Creating Docker Images
   * `cd AirplaneService`
   * `gradle build .`
   * `docker build -t airplane-service .`
   * `docker tag airplane-service:latest airplane-service:airplaneservice`
   * `docker tag airplane-service:airplaneservice alphastream99/airplane-service:airplaneservice`
   * `docker push alphastream99/airplane-service:airplaneservice`
   * `cd ../FlightApprovalService`
   * `gradle build .`
   * `docker build -t approval-service .`
   * `docker tag approval-service:latest approval-service:approvalservice`
   * `docker tag approval-service:approvalservice alphastream99/approval-service:approvalservice`
   * `docker push alphastream99/approval-service:approvalservice`
   * `cd ../ReportingService`
   * `gradle build .`
   * `docker build -t reporting-service .`
   * `docker tag reporting-service:latest reporting-service:reportingservice`
   * `docker tag reporting-service:reportingservice alphastream99/reporting-service:reportingservice`
   * `docker push alphastream99/reporting-service:reportingservice`
   * `cd ..`
* Running Kubernetes:
   * `cd k8_yamls`
   * `kubectl apply -f airplane-service/namespace.yaml`
   * `kubectl apply -f airplane-service/service.yaml`
   * `kubectl apply -f airplane-service/deployment.yaml`
   * `kubectl apply -f flight-approval-service/namespace.yaml`
   * `kubectl apply -f flight-approval-service/service.yaml`
   * `kubectl apply -f flight-approval-service/deployment.yaml`
   * `kubectl apply -f reporting-service/namespace.yaml`
   * `kubectl apply -f reporting-service/service.yaml`
   * `kubectl apply -f reporting-service/deployment.yaml`
   * `kubectl apply -f zipkin/namespace.yaml`
   * `kubectl apply -f zipkin/service.yaml`
   * `kubectl apply -f zipkin/deployment.yaml`
   * `kubectl apply -f kafka-zookeeper-schema-registry/schema-registry-service.yaml`
   * `kubectl apply -f kafka-zookeeper-schema-registry/schema-registry-deployment.yaml`
   * `kubectl apply -f kafka-zookeeper-schema-registry/kafka-service.yaml`
   * `kubectl apply -f kafka-zookeeper-schema-registry/kafka-deployment.yaml`
   * `kubectl apply -f kafka-zookeeper-schema-registry/zookeeper-service.yaml`
   * `kubectl apply -f kafka-zookeeper-schema-registry/zookeeper-deployment.yaml`
   * `cd ..`
   * `kubectl port-forward deployment/airplane-service 8099:8099 & kubectl port-forward deployment/flight-approval-service 8090:8090 & kubectl port-forward deployment/reporting-service 8097:8097 & kubectl port-forward deployment/zipkin 9411:9411& kubectl port-forward deployment/schema-registry 8081:8081 & kubectl port-forward deployment/kafka 9092:9092& kubectl port-forward deployment/zookeeper 2181:2181`
* Now that the things are running we need to run a few scripts manually:
   * Open terminal window and go to the repository 
   * `chmod u+x scripts/create-topic.sh`
   * `chmod u+x scripts/kafka_2.13-2.6.0/bin/*`
   * Run `kubectl get pods` and check the pod-name for kafka
   * `kubectl exec -it --namespace default <pod-name-from-previous-command> bash
   * `cd scripts/kafka_2.13-2.6.0/bin`
   * `../../../create-topic.sh`
   * `exit`
* Sample cURL: 
```bash
curl --location --request POST 'http://localhost:8099/airplane-service/flight-request/testDe' \
--header 'Content-Type: application/json' \
--data-raw '{
   "flightCode":"DEL-123",
   "fromCoordinates":{
      "lat":30,
      "lon":26
   },
   "toCoordinates":{
      "lat":22,
      "lon":-11
   },
   "uuid":"",
   "traceId": "idd",
   "parentId": -1,
   "startTimestamp": 0,
   "serviceName": "serviceName"
}'
```
### What we were able to get running
We tried really hard to deploy things via Kubernetes. However, we were not able to get the pods running as we wished. However, we managed to run zipkin.
![Pods](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/minikube_pods.png)
![Workloads](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/minikube_workloads.png)
![Workloads](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/commands.png)

In the end, we were not able to deploy the system on Kubernetes, and we were only able to do it locally. We would now show how things looked like during runtime:

Start of the microservice called "Airplane Service":
![Start](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/starting.png)

Sending of the first message accompanied with the first trace:
![First Message](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/sending_first_request.png)

One can see the message in the Kafka Topic:
![First Message](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/topic.png)

Searching for a trace, Zipkin presented us:
![First Trace](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/zipkin_1.png)

If we expand that trace, we can find out what services it has traversed and how long it took. If we click on it, it will lead us to the full description of the page, which we have posted in a previous chapter. But expanding that entry will show us:
![First Trace Expanded](https://github.com/AlphaStream99/airplaneTrackingWithZipkin/blob/main/images/operation/zipking_services_traversed.png)

### References
[1] Benno Pereszteghy's "Integration von Funktionstracing in OPC-UA"  *(For more details, please contat the author at benno99@gmx.at)*  
[2] [Zipkin vs. Jaeger: What Is the Difference?](https://lumigo.io/what-is-distributed-tracing/zipkin-vs-jaeger-what-is-the-difference/) (last accessed 17.01.2023)

---
### Group Members
* Benno Pereszteghy (K11771353)
* Dunja Zivotin (K11932162)
* Prankur Agarwal (K12102893)
