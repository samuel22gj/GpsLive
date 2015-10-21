package com.otter.gpslive.eventbus;

public class NmeaChangedEvent {
    private long timestamp;
    private String nmea;

    public NmeaChangedEvent(long timestamp, String nmea) {
        this.timestamp = timestamp;
        this.nmea = nmea;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNmea() {
        return nmea;
    }

    public void setNmea(String nmea) {
        this.nmea = nmea;
    }
}
