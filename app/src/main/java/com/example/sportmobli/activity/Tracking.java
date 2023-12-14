package com.example.sportmobli.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.example.sportmobli.util.HRPlotter;
import com.polar.sdk.api.PolarBleApi;
import com.polar.sdk.api.PolarBleApiCallback;
import com.polar.sdk.api.PolarBleApiDefaultImpl;
import com.polar.sdk.api.errors.PolarInvalidArgument;
import com.polar.sdk.api.model.PolarDeviceInfo;
import com.polar.sdk.api.model.PolarHrData;

import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

import com.example.sportmobli.R;

public class Tracking extends AppCompatActivity {

    private PolarBleApi api;
    private final String deviceId = "A6FC0B2E";
    private Disposable hrDisposable;
    private HRPlotter plotter;
    private XYPlot plot;
    TextView currentHR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Button lolButton = findViewById(R.id.button3);
        Button dietButton = findViewById(R.id.button5);
        Button userProfileButton = findViewById(R.id.button6);
        Button trainingButton = findViewById(R.id.button4);

        lolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        dietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DietActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Training.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        api = PolarBleApiDefaultImpl.defaultImplementation(
                getApplicationContext(),
                EnumSet.of(
                        PolarBleApi.PolarBleSdkFeature.FEATURE_POLAR_ONLINE_STREAMING,
                        PolarBleApi.PolarBleSdkFeature.FEATURE_BATTERY_INFO,
                        PolarBleApi.PolarBleSdkFeature.FEATURE_DEVICE_INFO
                )
        );
        api.setApiLogger(str -> Log.d("SDK", str));
        api.setApiCallback(new PolarBleApiCallback() {
            @Override
            public void deviceConnected(PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "Device connected " + deviceId);
                Toast.makeText(getApplicationContext(), "Device connected " + deviceId, Toast.LENGTH_SHORT).show();
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
            public void bleSdkFeatureReady(String identifier, PolarBleApi.PolarBleSdkFeature feature) {
                Log.d(TAG, "feature ready " + feature);

                switch (feature) {
                    case FEATURE_POLAR_ONLINE_STREAMING:
                        streamHR();
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
        });

        try {
            api.connectToDevice(deviceId);
            Toast.makeText(getApplicationContext(), "Connecting to " + deviceId, Toast.LENGTH_SHORT).show();

            currentHR = findViewById(R.id.textViewCurrentHR);

            plotter = new HRPlotter();
            plot = findViewById(R.id.hr_view_plot);
            plotter.setListener(plot);
            plot.addSeries(plotter.series.getHrSeries(), plotter.hrFormatter);
            plot.addSeries(plotter.series.getRrSeries(), plotter.rrFormatter);
            plot.setRangeBoundaries(50, 100, BoundaryMode.AUTO);
            plot.setDomainBoundaries(0, 360000, BoundaryMode.AUTO);
            plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10.0);
            plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 60000.0);
            plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("#"));
            plot.setLinesPerRangeLabel(2);
            streamHR();
        } catch (PolarInvalidArgument e) {
            Toast.makeText(getApplicationContext(), "Failed to connect to " + deviceId + e, Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        startActivity();
    }

    public void startActivity(){


    }
    public void streamHR() {
        Toast.makeText(getApplicationContext(), "Device connected " + deviceId, Toast.LENGTH_SHORT).show();
        boolean isDisposed = hrDisposable == null || hrDisposable.isDisposed();
        if (isDisposed) {
            hrDisposable = api.startHrStreaming(deviceId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            hrData -> {
                                for (PolarHrData.PolarHrSample sample : hrData.getSamples()) {
                                    Log.d(TAG, "HR " + sample.getHr());
                                    int test = sample.getHr();
                                    currentHR.setText(String.valueOf(sample.getHr()));
                                    plotter.addValues(sample);
                                }
                            },
                            error -> {
                                Log.e(TAG, "HR stream failed. Reason " + error + ". Tried on device: " + deviceId);
                                hrDisposable = null;
                                Toast.makeText(getApplicationContext(), "Failed to connect to " + deviceId + ".\nError: " + error, Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            },
                            () -> Log.d(TAG, "HR stream complete")
                    );
        } else {
            // NOTE stops streaming if it is "running"
            hrDisposable.dispose();
            hrDisposable = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.shutDown();

    }
}