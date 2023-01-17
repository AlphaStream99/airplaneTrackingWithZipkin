package com.smartcat.internship.reportingservice.adapter.kafka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="kafka.consumer.report")
public class KafkaInfoConfiguration extends KafkaBaseConsumerConfiguration {
}
