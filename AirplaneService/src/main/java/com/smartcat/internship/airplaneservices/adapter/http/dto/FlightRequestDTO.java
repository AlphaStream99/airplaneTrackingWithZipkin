package com.smartcat.internship.airplaneservices.adapter.http.dto;


public class FlightRequestDTO {

    private String flightCode;
    private CoordinatesDTO fromCoordinates;
    private CoordinatesDTO toCoordinates;
    private String uuid;

    private String traceId;
    private long parentId;
    private long startTimestamp;
    private String serviceName;

    public String getTraceId() {
        return traceId;
    }

    public long getParentId() {
        return parentId;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public FlightRequestDTO() {
    }

    public FlightRequestDTO(String flightCode, CoordinatesDTO fromCoordinates, CoordinatesDTO toCoordinates, String uuid, String traceId, long parentId, long startTimestamp, String serviceName) {
        this.flightCode = flightCode;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
        this.uuid = uuid;
        this.traceId = traceId;
        this.parentId = parentId;
        this.startTimestamp = startTimestamp;
        this.serviceName = serviceName;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public CoordinatesDTO getFromCoordinates() {
        return fromCoordinates;
    }

    public void setFromCoordinates(CoordinatesDTO fromCoordinates) {
        this.fromCoordinates = fromCoordinates;
    }

    public CoordinatesDTO getToCoordinates() {
        return toCoordinates;
    }

    public void setToCoordinates(CoordinatesDTO toCoordinates) {
        this.toCoordinates = toCoordinates;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
