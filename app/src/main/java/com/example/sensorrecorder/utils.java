package com.example.sensorrecorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class utils {
    public static String getTimeStamp() {
        return Long.toString(System.currentTimeMillis());
    }

    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals(Environment.MEDIA_MOUNTED);
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static String getFormatDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    public static String getPath(String directory, String fileName) {   // 获取数据保存地址
        if (isExternalStorageAvailable() && hasSDCardMounted()) {
            return directory + File.separatorChar + fileName + ".csv";
        }
        return null;
    }

    // 文本写入指定路径
    public static void saveText(String path, String context){
        BufferedWriter outputStream = null;
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }else {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 写入
        try {
            outputStream = new BufferedWriter(new FileWriter(path));
            outputStream.write(context);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
