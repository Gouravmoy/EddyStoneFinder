package com.example.lenovo.eddystonefinder.beans;

import java.util.Arrays;

/**
 * Created by lenovo on 3/27/2016.
 */
public class Beacons {

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

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getLastSeenTimeStamp() {
        return lastSeenTimeStamp;
    }

    public void setLastSeenTimeStamp(long lastSeenTimeStamp) {
        this.lastSeenTimeStamp = lastSeenTimeStamp;
    }

    public byte[] getUidServiceData() {
        return uidServiceData;
    }

    public void setUidServiceData(byte[] uidServiceData) {
        this.uidServiceData = uidServiceData;
    }

    public byte[] getTlmServiceData() {
        return tlmServiceData;
    }

    public void setTlmServiceData(byte[] tlmServiceData) {
        this.tlmServiceData = tlmServiceData;
    }

    public byte[] getUrlServiceData() {
        return urlServiceData;
    }

    public void setUrlServiceData(byte[] urlServiceData) {
        this.urlServiceData = urlServiceData;
    }

    public String getUidValue() {
        return uidValue;
    }

    public void setUidValue(String uidValue) {
        this.uidValue = uidValue;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public String getUrlValue() {
        return urlValue;
    }

    public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
    }
}
