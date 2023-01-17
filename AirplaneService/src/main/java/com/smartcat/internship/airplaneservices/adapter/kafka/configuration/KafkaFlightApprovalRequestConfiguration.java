package com.smartcat.internship.airplaneservices.adapter.kafka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix="kafka.producer.flight-request")
public class KafkaFlightApprovalRequestConfiguration extends KafkaBaseProducerConfiguration {
}

