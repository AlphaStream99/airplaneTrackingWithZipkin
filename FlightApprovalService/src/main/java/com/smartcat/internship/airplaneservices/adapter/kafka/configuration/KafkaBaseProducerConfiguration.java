package com.smartcat.internship.airplaneservices.adapter.kafka.configuration;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public abstract class KafkaBaseProducerConfiguration {

    private Properties properties;

    public Properties getProperties() {
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getCanonicalName());
        properties.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, "io.confluent.kafka.serializers.subject.RecordNameStrategy");
        properties.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, "false");
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
