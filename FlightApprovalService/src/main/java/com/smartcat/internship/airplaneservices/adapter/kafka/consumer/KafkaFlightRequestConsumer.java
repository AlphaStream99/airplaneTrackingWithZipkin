package com.smartcat.internship.airplaneservices.adapter.kafka.consumer;

import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.KafkaFlightApprovalRequestConfiguration;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaFlightRequestConsumer extends KafkaConsumer<String, Object> {

    @Autowired
    public KafkaFlightRequestConsumer(final KafkaFlightApprovalRequestConfiguration configuration) {
        super(configuration.getProperties());
        subscribe(configuration.getTopics());
    }
}
