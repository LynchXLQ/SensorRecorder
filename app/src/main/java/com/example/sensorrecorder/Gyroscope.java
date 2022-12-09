package com.example.sensorrecorder;

import static com.example.sensorrecorder.MainActivity.start;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Gyroscope {
    public interface Listener {
        void setText(String timeStamp, String x, String y, String z);
    }

    private Listener listener;

    public void setListener(Listener l) {
        listener = l;
    }

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private String timeStamp, x, y, z;
    private int i = 0;

    public Gyroscope(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                timeStamp = utils.getTimeStamp();
                x = Float.toString(event.values[0]);
                y = Float.toString(event.values[1]);
                z = Float.toString(event.values[2]);
                Log.i("Freq ->", "gyro "+ i++);
                listener.setText(timeStamp, x, y, z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }


    public void register(int sensitivity) {
        sensorManager.registerListener(sensorEventListener, sensor, sensitivity);
    }

    public void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
