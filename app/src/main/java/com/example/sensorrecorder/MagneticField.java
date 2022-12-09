package com.example.sensorrecorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class MagneticField {
    public interface Listener {
        void setText(String timeStamp, String x, String y, String z);
    }

    private Listener listener;

    public void setListener(Listener l) {
        listener = l;
    }

    private SensorManager sensorManager;
    private Sensor mag_sensor;
    private SensorEventListener sensorEventListener;
    private String timeStamp, x_mag, y_mag, z_mag;
    private int i = 1;

    public MagneticField(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                timeStamp = utils.getTimeStamp();
                x_mag = Float.toString(event.values[0]);
                y_mag = Float.toString(event.values[1]);
                z_mag = Float.toString(event.values[2]);
                Log.i("Freq ->", "mag "+ i++);
                listener.setText(timeStamp, x_mag, y_mag, z_mag);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

    }


    public void register(int sensitivity) {
        sensorManager.registerListener(sensorEventListener, mag_sensor, sensitivity);
    }

    public void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
