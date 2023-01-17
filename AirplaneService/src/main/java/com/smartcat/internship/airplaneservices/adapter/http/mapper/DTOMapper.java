package com.smartcat.internship.airplaneservices.adapter.http.mapper;

import avro.schemas.v1.Coordinates;
import avro.schemas.v1.FlightRequest;
import com.smartcat.internship.airplaneservices.adapter.http.dto.FlightRequestDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DTOMapper {

    public FlightRequest mapToflightRequest(FlightRequestDTO dto) {
        return FlightRequest.newBuilder()
                .setFlightCode(dto.getFlightCode())
                .setFromCoordinates(
                        Coordinates.newBuilder()
                                .setLat(dto.getFromCoordinates().getLat())
                                .setLon(dto.getFromCoordinates().getLon())
                                .build()
                ).setToCoordinates(
                        Coordinates.newBuilder()
                                .setLat(dto.getToCoordinates().getLat())
                                .setLon(dto.getToCoordinates().getLon())
                                .build()
                )
                .setUuid(UUID.randomUUID().toString())
                .setTraceId(dto.getTraceId())
                .setParentId(dto.getParentId())
                .setStartTimestamp(dto.getStartTimestamp())
                .setServiceName(dto.getServiceName())
                .build();
    }

}
