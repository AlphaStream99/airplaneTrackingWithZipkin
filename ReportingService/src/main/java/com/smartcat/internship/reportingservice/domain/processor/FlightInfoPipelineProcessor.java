package com.smartcat.internship.reportingservice.domain.processor;

import avro.schemas.v1.FlightResponse;
import com.smartcat.internship.reportingservice.domain.Event;
import org.springframework.stereotype.Component;


@Component
public class FlightInfoPipelineProcessor implements PipelineProcessor{

    @Override
    public Object process(Event event) {
        return event.getValue();
    }

    @Override
    public boolean accepts(Event event) {
        return event.getValue().getClass().equals(FlightResponse.class);
    }
}
