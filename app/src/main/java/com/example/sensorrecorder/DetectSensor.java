package com.example.sensorrecorder;

import static android.support.v4.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class DetectSensor{
    private SensorManager sensorManager;
    private StringBuilder sbAllSensors = new StringBuilder();

    public DetectSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sbAllSensors.append("This mobile phone has ").append(allSensors.size()).append(" sensors, include: \n\n");
        for (Sensor s : allSensors) {
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    sbAllSensors.append(s.getType()).append(" Accelerometer sensor").append("\n").append("Sensor Name:\t").append(s.getName()).append("\n").append("Sensor Vendor:\t").append(s.getVendor()).append("\n").append("Sensor Power:\t").append(s.getPower()).append("\n").append("\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sbAllSensors.append(s.getType()).append(" Gyroscope sensor").append("\n").append("Sensor Name:\t").append(s.getName()).append("\n").append("Sensor Vendor:\t").append(s.getVendor()).append("\n").append("Sensor Power:\t").append(s.getPower()).append("\n").append("\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sbAllSensors.append(s.getType()).append(" Magnetic field sensor").append("\n").append("Sensor Name:\t").append(s.getName()).append("\n").append("Sensor Vendor:\t").append(s.getVendor()).append("\n").append("Sensor Power:\t").append(s.getPower()).append("\n").append("\n");
                    break;
                default:
//                    sbAllSensors.append(s.getType()).append(" 其他传感器").append("\n").append("Sensor Name:\t").append(s.getName()).append("\n").append("Sensor Vendor:\t").append(s.getVendor()).append("\n").append("Sensor Power:\t").append(s.getPower()).append("\n");
                    break;
            }
        }
    }

    public void setText(TextView tv){
        tv.setText(sbAllSensors);
    }
}
