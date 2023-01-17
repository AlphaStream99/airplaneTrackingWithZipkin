package com.smartcat.internship.reportingservice.adapter.kafka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="kafka.channel-mapping")
public class ChannelMappingConfiguration implements ChannelMapping{

    private String flightResponse;

    @Override
    public String getFlightResponse() {
        return flightResponse;
    }

    @Override
    public void setFlightResponse(String flightResponse) {
        this.flightResponse = flightResponse;
    }


}
