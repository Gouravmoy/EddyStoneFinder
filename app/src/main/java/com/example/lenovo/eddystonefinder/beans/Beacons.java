package com.example.lenovo.eddystonefinder.beans;

import java.util.Arrays;

/**
 * Created by lenovo on 3/27/2016.
 */
class Beacons {

    final String deviceAddress;
    int rssi;
    long timeStamp = System.currentTimeMillis();
    long lastSeenTimeStamp = System.currentTimeMillis();
    byte[] uidServiceData;
    byte[] tlmServiceData;
    byte[] urlServiceData;

    String uidValue;
    int txPower;
    String urlValue;

    public Beacons(String deviceAddress, int rssi) {
        this.deviceAddress = deviceAddress;
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "Beacons{" +
                "deviceAddress='" + deviceAddress + '\'' +
                ", rssi=" + rssi +
                ", timeStamp=" + timeStamp +
                ", lastSeenTimeStamp=" + lastSeenTimeStamp +
                ", uidServiceData=" + Arrays.toString(uidServiceData) +
                ", tlmServiceData=" + Arrays.toString(tlmServiceData) +
                ", urlServiceData=" + Arrays.toString(urlServiceData) +
                ", uidValue='" + uidValue + '\'' +
                ", txPower=" + txPower +
                ", urlValue='" + urlValue + '\'' +
                '}';
    }
}
