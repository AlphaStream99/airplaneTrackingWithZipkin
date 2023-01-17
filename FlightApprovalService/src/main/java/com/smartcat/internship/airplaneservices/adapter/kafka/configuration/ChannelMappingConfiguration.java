package com.smartcat.internship.airplaneservices.adapter.kafka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="kafka.channel-mapping")
public class ChannelMappingConfiguration {

    private String flightRequest;
    private String flightResponse;

    public String getFlightRequest() {
        return flightRequest;
    }

    public void setFlightRequest(String flightRequest) {
        this.flightRequest = flightRequest;
    }

    public String getFlightResponse() {
        return flightResponse;
    }

    public void setFlightResponse(String flightResponse) {
        this.flightResponse = flightResponse;
    }
}
