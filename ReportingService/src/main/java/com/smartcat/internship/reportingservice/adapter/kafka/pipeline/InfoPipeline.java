package com.smartcat.internship.reportingservice.adapter.kafka.pipeline;

import avro.schemas.v1.FlightResponse;
import com.smartcat.internship.reportingservice.adapter.kafka.configuration.ChannelMappingConfiguration;
import com.smartcat.internship.reportingservice.adapter.kafka.consumer.KafkaInfoConsumer;
import com.smartcat.internship.reportingservice.domain.ApplicationRunner;
import com.smartcat.internship.reportingservice.domain.PayloadConverter;
import com.smartcat.internship.reportingservice.domain.processor.FlightInfoPipelineProcessor;
import com.smartcat.internship.reportingservice.domain.processor.PipelineProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zipkin2.Span;


import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Component
public class InfoPipeline {

    private Consumer<String, Object> consumer;
    private List<PipelineProcessor> processor;
    private PayloadConverter converter;
    private final ChannelMappingConfiguration configuration;

    @Autowired
    public InfoPipeline(KafkaInfoConsumer consumer,
                        FlightInfoPipelineProcessor processor,
                        PayloadConverter converter,
                        final ChannelMappingConfiguration configuration)
    {
        this.consumer = consumer;
        this.processor = Collections.singletonList(processor);
        this.converter = converter;
        this.configuration = configuration;
    }

    public void processNext() {
        ConsumerRecords<String, Object> records = this.consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, Object> record : records) {
            if (record.value() instanceof FlightResponse) {
                FlightResponse request = (FlightResponse) record.value();
                String traceId = String.valueOf(request.get("traceId"));
                String parentId = String.valueOf(request.get("parentId"));
                long startTimestampInMillisec = Long.parseLong(String.valueOf(request.get("startTimestamp")));
                String serviceName = String.valueOf(request.get("serviceName"));
                Span receiveSpan = ApplicationRunner.createSpan(
                        traceId,
                        parentId,
                        startTimestampInMillisec,
                        serviceName
                );
                ApplicationRunner.sendToZipkin(receiveSpan);

            }

        }
    }
}
