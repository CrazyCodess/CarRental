package com.hhu.carrental.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by demeiyan on 2017/5/18 00:24.
 */

public class MyOrientationListener implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float lastX = 0;
    //private OrientationEventListener
    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
