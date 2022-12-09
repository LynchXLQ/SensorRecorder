package com.example.sensorrecorder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Orientation implements SensorEventListener{
    public interface Listener {
        void setText(String timeStamp, String ori_Azimuth, String ori_Pitch, String ori_Roll);
    }

    private Listener listener;

    public void setListener(Listener l) {
        listener = l;
    }

    private SensorManager sensorManager;
    private Sensor acc_sensor, mag_sensor;
    private String timeStamp;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private int i = 1;

    public Orientation(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = event.values.clone();
                break;
        }
        if (mGravity != null && mGeomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                String ori_Azimuth = Float.toString(orientation[0]);
                String ori_Pitch = Float.toString(orientation[1]);
                String ori_Roll = Float.toString(orientation[2]);
                timeStamp = utils.getTimeStamp();
                Log.i("Freq ->", "ori "+ i++);
                listener.setText(timeStamp, ori_Azimuth, ori_Pitch, ori_Roll);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void register(int sensitivity){
        sensorManager.registerListener(this, acc_sensor, sensitivity);
        sensorManager.registerListener(this, mag_sensor, sensitivity);
    }

    public void unregister(){
        sensorManager.unregisterListener(this);
    }
}
