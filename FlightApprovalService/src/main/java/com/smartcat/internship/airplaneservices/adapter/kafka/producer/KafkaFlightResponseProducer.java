package com.smartcat.internship.airplaneservices.adapter.kafka.producer;

import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.KafkaFlightApprovalRequestConfiguration;
import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.KafkaFlightApprovalResponseConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
@Component
public class KafkaFlightResponseProducer extends KafkaProducer<String, Object> {

    @Autowired
    public KafkaFlightResponseProducer(final KafkaFlightApprovalResponseConfiguration configuration) {
        super(configuration.getProperties());
        initTransactions();
    }

}
