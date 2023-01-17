package com.smartcat.internship.airplaneservices;

import com.smartcat.internship.airplaneservices.adapter.kafka.pipeline.FlightRequestPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zipkin2.Endpoint;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import java.util.concurrent.Executors;

@Component
public class ApplicationRunner {
    private static final AsyncReporter<Span> rep;

    static {
        rep = AsyncReporter.create(URLConnectionSender.create("http://localhost:9411/api/v2/spans"));
    }

    private final FlightRequestPipeline pipeline;

    @Autowired
    public ApplicationRunner(FlightRequestPipeline pipeline) {
        this.pipeline = pipeline;

        Executors.newSingleThreadExecutor().submit(this::run);
    }

    public static Span createSpan(String traceId, Long parentSpanId, long startTimeInMillisec, String serviceName) {
        return Span.newBuilder()
                .traceId(traceId)
                .id((parentSpanId == -1 ? 1 : parentSpanId + 1))
                .parentId(parentSpanId)
                .timestamp(startTimeInMillisec)
                .remoteEndpoint(Endpoint.newBuilder().serviceName(serviceName).build())
                .duration((System.currentTimeMillis() * 1000) - startTimeInMillisec)
                .build();
    }

    public static void sendToZipkin(Span span) {
        rep.report(span);
        rep.flush();
    }

    public void run() {
        while (true) {
            try {
                this.pipeline.processNext();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
