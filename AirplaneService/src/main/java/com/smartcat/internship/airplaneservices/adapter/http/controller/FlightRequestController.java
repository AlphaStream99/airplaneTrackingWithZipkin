package com.smartcat.internship.airplaneservices.adapter.http.controller;

import avro.schemas.v1.FlightRequest;
import com.smartcat.internship.airplaneservices.adapter.http.dto.FlightRequestDTO;
import com.smartcat.internship.airplaneservices.adapter.http.mapper.DTOMapper;
import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.ChannelMapping;
import com.smartcat.internship.airplaneservices.adapter.kafka.configuration.ChannelMappingConfiguration;
import com.smartcat.internship.airplaneservices.domain.Event;
import com.smartcat.internship.airplaneservices.domain.services.FlightRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airplane-service")
public class FlightRequestController {

    private FlightRequestService flightRequestService;
    private DTOMapper mapper;
    private ChannelMapping channelMapping;

    @Autowired
    public FlightRequestController(FlightRequestService flightRequestService,
                                   DTOMapper mapper,
                                   ChannelMappingConfiguration channelMappingConfiguration) {
        this.flightRequestService = flightRequestService;
        this.mapper = mapper;
        this.channelMapping = channelMappingConfiguration;
    }

    @PostMapping("/flight-request/{flightCode}")
    public ResponseEntity<Void> handleFlightRequest(@PathVariable String flightCode,
                                                    @RequestBody FlightRequestDTO dto) {
        final FlightRequest flightRequest = mapper.mapToflightRequest(dto);
        flightRequestService.sendFlightRequest(new Event(flightRequest.getFlightCode(),
                                    flightRequest,
                                    channelMapping.getFlightRequest(),
                                    null));
        return ResponseEntity.ok().build();
    }
}
