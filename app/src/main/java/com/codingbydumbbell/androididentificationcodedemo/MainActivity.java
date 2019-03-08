package com.codingbydumbbell.androididentificationcodedemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_READ_PHONE_STATE_DEVICEID = 71;
    private static final int REQUEST_CODE_READ_PHONE_STATE_SERIALID = 73;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for Device ID、Subscriber ID/SimSerialNumber
//        getDeviceId();

        // for SerialNumber
        getSerialNumber();

        // for Mac Address
//        getWiFiMacAddress();
//        getBluetoothMacAddress();

        // for Android ID
//        getAndroidId();

        // for UUID
//        getUUID();
    }

    private void getUUID() {
        String uuid = UUID.randomUUID().toString();
        Log.d(TAG, "getUUID: " + uuid);
    }

    private void getAndroidId() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "getAndroidId: " + androidId);
    }

    private void getBluetoothMacAddress() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            String btAddress = adapter.getAddress();
            Log.d(TAG, "getBluetoothMacAddress: " + btAddress);
        } else Log.d(TAG, "No Bluetooth");
    }


    private void getWiFiMacAddress() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wm.getConnectionInfo();
        String macAddress = info.getMacAddress();
        Log.d(TAG, "getWiFiMacAddress: " + macAddress);
    }

    private void getSerialNumber() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_READ_PHONE_STATE_SERIALID);
                return;
            }
            String serial = Build.getSerial();
            Log.d(TAG, "Build.getSerial(): " + serial);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Log.d(TAG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD");
            String serial = Build.SERIAL;
            Log.d(TAG, "Build.SERIAL: " + serial);
        }
    }

    private void getDeviceId() {

        // 取得 TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // 確認使用者權限，若裝置版本是6.0（包含）以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_READ_PHONE_STATE_DEVICEID);
                return;
            }
        }

        // 如果裝置版本大於 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String imei = tm.getImei();
            String meid = tm.getMeid();
            Log.d(TAG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O");
            Log.d(TAG, "IMEI: " + imei + ", MEID: " + meid);
        } else {
            String deviceId = tm.getDeviceId();
            Log.d(TAG, "DeviceId: " + deviceId);
        }

        // for Subscriber ID／SimSerialNumber
        String subscriberId = tm.getSubscriberId();
        String simSerialNumber = tm.getSimSerialNumber();
        Log.d(TAG, "SubscriberId: " + subscriberId + ", SimSerialNumber: " + simSerialNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_PHONE_STATE_DEVICEID &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) getDeviceId();
        else if (requestCode == REQUEST_CODE_READ_PHONE_STATE_SERIALID &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) getSerialNumber();
        else Log.d(TAG, "onRequestPermissionsResult: REQUEST_CODE_READ_PHONE_STATE: No Permission");
    }
}
