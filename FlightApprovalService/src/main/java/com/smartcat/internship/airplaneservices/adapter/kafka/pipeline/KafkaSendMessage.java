package com.smartcat.internship.airplaneservices.adapter.kafka.pipeline;

import avro.schemas.v1.FlightResponse;;
import com.smartcat.internship.airplaneservices.domain.Event;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

public class KafkaSendMessage {

    public static void sendMessage(Producer<String, Object> producer, FlightResponse response, Event event) {
        try {
            producer.beginTransaction();
            producer.send(new ProducerRecord<String, Object>(event.getTopic(),
                    event.getPartition(),
                    (String) event.getKey(),
                    response));
            producer.flush();
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException ex) {
            ex.printStackTrace();
            producer.close();
        } catch (KafkaException ex) {
            ex.printStackTrace();
            producer.abortTransaction();
        }
    }

}
