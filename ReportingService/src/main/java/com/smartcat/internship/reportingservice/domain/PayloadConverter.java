package com.smartcat.internship.reportingservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class PayloadConverter {

    private ObjectMapper objectMapper;

    public PayloadConverter() {
        this.objectMapper =  new ObjectMapper();
    }


}
