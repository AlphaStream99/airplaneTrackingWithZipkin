package com.smartcat.internship.airplaneservices.adapter.kafka.pipeline;

import avro.schemas.v1.FlightRequest;
import com.smartcat.internship.airplaneservices.adapter.kafka.producer.KafkaFlightRequestProducer;
import com.smartcat.internship.airplaneservices.domain.Event;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

public class KafkaSendMessage {

    public static final String SERVICE_NAME = "FlightRequestService";
    private static String lastTraceId = null;

    public static void sendMessage(KafkaFlightRequestProducer producer, Event event) {
        try {
            producer.beginTransaction();
            ProducerRecord<String, FlightRequest> record =
                    new ProducerRecord<>(event.getTopic(),
                            event.getPartition(),
                            (String) event.getKey(),
                            (FlightRequest) event.getValue());
            record.value().setTraceId(getNextTraceId());
            record.value().setParentId(-1);
            record.value().setStartTimestamp(System.currentTimeMillis()*1000);
            record.value().setServiceName(SERVICE_NAME);
            producer.send(record);
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

    public static String getNextTraceId() {
        if (lastTraceId == null) lastTraceId = "1";
        else {
            long asLong = Long.parseLong(lastTraceId);
            asLong = asLong + 1;
            lastTraceId = String.valueOf(asLong);
        }
        System.out.println("last trace id: "+lastTraceId);
        return lastTraceId;
    }
}
