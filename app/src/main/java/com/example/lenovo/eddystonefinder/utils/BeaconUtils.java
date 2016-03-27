package com.example.lenovo.eddystonefinder.utils;

/**
 * Created by lenovo on 3/28/2016.
 */
public class BeaconUtils {

    public static double distanceFromRssi(int rssi, int txPower0m) {
        int pathLoss = txPower0m - rssi;
        return Math.pow(10, (pathLoss - 41) / 20.0);
    }
}
