package com.smartcat.internship.reportingservice.domain;

import com.smartcat.internship.reportingservice.adapter.kafka.pipeline.InfoPipeline;
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

    private final InfoPipeline pipeline;

    @Autowired
    public ApplicationRunner(final InfoPipeline pipeline){
        this.pipeline = pipeline;

        Executors.newSingleThreadExecutor().submit(this::run);
    }

    public static Span createSpan(String traceId, String parentSpanId, long startTimeInMillisec, String serviceName) {
        return Span.newBuilder()
                .traceId(traceId)
                .id((parentSpanId == null ? "1" : parentSpanId + "1"))
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

    public void run(){
        while (true){
            try {
                pipeline.processNext();
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
