package com.smartcat.internship.airplaneservices.adapter.kafka.configuration;

import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.KafkaBaseProducerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="kafka.producer.flight-response")
public class KafkaFlightApprovalResponseConfiguration extends KafkaBaseProducerConfiguration {
}
