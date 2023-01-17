package com.smartcat.internship.airplaneservices.adapter.kafka.producer;

import avro.schemas.v1.FlightRequest;
import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.KafkaFlightApprovalRequestConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaFlightRequestProducer extends KafkaProducer<String, FlightRequest> {

    @Autowired
    public KafkaFlightRequestProducer(final KafkaFlightApprovalRequestConfiguration configuration) {
        super(configuration.getProperties());
        initTransactions();
    }

}

