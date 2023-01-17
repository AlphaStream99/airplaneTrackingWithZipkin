package com.smartcat.internship.airplaneservices.adapter.kafka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.channel-mapping")
public class ChannelMappingConfiguration implements ChannelMapping{

    private String flightRequest;
    @Override
    public String getFlightRequest() { return flightRequest; }
    public void setFlightRequest(String flightRequest) {
        this.flightRequest = flightRequest;
    }


}
