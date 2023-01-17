package com.smartcat.internship.airplaneservices.domain;

import avro.schemas.v1.FlightResponse;

public interface PipelineProcessor <K, V > {
    FlightResponse process(Event requests);
    boolean accepts (Event requests);
}