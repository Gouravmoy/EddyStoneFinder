package com.example.lenovo.eddystonefinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lenovo.eddystonefinder.R;
import com.example.lenovo.eddystonefinder.beans.Beacons;
import com.example.lenovo.eddystonefinder.beans.ViewHolder;
import com.example.lenovo.eddystonefinder.extra.L;
import com.example.lenovo.eddystonefinder.utils.BeaconUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 3/13/2016.
 */
public class EddystoneListAdapter extends BaseAdapter {
    private ArrayList<Beacons> eddystones;
    private LayoutInflater inflater;

    public EddystoneListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.eddystones = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return eddystones.size();
    }

    @Override
    public Beacons getItem(int position) {
        return eddystones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflateIfRequired(view, position, parent);
        bind(getItem(position), view);
        return view;
    }

    private void bind(Beacons eddystone, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.macTextView.setText(String.format("MAC: %s (%.2fm)", eddystone.getDeviceAddress(),
                BeaconUtils.distanceFromRssi(eddystone.getRssi(), eddystone.getTxPower())));
        holder.rssiTextView.setText("RSSI: " + eddystone.getRssi());

        holder.eddystoneNamespaceTextView.setText("Namespace: " + (eddystone.getUidValue() == null ? "-" : eddystone.getUidValue().substring(0, 20)));
        holder.eddystoneInstanceIdTextView.setText("Instance ID: " + (eddystone.getUidValue() == null ? "-" : eddystone.getUidValue().substring(20, 32)));
        holder.eddystoneUrlTextView.setText("URL: " + (eddystone.getUrlValue() == null ? "-" : eddystone.getUrlValue()));
        L.m("Namespace: " + (eddystone.getUidValue() == null ? "-" : eddystone.getUidValue().substring(0, 20)));
        L.m("Instance ID: " + (eddystone.getUidValue() == null ? "-" : eddystone.getUidValue().substring(20, 32)));
        L.m("Distance - " + BeaconUtils.distanceFromRssi(eddystone.getRssi(), eddystone.getTxPower()));
    }

    private View inflateIfRequired(View view, int position, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.eddystone_item, null);
            view.setTag(new ViewHolder(view));
        }
        return view;
    }

    public void replaceWith(List<Beacons> eddystones) {
        this.eddystones.clear();
        this.eddystones.addAll(eddystones);
        notifyDataSetChanged();
    }
}
