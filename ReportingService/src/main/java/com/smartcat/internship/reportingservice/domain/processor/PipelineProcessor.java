package com.smartcat.internship.reportingservice.domain.processor;

import com.smartcat.internship.reportingservice.domain.Event;

public interface PipelineProcessor {
    Object process(Event event);
    boolean accepts (Event event);
}
