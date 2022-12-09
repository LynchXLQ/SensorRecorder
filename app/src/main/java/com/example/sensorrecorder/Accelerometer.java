package com.example.sensorrecorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class Accelerometer {
    public interface Listener {
        void setText(String timeStamp, String x, String y, String z);
    }

    private Listener listener;

    public void setListener(Listener l) {
        listener = l;
    }

    private SensorManager sensorManager;
    private Sensor acc_sensor;
    private SensorEventListener sensorEventListener;
    private String timeStamp, x_acc, y_acc, z_acc;
    private int i = 0;

    public Accelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                timeStamp = utils.getTimeStamp();
                x_acc = Float.toString(event.values[0]);
                y_acc = Float.toString(event.values[1]);
                z_acc = Float.toString(event.values[2]);
                Log.i("Freq ->", "acc " + i++);
                listener.setText(timeStamp, x_acc, y_acc, z_acc);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }

    public void register(int sensitivity) {
        sensorManager.registerListener(sensorEventListener, acc_sensor, sensitivity);
    }

    public void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
