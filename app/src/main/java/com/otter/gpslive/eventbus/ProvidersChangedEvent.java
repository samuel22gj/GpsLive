package com.otter.gpslive.eventbus;

public class ProvidersChangedEvent {
    private boolean gpsEnabled;
    private boolean networkEnabled;
    private boolean passiveEnabled;

    public ProvidersChangedEvent(
            boolean gpsEnabled, boolean networkEnabled, boolean passiveEnabled) {
        this.gpsEnabled = gpsEnabled;
        this.networkEnabled = networkEnabled;
        this.passiveEnabled = passiveEnabled;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(boolean gpsEnabled) {
        this.gpsEnabled = gpsEnabled;
    }

    public boolean isNetworkEnabled() {
        return networkEnabled;
    }

    public void setNetworkEnabled(boolean networkEnabled) {
        this.networkEnabled = networkEnabled;
    }

    public boolean isPassiveEnabled() {
        return passiveEnabled;
    }

    public void setPassiveEnabled(boolean passiveEnabled) {
        this.passiveEnabled = passiveEnabled;
    }
}
