package com.smartcat.internship.airplaneservices.adapter.inmemoryrepo;

import avro.schemas.v1.FlightRequest;

public interface FlightRequestRepository {
    void add(FlightRequest fr);
    String find(String flightCode);
    void remove (String flightCode);
}
