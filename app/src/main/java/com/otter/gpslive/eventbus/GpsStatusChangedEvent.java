package com.otter.gpslive.eventbus;

import android.location.GpsStatus;

public class GpsStatusChangedEvent {
    private GpsStatus gpsStatus;

    public GpsStatusChangedEvent(GpsStatus gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public GpsStatus getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(GpsStatus gpsStatus) {
        this.gpsStatus = gpsStatus;
    }
}
