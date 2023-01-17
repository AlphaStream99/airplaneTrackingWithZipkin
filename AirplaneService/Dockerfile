FROM openjdk:11

RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.6.1-bin.zip
RUN unzip gradle-6.6.1-bin.zip
ENV GRADLE_HOME /gradle-6.6.1
ENV PATH $PATH:/gradle-6.6.1/bin

RUN mkdir -p /app/
COPY . /app/AirplaneService
RUN cd /app/AirplaneService && gradle build

EXPOSE 8099
ADD build/libs/AirplaneServices-0.0.1-SNAPSHOT.jar /app/airplane-service.jar
ENTRYPOINT ["java"]
CMD ["-jar", "app/airplane-service.jar"]