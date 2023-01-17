package com.smartcat.internship.airplaneservices.domain.services;


import avro.schemas.v1.FlightRequest;
import com.smartcat.internship.airplaneservices.adapter.inmemoryrepo.FlightRequestRepository;
import com.smartcat.internship.airplaneservices.adapter.inmemoryrepo.InMemoryFlightRequestRepository;
import com.smartcat.internship.airplaneservices.adapter.kafka.pipeline.KafkaSendMessage;
import com.smartcat.internship.airplaneservices.adapter.kafka.producer.KafkaFlightRequestProducer;
import com.smartcat.internship.airplaneservices.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zipkin2.Endpoint;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

@Service
public class FlightRequestService {

    private KafkaFlightRequestProducer producer;
    private FlightRequestRepository repo;
    public static AsyncReporter<Span> rep;


    static {
        rep = AsyncReporter.create(URLConnectionSender.create("http://localhost:9411/api/v2/spans"));
    }

    @Autowired
    public FlightRequestService(
            KafkaFlightRequestProducer producer,
            InMemoryFlightRequestRepository repo
    ) {
        this.producer = producer;
        this.repo = repo;
    }


    public void sendFlightRequest(Event event) {
        KafkaSendMessage.sendMessage(producer, event);
    }
}
