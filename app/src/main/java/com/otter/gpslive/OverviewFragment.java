package com.otter.gpslive;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otter.gpslive.eventbus.BusProvider;
import com.otter.gpslive.eventbus.GpsStatusChangedEvent;
import com.otter.gpslive.eventbus.LocationChangedEvent;
import com.otter.gpslive.eventbus.NmeaChangedEvent;
import com.otter.gpslive.eventbus.ProvidersChangedEvent;
import com.squareup.otto.Subscribe;

public class OverviewFragment extends Fragment {
    private static final String TAG = OverviewFragment.class.getSimpleName();

    private TextView gps_status;
    private TextView network_status;
    private TextView passive_status;
    private TextView provider;
    private TextView accuracy;
    private TextView longitude;
    private TextView latitude;
    private TextView bearing;
    private TextView altitude;
    private TextView speed;
    private TextView time;
    private TextView max_satellites;
    private TextView satellites;
    private TextView first_fix_time;
    private TextView time_stamp;
    private TextView nmea;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_overview, container, false);

        gps_status = (TextView) root.findViewById(R.id.gps_status);
        network_status = (TextView) root.findViewById(R.id.network_status);
        passive_status = (TextView) root.findViewById(R.id.passive_status);
        provider = (TextView) root.findViewById(R.id.provider);
        accuracy = (TextView) root.findViewById(R.id.accuracy);
        longitude = (TextView) root.findViewById(R.id.longitude);
        latitude = (TextView) root.findViewById(R.id.latitude);
        bearing = (TextView) root.findViewById(R.id.bearing);
        altitude = (TextView) root.findViewById(R.id.altitude);
        speed = (TextView) root.findViewById(R.id.speed);
        time = (TextView) root.findViewById(R.id.time);
        max_satellites = (TextView) root.findViewById(R.id.max_satellites);
        satellites = (TextView) root.findViewById(R.id.satellites);
        first_fix_time = (TextView) root.findViewById(R.id.first_fix_time);
        time_stamp = (TextView) root.findViewById(R.id.time_stamp);
        nmea = (TextView) root.findViewById(R.id.nmea);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Register event bus Otto.
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister event bus Otto.
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onProvidersChanged(ProvidersChangedEvent event) {
        gps_status.setText(getString(
                event.isGpsEnabled() ? R.string.enable : R.string.disable));
        network_status.setText(getString(
                event.isNetworkEnabled() ? R.string.enable : R.string.disable));
        passive_status.setText(getString(
                event.isPassiveEnabled() ? R.string.enable : R.string.disable));
    }

    @Subscribe
    public void onLocationChanged(LocationChangedEvent event) {
        Location location = event.getLocation();

        String meters = getString(R.string.unit_meter);
        String metersPerSecond = getString(R.string.unit_meters_per_second);
        String degrees = getString(R.string.unit_degree);

        provider.setText(location.getProvider());
        latitude.setText(getString(R.string.format_value_and_unit,
                String.valueOf(location.getLatitude()), degrees));
        longitude.setText(getString(R.string.format_value_and_unit,
                String.valueOf(location.getLongitude()), degrees));
        altitude.setText(getString(R.string.format_value_and_unit,
                String.valueOf(location.getAltitude()), meters));
        accuracy.setText(getString(R.string.format_value_and_unit,
                String.valueOf(location.getAccuracy()), meters));
        bearing.setText(getString(R.string.format_value_and_unit,
                String.valueOf(location.getBearing()), degrees));
        speed.setText(getString(R.string.format_value_and_unit,
                String.valueOf(location.getSpeed()), metersPerSecond));
        time.setText(Util.convertMillisecondToDateTime(location.getTime()));
    }

    @Subscribe
    public void onGpsStatusChagned(GpsStatusChangedEvent event) {
        GpsStatus gpsStatus = event.getGpsStatus();

        String millisecond = getString(R.string.unit_millisecond);

        max_satellites.setText(String.valueOf(gpsStatus.getMaxSatellites()));
        int satelliteCount = 0;
        for (GpsSatellite gpsSatellite : gpsStatus.getSatellites()) {
            satelliteCount++;
        }
        satellites.setText(String.valueOf(satelliteCount));
        first_fix_time.setText(getString(R.string.format_value_and_unit,
                String.valueOf(gpsStatus.getTimeToFirstFix()), millisecond));
    }

    @Subscribe
    public void onNmeaChanged(NmeaChangedEvent event) {
        time_stamp.setText(Util.convertMillisecondToDateTime(event.getTimestamp()));
        nmea.setText(event.getNmea());
    }
}
