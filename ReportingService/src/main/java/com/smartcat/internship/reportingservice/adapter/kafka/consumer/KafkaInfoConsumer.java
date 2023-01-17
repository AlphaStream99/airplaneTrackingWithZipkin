package com.smartcat.internship.reportingservice.adapter.kafka.consumer;


import com.smartcat.internship.reportingservice.adapter.kafka.configuration.KafkaInfoConfiguration;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaInfoConsumer extends KafkaConsumer<String, Object> {

    @Autowired
    public KafkaInfoConsumer(final KafkaInfoConfiguration configuration) {
        super(configuration.getProperties());
        subscribe(configuration.getTopics());
    }
}
