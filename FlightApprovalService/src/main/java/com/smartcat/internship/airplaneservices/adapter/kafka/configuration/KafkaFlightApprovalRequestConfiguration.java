package com.smartcat.internship.airplaneservices.adapter.kafka.configuration;

import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.KafkaBaseConsumerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="kafka.consumer.flight-request")
public class KafkaFlightApprovalRequestConfiguration extends KafkaBaseConsumerConfiguration {
}
