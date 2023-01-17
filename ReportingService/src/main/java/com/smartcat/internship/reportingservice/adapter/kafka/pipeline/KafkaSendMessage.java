package com.smartcat.internship.reportingservice.adapter.kafka.pipeline;

import avro.schemas.v1.FlightRequest;
import avro.schemas.v1.FlightResponse;
import com.smartcat.internship.reportingservice.domain.Event;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

public class KafkaSendMessage {


    public static void sendApprovedOrRejectedMessage() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
