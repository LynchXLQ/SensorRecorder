package com.example.sensorrecorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
    /*
    get sensor data as fast as possible
    public static final int SENSOR_DELAY_FASTEST = 0;  0ms
    rate suitable for games
    public static final int SENSOR_DELAY_GAME = 1;  20ms
    rate suitable for the user interface
    public static final int SENSOR_DELAY_UI = 2;  60ms
    rate (default) suitable for screen orientation changes
    public static final int SENSOR_DELAY_NORMAL = 3;  200ms
    */
    private static final int sensitivity = 3;
    private static final int REQUEST_CODE = 1024;
    public final static String TAG = "Sensor";

    private TextView tv_accelerometer_x_result;
    private TextView tv_accelerometer_y_result;
    private TextView tv_accelerometer_z_result;
    private TextView tv_linear_acceleration_x_result;
    private TextView tv_linear_acceleration_y_result;
    private TextView tv_linear_acceleration_z_result;
    private TextView tv_gyroscope_x_result;
    private TextView tv_gyroscope_y_result;
    private TextView tv_gyroscope_z_result;
    private TextView tv_magnetic_x_result;
    private TextView tv_magnetic_y_result;
    private TextView tv_magnetic_z_result;
    private TextView tv_orientation_Azimuth_result;
    private TextView tv_orientation_Pitch_result;
    private TextView tv_orientation_Roll_result;

    private EditText file_name;
    private Button bt_start, bt_stop;
    public static boolean start = false;
    private StringBuffer sb_acc;
    private StringBuffer sb_lacc;
    private StringBuffer sb_gyro;
    private StringBuffer sb_mag;
    private StringBuffer sb_ori;
    private String internalDirectory, externalDirectory;

    // Sensors
    private Accelerometer accelerometer;
    private LinearAcceleration linearAcceleration;
    private Gyroscope gyroscope;
    private MagneticField magneticField;
    private Orientation orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_sensor_info = findViewById(R.id.sensor_info);
        tv_accelerometer_x_result = findViewById(R.id.tv_accelerometer_x_result);
        tv_accelerometer_y_result = findViewById(R.id.tv_accelerometer_y_result);
        tv_accelerometer_z_result = findViewById(R.id.tv_accelerometer_z_result);
        tv_linear_acceleration_x_result = findViewById(R.id.tv_linear_acceleration_x_result);
        tv_linear_acceleration_y_result = findViewById(R.id.tv_linear_acceleration_y_result);
        tv_linear_acceleration_z_result = findViewById(R.id.tv_linear_acceleration_z_result);
        tv_gyroscope_x_result = findViewById(R.id.tv_gyroscope_x_result);
        tv_gyroscope_y_result = findViewById(R.id.tv_gyroscope_y_result);
        tv_gyroscope_z_result = findViewById(R.id.tv_gyroscope_z_result);
        tv_magnetic_x_result = findViewById(R.id.tv_magnetic_x_result);
        tv_magnetic_y_result = findViewById(R.id.tv_magnetic_y_result);
        tv_magnetic_z_result = findViewById(R.id.tv_magnetic_z_result);
        tv_orientation_Azimuth_result = findViewById(R.id.tv_orientation_Azimuth_result);
        tv_orientation_Pitch_result = findViewById(R.id.tv_orientation_Pitch_result);
        tv_orientation_Roll_result = findViewById(R.id.tv_orientation_Roll_result);

        bt_start = findViewById(R.id.bt_start);
        bt_stop = findViewById(R.id.bt_stop);
        bt_start.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        bt_stop.setEnabled(false);
        file_name = findViewById(R.id.et_file);

        requestPermissions();
        sb_acc = new StringBuffer();
        sb_lacc = new StringBuffer();
        sb_gyro = new StringBuffer();
        sb_mag = new StringBuffer();
        sb_ori = new StringBuffer();
        sb_acc.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
        sb_lacc.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
        sb_gyro.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
        sb_mag.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
        sb_ori.append("Date,").append("Timestamp,").append("Azimuth,").append("Pitch,").append("Roll").append("\n");

        // Path
        internalDirectory = this.getExternalFilesDir(null).getAbsolutePath();   // 内部储存路径
        externalDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();   // 外部储存路径

        // show sensor information
        DetectSensor detectSensor = new DetectSensor(this);
        detectSensor.setText(tv_sensor_info);

        // Accelerometer Sensor
        accelerometer = new Accelerometer(this);
        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void setText(String timeStamp, String x, String y, String z) {
                tv_accelerometer_x_result.setText(x);
                tv_accelerometer_y_result.setText(y);
                tv_accelerometer_z_result.setText(z);
                if (start) {
                    sb_acc.append(utils.getFormatDate()).append(",").append(timeStamp).append(",").append(x).append(",").append(y).append(",").append(z).append("\n");
                }
            }
        });

        // Linear Acceleration Sensor
        linearAcceleration = new LinearAcceleration(this);
        linearAcceleration.setListener(new LinearAcceleration.Listener() {
            @Override
            public void setText(String timeStamp, String x, String y, String z) {
                tv_linear_acceleration_x_result.setText(x);
                tv_linear_acceleration_y_result.setText(y);
                tv_linear_acceleration_z_result.setText(z);
                if (start) {
                    sb_lacc.append(utils.getFormatDate()).append(",").append(timeStamp).append(",").append(x).append(",").append(y).append(",").append(z).append("\n");
                }
            }
        });

        // Gyroscope Sensor
        gyroscope = new Gyroscope(this);
        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void setText(String timeStamp, String x, String y, String z) {
                tv_gyroscope_x_result.setText(x);
                tv_gyroscope_y_result.setText(y);
                tv_gyroscope_z_result.setText(z);
                if (start) {
                    sb_gyro.append(utils.getFormatDate()).append(",").append(timeStamp).append(",").append(x).append(",").append(y).append(",").append(z).append("\n");
                }
            }
        });

        // Magnetic Field Sensor
        magneticField = new MagneticField(this);
        magneticField.setListener(new MagneticField.Listener() {
            @Override
            public void setText(String timeStamp, String x, String y, String z) {
                tv_magnetic_x_result.setText(x);
                tv_magnetic_y_result.setText(y);
                tv_magnetic_z_result.setText(z);
                if (start) {
                    sb_mag.append(utils.getFormatDate()).append(",").append(timeStamp).append(",").append(x).append(",").append(y).append(",").append(z).append("\n");
                }
            }
        });

        // Orientation
        orientation = new Orientation(this);
        orientation.setListener(new Orientation.Listener() {
            @Override
            public void setText(String timeStamp, String ori_Azimuth, String ori_Pitch, String ori_Roll) {
                tv_orientation_Azimuth_result.setText(ori_Azimuth);
                tv_orientation_Pitch_result.setText(ori_Pitch);
                tv_orientation_Roll_result.setText(ori_Roll);
                if (start) {
                    sb_ori.append(utils.getFormatDate()).append(",").append(timeStamp).append(",").append(ori_Azimuth).append(",").append(ori_Pitch).append(",").append(ori_Roll).append("\n");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometer.register(sensitivity);
        linearAcceleration.register(sensitivity);
        gyroscope.register(sensitivity);
        magneticField.register(sensitivity);
        orientation.register(sensitivity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        accelerometer.unregister();
        linearAcceleration.unregister();
        gyroscope.unregister();
        magneticField.unregister();
        orientation.unregister();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                bt_start.setEnabled(false);
                bt_stop.setEnabled(true);
                T.showToast(this, "Start Recording");
                start = true;
                break;
            case R.id.bt_stop:
                if (start) {
                    start = false;
                    bt_start.setEnabled(true);
                    bt_stop.setEnabled(false);
                    String rootPath = externalDirectory + File.separator + file_name.getText().toString();
                    String acc_path = externalDirectory + File.separator + file_name.getText().toString() + "_acc" + ".txt";
                    String lacc_path = externalDirectory + File.separator + file_name.getText().toString() + "_lacc" + ".txt";
                    String gyro_path = externalDirectory + File.separator + file_name.getText().toString() + "_gyro" + ".txt";
                    String mag_path = externalDirectory + File.separator + file_name.getText().toString() + "_mag" + ".txt";
                    String ori_path = externalDirectory + File.separator + file_name.getText().toString() + "_ori" + ".txt";
                    Log.i("path -> ", rootPath);
                    T.saveAsFileWriter(sb_acc.toString(), acc_path);
                    T.saveAsFileWriter(sb_lacc.toString(), lacc_path);
                    T.saveAsFileWriter(sb_gyro.toString(), gyro_path);
                    T.saveAsFileWriter(sb_mag.toString(), mag_path);
                    T.saveAsFileWriter(sb_ori.toString(), ori_path);

                    sb_acc = new StringBuffer();
                    sb_lacc = new StringBuffer();
                    sb_gyro = new StringBuffer();
                    sb_mag = new StringBuffer();
                    sb_ori = new StringBuffer();
                    sb_acc.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
                    sb_lacc.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
                    sb_gyro.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
                    sb_mag.append("Date,").append("Timestamp,").append("x,").append("y,").append("z").append("\n");
                    sb_ori.append("Date,").append("Timestamp,").append("Azimuth,").append("Pitch,").append("Roll").append("\n");

                    T.showToast(this, "Save to " + rootPath, true);
                } else {
                    T.showToast(this, "Please Start First");
                }
                break;
        }
    }

    private void requestPermission(String data_path, String content) {
        //判断版本大于等于android 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    private void requestPermissions() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            String[] requestPermissions = packageInfo.requestedPermissions;
            boolean need_requestPermissions = false;
            Log.i("requestPermissions/", "number=" + requestPermissions.length);
            for (String p : requestPermissions) {
                Log.i("requestPermissions", p);

                if (ContextCompat.checkSelfPermission(this, p)
                        != PackageManager.PERMISSION_GRANTED) {
                    need_requestPermissions = true;
                }

            }
            if (need_requestPermissions)
                ActivityCompat.requestPermissions(this, requestPermissions, 1);
        }
    }
}