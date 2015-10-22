package com.otter.gpslive;

import android.content.Context;
import android.location.GpsSatellite;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SatelliteAdapter extends RecyclerView.Adapter<SatelliteAdapter.ViewHolder> {
    private static final String TAG = SatelliteAdapter.class.getSimpleName();

    private Context mContext;
    private List<GpsSatellite> mSatelliteList;

    public SatelliteAdapter(Context ctx, List<GpsSatellite> satelliteList) {
        mContext = ctx;
        mSatelliteList = satelliteList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView azimuth;
        public TextView elevation;
        public TextView prn;
        public TextView snr;
        public TextView almanac;
        public TextView ephemeris;
        public TextView used_in_fix;

        public ViewHolder(View itemView) {
            super(itemView);

            number = (TextView) itemView.findViewById(R.id.number);
            azimuth = (TextView) itemView.findViewById(R.id.azimuth);
            elevation = (TextView) itemView.findViewById(R.id.elevation);
            prn = (TextView) itemView.findViewById(R.id.prn);
            snr = (TextView) itemView.findViewById(R.id.snr);
            almanac = (TextView) itemView.findViewById(R.id.almanac);
            ephemeris = (TextView) itemView.findViewById(R.id.ephemeris);
            used_in_fix = (TextView) itemView.findViewById(R.id.used_in_fix);
        }
    }

    @Override
    public SatelliteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.satellite_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SatelliteAdapter.ViewHolder holder, int position) {
        GpsSatellite gpsSatellite = mSatelliteList.get(position);

        String degrees = mContext.getString(R.string.unit_degree);

        holder.number.setText(mContext.getString(R.string.format_number, position + 1));
        holder.azimuth.setText(mContext.getString(R.string.format_value_and_unit,
                String.valueOf(gpsSatellite.getAzimuth()), degrees));
        holder.elevation.setText(mContext.getString(R.string.format_value_and_unit,
                String.valueOf(gpsSatellite.getElevation()), degrees));
        holder.prn.setText(String.valueOf(gpsSatellite.getPrn()));
        holder.snr.setText(String.valueOf(gpsSatellite.getSnr()));
        holder.almanac.setText(mContext.getString(
                gpsSatellite.hasAlmanac() ? R.string.yes : R.string.no));
        holder.ephemeris.setText(mContext.getString(
                gpsSatellite.hasEphemeris() ? R.string.yes : R.string.no));
        holder.used_in_fix.setText(mContext.getString(
                gpsSatellite.usedInFix() ? R.string.yes : R.string.no));
    }

    @Override
    public int getItemCount() {
        return mSatelliteList.size();
    }

    public void swap(List<GpsSatellite> satelliteList) {
        mSatelliteList.clear();
        mSatelliteList.addAll(satelliteList);
        notifyDataSetChanged();
    }
}
