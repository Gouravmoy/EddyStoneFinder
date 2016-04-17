package com.example.lenovo.eddystonefinder.utils;

import com.example.lenovo.eddystonefinder.beans.Beacons;

import java.util.Map;

/**
 * Created by lenovo on 4/17/2016.
 */
public class DitanceUtil {
    public static String getNearestBeacon(Map<String, Beacons> deviceToBeaconMap) {
        String returnValue = "";
        double minDistance = 200;
        double currDistance = 0;
        for (Map.Entry<String, Beacons> beaconsEntry : deviceToBeaconMap.entrySet()) {
            Beacons eddystone = beaconsEntry.getValue();
            currDistance = BeaconUtils.distanceFromRssi(eddystone.getRssi(), eddystone.getTxPower());
            if (currDistance < 0.75) {
                if (minDistance > currDistance) {
                    minDistance = currDistance;
                    returnValue = eddystone.getDeviceAddress();
                }
            }
        }
        return returnValue;
    }
}