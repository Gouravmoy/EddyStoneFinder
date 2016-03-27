package com.example.lenovo.eddystonefinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.example.lenovo.eddystonefinder.BeaconValidators.UidValidator;
import com.example.lenovo.eddystonefinder.MyApplication;
import com.example.lenovo.eddystonefinder.R;
import com.example.lenovo.eddystonefinder.utils.Utils;
import com.example.lenovo.eddystonefinder.adapter.EddystoneListAdapter;
import com.example.lenovo.eddystonefinder.beans.Beacons;
import com.example.lenovo.eddystonefinder.extra.Constants;
import com.example.lenovo.eddystonefinder.extra.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = "ScannerActivity";
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final int delay = 5000;

    // An aggressive scan for nearby devices that reports immediately.
    private static final ScanSettings SCAN_SETTINGS =
            new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0)
                    .build();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static Handler scannHandler;

    // The Eddystone Service UUID, 0xFEAA.
    private static final ParcelUuid EDDYSTONE_SERVICE_UUID =
            ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");

    private BluetoothLeScanner scanner;
    private EddystoneListAdapter arrayAdapter;

    private List<ScanFilter> scanFilters;
    private ScanCallback scanCallback;

    private Map<String /* device address */, Beacons> deviceToBeaconMap = new HashMap<>();

    ArrayList<Beacons> eddyStoneList;

    ListView eddyStoneListView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        eddyStoneList = new ArrayList<>();
        arrayAdapter = new EddystoneListAdapter(MyApplication.getAppContext());

        eddyStoneListView = (ListView) findViewById(R.id.device_list_eddystone);
        eddyStoneListView.setAdapter(arrayAdapter);

        scanFilters = new ArrayList<>();
        scanFilters.add(new ScanFilter.Builder().setServiceUuid(EDDYSTONE_SERVICE_UUID).build());

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                ScanRecord scanRecord = result.getScanRecord();
                if (scanRecord == null) {
                    return;
                }
                String deviceAddress = result.getDevice().getAddress();
                Beacons beacon;

                if (!deviceToBeaconMap.containsKey(deviceAddress)) {
                    beacon = new Beacons(deviceAddress, result.getRssi());
                    deviceToBeaconMap.put(deviceAddress, beacon);
                    eddyStoneList.add(beacon);
                    toolbar.setSubtitle("Found beacons : " + eddyStoneList.size());
                    arrayAdapter.replaceWith(eddyStoneList);
                } else {
                    deviceToBeaconMap.get(deviceAddress).setRssi(result.getRssi());
                }
                byte[] serviceData = scanRecord.getServiceData(EDDYSTONE_SERVICE_UUID);
                validateServiceData(deviceAddress, serviceData);
                scanner.stopScan(scanCallback);
            }

            @Override
            public void onScanFailed(int errorCode) {
                switch (errorCode) {
                    case SCAN_FAILED_ALREADY_STARTED:
                        L.m("SCAN_FAILED_ALREADY_STARTED");
                        break;
                    case SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                        L.m("SCAN_FAILED_APPLICATION_REGISTRATION_FAILED");
                        break;
                    case SCAN_FAILED_FEATURE_UNSUPPORTED:
                        L.m("SCAN_FAILED_FEATURE_UNSUPPORTED");
                        break;
                    case SCAN_FAILED_INTERNAL_ERROR:
                        L.m("SCAN_FAILED_INTERNAL_ERROR");
                        break;
                    default:
                        L.m("Scan failed, unknown error code");
                        break;
                }
            }
        };
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void validateServiceData(String deviceAddress, byte[] serviceData) {
        Beacons beacon = deviceToBeaconMap.get(deviceAddress);
        if (serviceData == null) {
            String err = "Null Eddystone service data";
            L.m(deviceAddress + " -> " + err);
            return;
        }
        Log.v(TAG, deviceAddress + " " + Utils.toHexString(serviceData));
        switch (serviceData[0]) {
            case Constants.UID_FRAME_TYPE:
                UidValidator.validate(deviceAddress, serviceData, beacon);
                break;
            default:
                String err = String.format("Invalid frame type byte %02X", serviceData[0]);
                L.m(deviceAddress + " -> " + err);
                break;
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private void init() {
        BluetoothManager manager = (BluetoothManager) MyApplication.getAppContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = manager.getAdapter();
        if (btAdapter == null) {
            showFinishingAlertDialog("Bluetooth Error", "Bluetooth not detected on device");
        } else if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            scanner = btAdapter.getBluetoothLeScanner();
        }

    }

    private void showFinishingAlertDialog(String title, String message) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //getActivity().finish();
                    }
                }).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (scanner != null) {
            scanner.stopScan(scanCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacksAndMessages(null);
        /*if (scanner != null) {
            scanner.startScan(scanFilters, SCAN_SETTINGS, scanCallback);
        }*/
        scannHandler = new Handler();
        scannHandler.postDelayed(new Runnable() {
            public void run() {
                if (scanner != null) {
                    scanner.startScan(scanFilters, SCAN_SETTINGS, scanCallback);
                }
                scannHandler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                init();
            } else {
                //getActivity().finish();
            }
        }
    }
}
