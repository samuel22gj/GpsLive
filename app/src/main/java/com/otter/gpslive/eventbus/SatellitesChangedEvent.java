package com.otter.gpslive.eventbus;

import android.location.GpsSatellite;

import java.util.List;

public class SatellitesChangedEvent {
    List<GpsSatellite> satellites;

    public SatellitesChangedEvent(List<GpsSatellite> satellites) {
        this.satellites = satellites;
    }

    public List<GpsSatellite> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<GpsSatellite> satellites) {
        this.satellites = satellites;
    }
}
