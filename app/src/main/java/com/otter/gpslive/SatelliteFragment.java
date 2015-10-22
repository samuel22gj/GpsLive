package com.otter.gpslive;

import android.location.GpsSatellite;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otter.gpslive.eventbus.BusProvider;
import com.otter.gpslive.eventbus.SatellitesChangedEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class SatelliteFragment extends Fragment {
    private static final String TAG = SatelliteFragment.class.getSimpleName();

    private SatelliteAdapter mSatelliteAdapter;

    public static SatelliteFragment newInstance() {
        return new SatelliteFragment();
    }

    public SatelliteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSatelliteAdapter = new SatelliteAdapter(getContext(),
                new ArrayList<GpsSatellite>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_satellite, container, false);

        // Initial RecyclerView.
        RecyclerView satellite_recycler = (RecyclerView) root.findViewById(R.id.satellite_recycler);
        satellite_recycler.setHasFixedSize(true);
        satellite_recycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        satellite_recycler.setAdapter(mSatelliteAdapter);

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
    public void onSatellitesChanged(SatellitesChangedEvent event) {
        mSatelliteAdapter.swap(event.getSatellites());
    }
}
