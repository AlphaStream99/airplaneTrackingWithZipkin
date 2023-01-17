package com.smartcat.internship.airplaneservices.adapter.kafka.pipeline;

import avro.schemas.v1.FlightRequest;
import avro.schemas.v1.FlightResponse;
import com.smartcat.internship.airplaneservices.ApplicationRunner;
import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.ChannelMappingConfiguration;
import com.smartcat.internship.airplaneservices.adapter.kafka.consumer.KafkaFlightRequestConsumer;
import com.smartcat.internship.airplaneservices.adapter.kafka.producer.KafkaFlightResponseProducer;
import com.smartcat.internship.airplaneservices.domain.Event;
import com.smartcat.internship.airplaneservices.domain.FlightRequestPipelineProcessor;
import com.smartcat.internship.airplaneservices.domain.PipelineProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zipkin2.Span;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Component
public class FlightRequestPipeline {

    private final ChannelMappingConfiguration configuration;
    private final Consumer<String, Object> consumer;
    private final Producer<String, Object> producer;
    private final List<PipelineProcessor<String, FlightRequest>> processors;

    @Autowired
    public FlightRequestPipeline(KafkaFlightRequestConsumer consumer,
                                 KafkaFlightResponseProducer producer,
                                 FlightRequestPipelineProcessor processor,
                                 ChannelMappingConfiguration configuration) {
        this.consumer = consumer;
        this.producer = producer;
        this.processors = Collections.singletonList(processor);
        this.configuration = configuration;
    }


    public void processNext() {
        ConsumerRecords<String, Object> records = this.consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, Object> record : records) {
            if (record.value() instanceof FlightRequest) {
                FlightRequest request = (FlightRequest) record.value();
                String traceId = request.getTraceId();
                long parentId = request.getParentId();
                long startTimestampInMillisec = request.getStartTimestamp();
                String serviceName = request.getServiceName();
                System.out.println("traceId in FA: "+traceId);
                System.out.println("parentId in FA: "+parentId);
                System.out.println("startTimestamp in FA: "+startTimestampInMillisec);
                System.out.println("serviceName in FA: "+serviceName);
                Span receiveSpan = ApplicationRunner.createSpan(
                        traceId,
                        parentId,
                        startTimestampInMillisec,
                        serviceName
                );
                System.out.println("span to string: "+receiveSpan.toString());
                ApplicationRunner.sendToZipkin(receiveSpan);

                Event event = new Event(
                        record.key(),
                        record.value(),
                        this.configuration.getFlightResponse(), //Because we need the flight-request-topic to send a response
                        record.partition()
                );
                this.processors.forEach(
                        pipelineProcessor -> {
                            if (pipelineProcessor.accepts(event)) {
                                FlightResponse response = pipelineProcessor.process(event);
                                Span sendingSpan = ApplicationRunner.createSpan(
                                        traceId,
                                        Long.parseLong(receiveSpan.id()),
                                        System.currentTimeMillis() * 1000,
                                        "FlightApprovalService"
                                );
                                response.setTraceId(sendingSpan.traceId());
                                response.setParentId(Long.parseLong(sendingSpan.parentId()));
                                response.setStartTimestamp(sendingSpan.timestamp());
                                response.setServiceName(sendingSpan.remoteServiceName());
                                KafkaSendMessage.sendMessage(this.producer, response, event);
                            }
                        }
                );
            }

        }
    }
}
