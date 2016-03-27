package com.example.lenovo.eddystonefinder.BeaconValidators;

import com.example.lenovo.eddystonefinder.Utils.Utils;
import com.example.lenovo.eddystonefinder.beans.Beacons;
import com.example.lenovo.eddystonefinder.extra.L;

import java.util.Arrays;

/**
 * Created by lenovo on 3/28/2016.
 */
public class UidValidator {
    public static void validate(String deviceAddress, byte[] serviceData, Beacons beacon) {
        int txPower = (int) serviceData[1];
        beacon.setTxPower(txPower);

        byte[] uidBytes = Arrays.copyOfRange(serviceData, 2, 18);
        beacon.setUidValue(Utils.toHexString(uidBytes));

        if (beacon.getUidServiceData() == null) {
            beacon.setUidServiceData(serviceData.clone());
        } else {
            L.m("Some Proble in UID Service Data");
        }
    }
}
