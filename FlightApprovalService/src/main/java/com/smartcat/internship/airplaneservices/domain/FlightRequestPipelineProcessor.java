package com.smartcat.internship.airplaneservices.domain;

import avro.schemas.v1.FlightRequest;
import avro.schemas.v1.FlightResponse;
import avro.schemas.v1.FlightStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlightRequestPipelineProcessor implements PipelineProcessor<String, FlightRequest> {

    //private FlightCodeRepository flightCodeRepository;

    @Autowired
    public FlightRequestPipelineProcessor() {
        //this.flightCodeRepository = flightCodeRepository;
    }

    @Override
    public FlightResponse process(Event event) {

        //int numOfAirplanes = flightCodeRepository.numberOfAirplanes();
        String flightCode = (String) event.getKey();
        FlightRequest fr = (FlightRequest) event.getValue();
        String uuid = fr.getUuid();

        float lat1 = fr.getFromCoordinates().getLat();
        float lon1 = fr.getFromCoordinates().getLon();
        float lat2 = fr.getToCoordinates().getLat();
        float lon2 = fr.getToCoordinates().getLon();

        double distance = DistanceCalculator.distance(lat1, lon1, lat2, lon2);

        String traceId = fr.getTraceId();
        long parentId = fr.getParentId();
        long startTimestamp = fr.getStartTimestamp();
        String serviceName = fr.getServiceName();

        double flightTimeInMinutes = (distance/900.0) * 60;
        // this was changed for the cloud computing project to exclude redis
        FlightStatus status = FlightStatus.APPROVED;
        // Da ima manje od 10 aviona i da dati flightCode ne postoji vec
//        if (numOfAirplanes <= 10 && !flightCodeRepository.find(flightCode)) {
//            flightCodeRepository.add(event.getKey().toString());
//            status = FlightStatus.APPROVED;
//        }

        return new FlightResponse(flightCode, (float) distance, (int) flightTimeInMinutes, status, uuid, traceId, parentId, startTimestamp, serviceName);

    }

    @Override
    public boolean accepts(Event event) {
        return event.getValue().getClass().equals(FlightRequest.class);
    }
}
