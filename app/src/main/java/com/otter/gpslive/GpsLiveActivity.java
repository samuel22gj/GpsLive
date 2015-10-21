package com.otter.gpslive;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.otter.gpslive.eventbus.BusProvider;
import com.otter.gpslive.eventbus.GpsStatusChangedEvent;
import com.otter.gpslive.eventbus.LocationChangedEvent;
import com.otter.gpslive.eventbus.NmeaChangedEvent;
import com.otter.gpslive.eventbus.ProvidersChangedEvent;
import com.squareup.otto.Produce;

public class GpsLiveActivity extends AppCompatActivity
        implements LocationListener, GpsStatus.Listener, GpsStatus.NmeaListener {
    private static final String TAG = GpsLiveActivity.class.getSimpleName();

    private static final long MIN_UPDATE_INTERVAL  = 0L; // milliseconds
    private static final float MIN_UPDATE_DISTANCE = 0F; // meters

    private LocationManager mLocationManager;

    private ViewPager mViewPager;
    private OverviewFragment mOverviewFragment;
    private SatelliteFragment mSatelliteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_live);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager == null) {
            // Show not support location service dialog to close activity.
            showErrorDialog(getString(R.string.not_support_location_service));
            finish();
        }

        mOverviewFragment = OverviewFragment.newInstance();
        mSatelliteFragment = SatelliteFragment.newInstance();

        initToolbar();
        initViewPager();
        initTabLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Register event bus Otto.
        BusProvider.getInstance().register(this);

        if (mLocationManager != null) {
            registerGpsListeners();

            // Show providers information.
            showProvidersInfo();

            // Show location information.
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) showLocationInfo(lastKnownLocation);

            // Show GPS status information.
            if (mLocationManager.getGpsStatus(null) != null) showGpsStatusInfo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Unregister event bus Otto.
        BusProvider.getInstance().unregister(this);

        if (mLocationManager != null) {
            unregisterGpsListeners();
        }
    }

    private void showErrorDialog(String msg) {
        ErrorDialog errorDialog = ErrorDialog.newInstance(msg);
        errorDialog.show(getSupportFragmentManager(), ErrorDialog.FRAGMENT_TAG);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mOverviewFragment, getString(R.string.nav_item_overview));
        adapter.addFragment(mSatelliteFragment, getString(R.string.nav_item_satellite));
        mViewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        TabLayout tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(mViewPager);
    }

    private void registerGpsListeners() {
        // Register the listener with the Location Manager to receive location updates.
        if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            Log.i(TAG, "Register GPS Location Provider");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_UPDATE_INTERVAL, MIN_UPDATE_DISTANCE, this);
        }
        if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            Log.i(TAG, "Register Network Location Provider");
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_UPDATE_INTERVAL, MIN_UPDATE_DISTANCE, this);
        }

        // Register the listener to receiving notifications when GPS status has changed.
        Log.i(TAG, "Register GpsStatusListener: " + mLocationManager.addGpsStatusListener(this));

        // Register the listener to receiving NMEA sentences from the GPS.
        Log.i(TAG, "Register NmeaListener: " + mLocationManager.addNmeaListener(this));
    }

    private void unregisterGpsListeners() {
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        mLocationManager.removeNmeaListener(this);
    }

    private Location getLastKnownLocation() {
        return  mLocationManager.getLastKnownLocation(
                mLocationManager.getBestProvider(new Criteria(), false));
    }

    private void showProvidersInfo() {
        BusProvider.getInstance().post(produceProvidersChangedEvent());
    }

    @Produce
    public ProvidersChangedEvent produceProvidersChangedEvent() {
        return new ProvidersChangedEvent(
                mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER),
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER),
                mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)
        );
    }

    private void showLocationInfo(Location location) {
        BusProvider.getInstance().post(new LocationChangedEvent(location));
    }

    @Produce
    public LocationChangedEvent produceLocationChangedEvent() {
        return new LocationChangedEvent(getLastKnownLocation());
    }

    private void showGpsStatusInfo() {
        BusProvider.getInstance().post(produceGpsStatusChangedEvent());

        // TODO: Show each GpsSatellite detail.
        GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
        for (GpsSatellite gpsSatellite : gpsStatus.getSatellites()) {
        }
    }

    @Produce
    public GpsStatusChangedEvent produceGpsStatusChangedEvent() {
        return new GpsStatusChangedEvent(mLocationManager.getGpsStatus(null));
    }

    private void showNmeaInfo(long timestamp, String nmea) {
        BusProvider.getInstance().post(new NmeaChangedEvent(timestamp, nmea));
    }

    /* **************** *
     * LocationListener *
     * **************** */
    @Override
    public void onLocationChanged(Location location) {
//        Log.v(TAG, "onLocationChanged");

        showLocationInfo(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String statusStr = null;
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                statusStr = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                statusStr = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                statusStr = "AVAILABLE";
                break;
        }
        Log.v(TAG, "onStatusChange(" + provider + ", " + statusStr + ")");
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Log.v(TAG, "onProviderEnabled: " + provider);

        showProvidersInfo();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Log.v(TAG, "onProviderDisabled: " + provider);

        showProvidersInfo();
    }

    /* ****************** *
     * GpsStatus.Listener *
     * ****************** */
    @Override
    public void onGpsStatusChanged(int event) {
        String eventStr = null;
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                eventStr = "GPS_EVENT_STARTED";
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                eventStr = "GPS_EVENT_STOPPED";
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                eventStr = "GPS_EVENT_FIRST_FIX";
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                eventStr = "GPS_EVENT_SATELLITE_STATUS";
                break;
        }
        Log.v(TAG, "onGpsStatusChanged(" + eventStr + ")");

        showGpsStatusInfo();
    }

    /* ********************** *
     * GpsStatus.NmeaListener *
     * ********************** */
    @Override
    public void onNmeaReceived(long timestamp, String nmea) {
//        Log.v(TAG, "onNmeaReceived: " + timestamp + ", " + nmea);

        showNmeaInfo(timestamp, nmea);
    }

    /* **** *
     * Menu *
     * **** */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gps_live, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_location_settings:
                // Open system location settings.
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
