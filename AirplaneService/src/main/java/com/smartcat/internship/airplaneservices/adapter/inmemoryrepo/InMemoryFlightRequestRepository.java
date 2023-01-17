package com.smartcat.internship.airplaneservices.adapter.inmemoryrepo;

import avro.schemas.v1.FlightRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class InMemoryFlightRequestRepository implements FlightRequestRepository {

    private HashMap<String, String> repo;

    public InMemoryFlightRequestRepository() {
        this.repo = new HashMap<>();
    }

    @Override
    public void add(FlightRequest fr) {
        repo.put(fr.getFlightCode(), fr.getUuid());
    }

    @Override
    public String find(String flightCode) {
        return repo.get(flightCode);
    }

    @Override
    public void remove(String flightCode) {
        repo.remove(flightCode);
    }
}
