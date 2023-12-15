package com.example.sportmobli.util;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.polar.sdk.api.PolarBleApiCallback;
import com.polar.sdk.api.model.PolarDeviceInfo;

import java.util.UUID;

public class PolarBleApiUtil {
    public static PolarBleApiCallback getApiSetup(String deviceId, Context context, PolarListener listener){
        return new PolarBleApiCallback() {
            @Override
            public void deviceConnected(PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "Device connected " + deviceId);
                Toast.makeText(context, "Device connected " + deviceId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deviceConnecting(PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "Device connecting " + deviceId);
            }

            @Override
            public void deviceDisconnected(PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "Device disconnected " + deviceId);
            }

            @Override
            public void bleSdkFeatureReady(String identifier, com.polar.sdk.api.PolarBleApi.PolarBleSdkFeature feature) {
                Log.d(TAG, "feature ready " + feature);

                switch (feature) {
                    case FEATURE_POLAR_ONLINE_STREAMING:

                        listener.executeWhenReady();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void disInformationReceived(String identifier, UUID uuid, String value) {
                if (uuid.equals(UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb"))) {
                    String msg = "Firmware: " + value.trim();
                    Log.d(TAG, "Firmware: " + identifier + " " + value.trim());
                }
            }

            @Override
            public void batteryLevelReceived(String identifier, int level) {
                Log.d(TAG, "Battery level " + identifier + " " + level + "%");
            }
        };
    }
    public interface PolarListener{
         void executeWhenReady();
    }
}
