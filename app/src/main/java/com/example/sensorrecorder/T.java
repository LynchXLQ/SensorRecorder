package com.example.sensorrecorder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sensorrecorder.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

/**
 * tools in common use
 *
 * @author Gu
 */
public final class T {
    private T() {
    }

    // -------------------------------------------------------------------------------------
    // density

    // -------------------------------------------------------------------------------------
    // show
    private static Toast toast = null;

    /**
     * DIP???PX
     *
     * @param context context
     * @param dpValue DIP
     * @return PX
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * PX???DIP
     *
     * @param context context
     * @param pxValue PX
     * @return DIP
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // -------------------------------------------------------------------------------------
    // version

    /**
     * ?????????????????????
     *
     * @param context context
     * @return status_bar_height
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * ???????????????????????????:api_level<19?????????0
     *
     * @param c
     * @return
     */
    public static int getFitStatusBarHeight(Context c) {
        if (Build.VERSION.SDK_INT < 19)
            return 0;
        return getStatusBarHeight(c);
    }

    /**
     * ??????PackageInfo
     *
     * @param context     context
     * @param packageName packageName
     * @return PackageInfo or null
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            return manager.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ??????VersionName
     *
     * @param context            context
     * @param packageName        packageName
     * @param defaultVersionName defaultVersionName
     * @return versionName or defaultVersionName
     * @see T#getPackageInfo(Context, String)
     */
    public static String getVersionName(Context context, String packageName,
                                        String defaultVersionName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return defaultVersionName;
    }

    /**
     * @see T#getVersionName(Context, String, String)
     */
    public static String getVersionName(Context context, String defaultVersionName) {
        return getVersionName(context, context.getPackageName(),
                defaultVersionName);
    }

    /**
     * ??????VersionCode
     *
     * @param context            context
     * @param packageName        packageName
     * @param defaultVersionCode defaultVersionCode
     * @return versionCode or defaultVersionCode
     * @see T#getPackageInfo(Context, String)
     */
    public static int getVersionCode(Context context, String packageName,
                                     int defaultVersionCode) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return defaultVersionCode;
    }

    // -------------------------------------------------------------------------------------
    // data and time

    /**
     * @see T#getVersionCode(Context, String, int)
     */
    public static int getVersionCode(Context context, int defaultVersionCode) {
        return getVersionCode(context, context.getPackageName(),
                defaultVersionCode);
    }

    /**
     * ??????????????????????????????
     *
     * @param calendar calendar
     * @param template the pattern, such as "yyyyMMddHHmmss"
     * @return formatTime
     */
    public static String getFormatTime(Calendar calendar, String template) {
        if (calendar == null || template == null)
            throw new NullPointerException("template must not null");
        return new SimpleDateFormat(template, Locale.getDefault())
                .format(calendar.getTime());
    }

    /**
     * @see T#getFormatTime(Calendar, String)
     */
    public static String getFormatTime(String template) {
        return getFormatTime(Calendar.getInstance(), template);
    }

    /**
     * @see T#getCalendar(String)
     * @see T#getFormatTime(Calendar, String)
     */
    public static String getFormatTime(String sourceStr, String template) {
        Calendar calendar = getCalendar(sourceStr);
        if (calendar != null) {
            return getFormatTime(calendar, template);
        }
        return sourceStr;
    }

    /**
     * ?????????????????????N????????????????????????????????????
     *
     * @param data_str
     * @return ????????????[???, ???, ?????????]
     */
    public static int[] getRemainTime(String data_str, int hours) {
        return getRemainTimeArr(getRemainSecond(data_str, hours));
    }

    /**
     * ?????????????????????N????????????????????????????????????
     *
     * @param data_str
     * @return ?????? ???
     */
    public static long getRemainSecond(String data_str, int hours) {
        //?????????
        long SS = (long) ((T.getCalendar(data_str).getTimeInMillis() + 1000d * 60 * 60 * hours - new Date().getTime()) / 1000d);
        return SS;
    }

    /**
     * ?????????????????? ??????[???, ???, ?????????]
     *
     * @param SS ????????????
     * @return ????????????[???, ???, ?????????]
     */
    public static int[] getRemainTimeArr(long SS) {
        SS = Math.max(0, SS);
        int day = (int) (SS / 60 / 60 / 24);
        int hour = (int) (SS / 60 / 60 % 24);
        int minute = (int) (SS / 60 % 60);
        int second = (int) (SS % 60);
        return new int[]{day, hour, minute, second};
    }


    /**
     * ??????Calendar
     *
     * @param sourceStr the sourceStr, such as "20151024102424",the length must is 14
     * @return calendar or null
     */
    public static Calendar getCalendar(String sourceStr) {
        if (sourceStr != null) {
            sourceStr = getNumberSTR(sourceStr);
            if (sourceStr.length() == 8) {
                sourceStr += "0000";
            }
            if (sourceStr.length() >= 12) {
                try {
                    int year = Integer.parseInt(sourceStr.substring(0, 4));
                    int month = Integer.parseInt(sourceStr.substring(4, 6)) - 1;
                    int day = Integer.parseInt(sourceStr.substring(6, 8));
                    int hourOfDay = Integer.parseInt(sourceStr.substring(8, 10));
                    int minute = Integer.parseInt(sourceStr.substring(10, 12));
                    int second = sourceStr.length() >= 14 ? Integer.parseInt(sourceStr.substring(12, 14)) : 0;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hourOfDay, minute, second);
                    return calendar;
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    /**
     * ??????Calendar
     *
     * @param timeInMills
     * @return calendar
     */
    public static Calendar getCalendar(long timeInMills) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMills);
        return cal;
    }

    /**
     * ???????????????????????????
     *
     * @param str
     * @return
     */
    public static String getNumberSTR(String str) {
        if (T.isEmpty(str))
            return "";
        return str.replaceAll("[^0-9]", "");
    }

    /**
     * 3???????????????????????????
     *
     * @param timeInSecond
     * @param template
     * @return
     */
    public static String get3FormatTimeA(long timeInSecond, String template) {
        Calendar nowCal = Calendar.getInstance();
        long nowTimeInSecond = nowCal.getTimeInMillis() / 1000l;
        long passTimeInSecond = nowTimeInSecond - timeInSecond;
        if (passTimeInSecond < 0)
            passTimeInSecond = 0;
        if (passTimeInSecond < 259200)//3???
        {
            if (passTimeInSecond < 60)//1??????
                return passTimeInSecond + "??????";
            else if (passTimeInSecond < 3600)//?????????
                return passTimeInSecond / 60l + "?????????";
            else if (passTimeInSecond < 86400)//??????
                return passTimeInSecond / 3600l + "?????????";
            else
                return passTimeInSecond / 86400l + "??????";
        } else {
            nowCal.setTimeInMillis(timeInSecond * 1000l);
            return getFormatTime(nowCal, template);
        }
    }

    /**
     * 3???????????????????????????
     *
     * @param timeInSecond
     * @param template1
     * @param template2
     * @return
     */
    public static String get3FormatTimeB(long timeInSecond, String template1, String template2) {
        Calendar todayCal = Calendar.getInstance();//??????0???
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        Calendar currentCal = Calendar.getInstance();//??????0???
        currentCal.setTimeInMillis(timeInSecond * 1000L);
        currentCal.set(Calendar.HOUR_OF_DAY, 0);
        currentCal.set(Calendar.MINUTE, 0);
        currentCal.set(Calendar.SECOND, 0);

        long passTimeInMills = todayCal.getTimeInMillis() - currentCal.getTimeInMillis();

        currentCal.setTimeInMillis(timeInSecond * 1000L);

        if (passTimeInMills >= 0 && passTimeInMills <= 259200000)//3???
        {
            String sub = null;
            if (passTimeInMills < 86400000)
                sub = "??????";
            else if (passTimeInMills < 172800000)
                sub = "??????";
            else
                sub = "??????";
            return template1 == null ? sub + "" : sub + getFormatTime(currentCal, template1);
        } else {
            return getFormatTime(currentCal, template2);
        }
    }


    /**
     * ??????Week
     *
     * @param calendar calendar
     * @return 1 ~ 7
     */
    public static int getWeekInt(Calendar calendar) {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0 ? 7 : calendar
                .get(Calendar.DAY_OF_WEEK) - 1;
    }

    // -------------------------------------------------------------------------------------
    // value

    /**
     * ??????Week
     *
     * @param calendar calendar
     * @return ???, ???, ???, ???, ???, ???, ???
     * @see T#getWeekInt(Calendar)
     */
    public static String getWeekString(Calendar calendar) {
        int now_weekI = getWeekInt(calendar);
        switch (now_weekI) {
            case 1:
                return "???";
            case 2:
                return "???";
            case 3:
                return "???";
            case 4:
                return "???";
            case 5:
                return "???";
            case 6:
                return "???";
            default:
                return "???";
        }
    }

    /**
     * url2fileName
     *
     * @param url
     * @return url????????????????????????fileName
     */
    public static String getFileName(String url) {
        if (isEmpty(url)) {
            return url;
        }
        String fileName = new String(url);
        if (fileName.contains("?"))
            fileName = fileName.substring(0, fileName.indexOf('?'));
        fileName = fileName.replaceAll("/", "");
        fileName = fileName.replaceAll(":", "");
        fileName = fileName.replaceAll("\\\\", "");
        fileName = fileName.replaceAll("\\*", "");
        fileName = fileName.replaceAll("\\?", "");
        fileName = fileName.replaceAll("\\<", "");
        fileName = fileName.replaceAll("\\>", "");
        fileName = fileName.replaceAll("\\|", "");
        if (fileName.length() > 200) {
            return fileName.substring(fileName.length() - 200);
        } else {
            return fileName;
        }
    }

    /**
     * first substring
     *
     * @param uri
     * @param indexString
     * @return uri?????????????????????????????????
     */
    public static String getFirstSubString(String uri, String indexString) {
        if (isEmpty(uri) || isEmpty(indexString)) {
            return uri;
        }
        int index = uri.indexOf(indexString);
        if (index + indexString.length() > uri.length())
            return uri;
        return uri.substring(index + indexString.length());
    }

    /**
     * last substring
     *
     * @param uri
     * @param indexString
     * @return uri????????????????????????????????????
     */
    public static String getLastSubString(String uri, String indexString) {
        if (isEmpty(uri) || isEmpty(indexString)) {
            return uri;
        }
        int index = uri.lastIndexOf(indexString);
        if (index + indexString.length() > uri.length())
            return uri;
        return uri.substring(index + indexString.length());
    }

    /**
     * uri2simple_fileName
     *
     * @param uri
     * @return uri??????????????????????????????fileName
     */
    public static String getSimpleFileName(String uri) {
        return getLastSubString(uri, "/");

    }

    /**
     * uri2simple_fileName
     *
     * @param uri
     * @return uri??????????????????????????????fileName
     */
    public static String getFileDocType(String uri) {
        if (isEmpty(uri)) {
            return "";
        }
        int index = uri.lastIndexOf(".");
        if (index + 1 > uri.length())
            return "";
        return uri.substring(index + 1);

    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * Returns true if the list is null or 0-length.
     *
     * @param list the list to be examined
     * @return true if list is null or zero length
     */
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    /**
     * Returns true if a==b.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isEquals(String a, String b) {
        if (a == null && b == null)
            return true;
        if (a != null && b != null) {
            return a.equals(b);
        }
        return false;
    }

    /**
     * ??????????????????
     *
     * @param value value
     * @param rules rules ??????(???:0.00??????2?????????)
     * @return string
     */
    public static String getTrim(double value, String rules) {
        DecimalFormat df = new DecimalFormat(rules);
        return df.format(value);
    }

    /**
     * ??????????????????
     *
     * @param value value
     * @param rules ??????(???:0.00??????2?????????)
     * @return string or "" or value
     * @see T#getTrim(double, String)
     */
    public static String getTrim(String value, String rules) {
        if (value == null || value.length() == 0 || rules == null
                || rules.length() == 0) {
            return "";
        }
        try {
            return getTrim(Double.parseDouble(value), rules);
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * ??????hashMap??????key????????????????????????String?????????value???("","null","-"???????????????)
     *
     * @param hashMap hashMap
     * @param key     key
     * @return true or false
     */
    public static boolean hasSTRValue(HashMap<String, Object> hashMap, String key) {
        if (hashMap != null && hashMap.get(key) != null) {
            String value = hashMap.get(key).toString();
            if (!value.equals("") && !value.equals("null")
                    && !value.equals("-")) {
                return true;
            }
        }
        return false;
    }

    /**
     * ??????hashMap??????key???String??????value???
     *
     * @param hashMap hashMap
     * @param key     key
     * @return value or defaultStringValue
     * @see T#hasSTRValue(HashMap, String)
     */
    public static String getSTRValue(HashMap<String, Object> hashMap, String key,
                                     String defaultStringValue) {
        if (hasSTRValue(hashMap, key)) {
            return hashMap.get(key).toString();
        }
        return defaultStringValue;
    }

    /**
     * ??????????????????
     *
     * @param inList inList
     * @return outList ??????????????????
     */
    public static <E> ArrayList<E> getReverseList(List<E> inList) {
        if (inList == null || inList.size() == 0) {
            return new ArrayList<E>(inList);
        }
        ArrayList<E> outList = new ArrayList<E>();
        int size = inList.size();
        for (int i = size - 1; i >= 0; i--) {
            outList.add(inList.get(i));
        }
        return outList;
    }

    /**
     * ????????????
     *
     * @param context        context
     * @param msg            message
     * @param isShowLongTime isShowLongTime
     */
    public static void showToast(Context context, String msg, boolean isShowLongTime) {
        if (context != null && msg != null && !msg.equals("")) {
            if (toast == null) {
                LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflate.inflate(R.layout.transient_notification, null);
                TextView tv = (TextView) view.findViewById(android.R.id.message);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(0xA6000000);
                gradientDrawable.setCornerRadius(dip2px(context, 5f));
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setBackground(gradientDrawable);
                } else {
                    view.setBackgroundDrawable(gradientDrawable);
                }
                toast = new Toast(context);
                toast.setView(view);
                toast.setGravity(Gravity.CENTER, 0, 0);
            }
            if (isShowLongTime) {
                toast.setDuration(Toast.LENGTH_LONG);
            } else {
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            if (tv != null) {
                tv.setText(msg);
                toast.show();
            }
        }
    }

    /**
     * ????????????(???)
     *
     * @param context context
     * @param msg     message
     * @see T#showToast(Context, String, boolean)
     */
    public static void showToast(Context context, String msg) {
        showToast(context, msg, false);
    }

    /**
     * ???????????????
     *
     * @param view view
     */
    public static void showSoftInput(EditText view) {
        if (view != null) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            InputMethodManager inputmanger = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.showSoftInput(view, 0);
        }
    }

    /**
     * ???????????????
     *
     * @param view view
     */
    public static void hideSoftInput(View view) {
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    // -------------------------------------------------------------------------------------
    // file

    /**
     * ???????????????SD?????????????????????
     *
     * @param context
     * @return
     */
    @SuppressWarnings("all")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static List<String> getValidSDCardPathList(Context context) {
        List<String> pathList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            try {
                StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                String[] paths = (String[]) sm.getClass().getMethod("getVolumePaths").invoke(sm);
                for (int i = 0; i < paths.length; i++) {
                    String status = (String) sm.getClass().getMethod("getVolumeState", String.class).invoke(sm, paths[i]);
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        pathList.add(paths[i]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pathList;
    }

    /**
     * ??????????????????SD???
     *
     * @return true or false
     */
    public static boolean isSdCardOK() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * ?????????
     *
     * @param inputStream   inputStream
     * @param filePathDir   filePathDir
     * @param fileName      fileName
     * @param isRewriteFile isRewriteFile?????????????????????
     * @return true or false
     */
    public static boolean writeFile(InputStream inputStream, String filePathDir, String fileName, boolean isRewriteFile) {
        if (inputStream == null || filePathDir == null || fileName == null) {
            throw new NullPointerException();
        }
        try {
            File fileDir = new File(filePathDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String filePath = filePathDir + "/" + fileName;
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                if (isRewriteFile) {
                    file.delete();
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            filePath);
                    byte[] buffer = new byte[1024];
                    int count = 0;
                    while ((count = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    return true;
                }
            } else {
                FileOutputStream fileOutputStream = new FileOutputStream(
                        filePath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, count);
                }
                fileOutputStream.close();
                inputStream.close();
                return true;
            }
            inputStream.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ??????APP????????????
     */
    public static File getCacheDir(Context context) {
        File appFilesDir = getExternalDir(context, "cache");
        if (appFilesDir == null) {
            appFilesDir = context.getCacheDir();
        }
        if (appFilesDir != null && !appFilesDir.exists()) {
            appFilesDir.mkdirs();
        }
        return appFilesDir;
    }

    /**
     * ??????APP??????????????????
     *
     * @return file
     */
    public static File getFilesDir(Context context) {
        File appFilesDir = getExternalDir(context, "files");
        if (appFilesDir == null) {
            appFilesDir = context.getFilesDir();
        }
        if (appFilesDir != null && !appFilesDir.exists()) {
            appFilesDir.mkdirs();
        }
        return appFilesDir;
    }

    /**
     * ??????APP???????????????api_level<23??????????????????
     *
     * @param context
     * @return dirName
     */
    public static File getExternalDir(Context context, String dirName) {
        File appFilesDir = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (isSdCardOK()) {
                appFilesDir = getExternalFilesDir(context, dirName);
            }
            if (appFilesDir == null) {
                List<String> pathList = getValidSDCardPathList(context);
                if (pathList.size() != 0) {
                    appFilesDir = new File(pathList.get(0) + "/" + dirName);
                }
            }
        }
        return appFilesDir;
    }

    /**
     * ????????????APP??????????????????
     *
     * @param context
     * @return file
     */
    public static File getExternalFilesDir(Context context, String childDirName) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appFilesDir = new File(new File(dataDir, context.getPackageName()), childDirName);
        if (!appFilesDir.exists()) {
            if (!appFilesDir.mkdirs()) {
                return null;
            }
            try {
                new File(appFilesDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return appFilesDir;
    }


    /**
     * ??????String???????????????
     *
     * @param content
     * @param filePath
     */
    public static void saveAsFileWriter(String content, String filePath) {

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(filePath);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ???????????????APK???????????????
     *
     * @param context     context
     * @param apkFilePath apkFilePath
     * @return true or false(APK?????????????????????false)
     */
    public static boolean installAPK(Context context, String apkFilePath) {
        String cmd = "chmod 755 " + apkFilePath; /* 755 ????????????apk????????????????????????????????? ????????????????????????????????????????????? */
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(apkFilePath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * ?????????????????????
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


    /**
     * ????????????????????????
     *
     * @param context
     */
    public static void openAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * ??????????????????
     *
     * @param context
     */
    public static void openTel(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * ??????????????????DEBUG??????
     *
     * @param context
     * @return
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * inputStream 2 string
     *
     * @param inputStream inputStream
     * @param charsetName charsetName,default is "UTF-8"
     * @return string
     */
    public static String getStringFromInputStream(InputStream inputStream, String charsetName) {
        if (inputStream == null) {
            throw new NullPointerException();
        }
        if (charsetName == null) {
            charsetName = "UTF-8";
        }
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, charsetName);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * ??????????????????
     *
     * @param ex
     * @return
     */
    public static String getStringFromThrowable(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }


    /**
     * inputStream 2 byte[]
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static byte[] getByteArrFromInputStream(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (in != null) {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            return out.toByteArray();
        }
        return null;
    }

    /**
     * ???????????????BASE64?????????
     *
     * @param filePath
     * @return
     */
    public static String getBase64StrFromFile(String filePath) {
        try {
            FileInputStream in = new FileInputStream(filePath);
            byte buffer[] = getByteArrFromInputStream(in);
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * bitmap??????base64
     *
     * @param bitmap
     * @return
     */
    public static String getBase64StrFromBitmap(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param file
     */
    public static void mediaScanFile(Context context, File file) {
        if (context != null)
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    // -------------------------------------------------------------------------------------
    // activity and service

    /**
     * ??????activityManager
     *
     * @param context context
     * @return activityManager
     */
    public static ActivityManager getActivityManager(Context context) {
        if (context == null) {
            throw new NullPointerException("context can not be empty");
        }
        return (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * Whether the service is running.
     *
     * @param context       context
     * @param className     className
     * @param maxServiceNum maxServiceNum?????????????????????????????????
     * @return true or false
     */
    public static boolean isServiceRunning(Context context, String className,
                                           int maxServiceNum) {
        List<RunningServiceInfo> runningServiceInfos = getActivityManager(
                context).getRunningServices(maxServiceNum);
        for (RunningServiceInfo runningServiceInfo : runningServiceInfos) {
            if (runningServiceInfo.service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the activity is running.
     *
     * @param context    context
     * @param className  className
     * @param maxTaskNum maxTaskNum???????????????????????????????????????
     * @return true or false
     * @deprecated see {@link ActivityManager#getRunningTasks(int)}
     */
    public static boolean isActivityRunning(Context context, ComponentName className,
                                            int maxTaskNum) {
        List<RunningTaskInfo> runningTaskInfos = getActivityManager(context)
                .getRunningTasks(maxTaskNum);
        for (RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            if (runningTaskInfo.topActivity.equals(className)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------------------
    // text

    /**
     * Whether is true name.
     *
     * @param name name
     * @return true or false
     */
    public static boolean isTrueName(String name) {
        if (!isEmpty(name)) {
            String matcher = "^[a-zA-Z_][a-zA-Z0-9_]{4,19}$";
            if (name.matches(matcher)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether is true nickname.
     *
     * @param name name
     * @return true or false
     */
    public static boolean isTrueNickName(String name) {
        if (!isEmpty(name)) {
            //String matcher = "^[\u4e00-\u9fa5a-zA-Z0-9~!@#$%^&*()_+.~-]{2,10}$";
            String matcher = "^[_A-Za-z0-9\u4e00-\u9fa5]{2,10}$";
            if (name.matches(matcher)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether is true phoneNumber.
     *
     * @param phoneNumber phoneNumber
     * @return true or false
     */
    public static boolean isTrueNumber(String phoneNumber) {
        if (!isEmpty(phoneNumber)) {
            //String matcher = "0\\d{2,3}-\\d{7,8}|0\\d{2,3}\\s?\\d{7,8}|13[0-9]\\d{8}|18[0-9]\\d{8}|14[0-9]\\d{8}|15[0-9]\\d{8}";
            String matcher = "^[1][34578][0-9]{9}$";
            if (!phoneNumber.matches(matcher)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Whether is true password.
     *
     * @param password password
     * @return true or false
     */
    public static boolean isTruePassword(String password) {
        if (!isEmpty(password)) {
            String matcher = "^\\w[a-zA-Z0-9~!@#$%^&*()_+.~-]{5,15}$";
            if (password.matches(matcher)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether is true phoneNumber.
     *
     * @param emailAddress phoneNumber
     * @return true or false
     */
    public static boolean isTrueEmail(String emailAddress) {
        if (!isEmpty(emailAddress)) {
            String matcher = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
            if (!emailAddress.matches(matcher)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Whether is true url.
     *
     * @param url url
     * @return true or false
     */
    public static boolean isTrueUrl(String url) {
        if (!isEmpty(url)) {
            String matcher = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
            if (!url.matches(matcher)) {
                return false;
            }
        }
        return true;
    }

    // -------------------------------------------------------------------------------------
    // bitmap

    /**
     * ???view?????????bitmap
     *
     * @param view
     * @return bitmap
     */
    public static Bitmap convertView2Bitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        //??????bitmap????????????
        Canvas canvas = new Canvas(bitmap);
        //???view??????????????????????????????
        view.draw(canvas);
        return bitmap;
//        view.destroyDrawingCache();
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.setDrawingCacheEnabled(true);
//        Bitmap bitmap = view.getDrawingCache(true);
//        return bitmap;
    }

    /**
     * save bitmap
     *
     * @param bitmap   bitmap
     * @param filePath filePath
     * @param quality  quality??????????????????
     * @param format   format??????????????????
     * @see Bitmap#compress(CompressFormat, int, java.io.OutputStream)
     */
    public static void saveBitmap(Bitmap bitmap, String filePath, int quality, CompressFormat format) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(format, quality, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * save bitmap default ( quality = 100,format = JPEG )
     *
     * @param bitmap   bitmap
     * @param filePath filePath
     * @see T#saveBitmap(Bitmap, String, int, CompressFormat)
     */
    public static void saveBitmapDefault(Bitmap bitmap, String filePath) {
        saveBitmap(bitmap, filePath, 100, CompressFormat.JPEG);
    }

    /**
     * ????????????????????????
     *
     * @param options   options
     * @param reqWidth  reqWidth
     * @param reqHeight reqHeight
     * @return inSampleSize
     */
    public static int getBitmapInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * ?????????????????????????????????
     *
     * @param filePath filePath
     * @param targetW  targetW
     * @param targetH  targetH
     * @return bitmap
     */
    public static Bitmap getBtimapFromFile(String filePath, int targetW, int targetH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = getBitmapInSampleSize(options, targetW, targetH);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * ????????????id????????????
     *
     * @param context context
     * @param id      id
     * @param targetW targetW
     * @param targetH targetH
     * @return bitmap
     */
    public static Bitmap getBitmapFromResource(Context context, int id, int targetW, int targetH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id, options);
        // Calculate inSampleSize
        options.inSampleSize = getBitmapInSampleSize(options, targetW, targetH);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

    /**
     * ??????????????????????????????
     *
     * @param filePath
     * @return
     */
    public static int getBitmapFileDigree(String filePath) {
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // ?????????????????????????????????
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // ??????????????????
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        return digree;
    }

    public static Bitmap getRotateBitmap(Bitmap bitmap, int digree) {
        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            Matrix m = new Matrix();
            m.postRotate(digree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        } else return null;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param bitmap
     * @param w      width
     * @param h      height
     * @return bitmap
     */
    public static Bitmap scaleBitmapWH(Bitmap bitmap, float w, float h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleH = h / (float) height;
        float scaleW = w / (float) width;
        float scale = Math.min(scaleH, scaleW);
        if (scale >= 1)
            return bitmap;
        return scaleBitmap(bitmap, scale);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param bitmap
     * @param w      width
     * @return bitmap
     */
    public static Bitmap scaleBitmapW(Bitmap bitmap, float w) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        float scale = w / (float) width;
        return scaleBitmap(bitmap, scale);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param bitmap bitmap
     * @param h      height
     * @return bitmap
     */
    public static Bitmap scaleBitmapH(Bitmap bitmap, float h) {
        if (bitmap == null) {
            return null;
        }
        int height = bitmap.getHeight();
        float scale = h / (float) height;
        return scaleBitmap(bitmap, scale);
    }

    /**
     * ?????????????????????
     *
     * @param bitmap bitmap
     * @param scale  scale
     * @return bitmap
     */
    public static synchronized Bitmap scaleBitmap(Bitmap bitmap, float scale) {
        if (bitmap == null || bitmap.getHeight() < 0 || bitmap.getWidth() < 0) {
            return null;
        }
        if (scale == 0)
            return bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    /**
     * ?????????????????????
     *
     * @param bitmap bitmap
     * @return bitmap
     */
    public static synchronized Bitmap cropBitmapCenter(Bitmap bitmap, float rate_w, float rate_h) {
        if (bitmap == null || bitmap.getHeight() < 0 || bitmap.getWidth() < 0) {
            return null;
        }
        if (!(rate_w > 0 && rate_w <= 1 && rate_h > 0 && rate_h <= 1))
            return bitmap;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        float dx = width * (1 - rate_w) * 0.5f;
        float dy = height * (1 - rate_h) * 0.5f;

        bitmap = Bitmap.createBitmap(bitmap, Math.round(dx), Math.round(dy), (int) (width * rate_w), (int) (height * rate_h));
        return bitmap;
    }

    /**
     * ?????????????????????
     *
     * @param bitmap bitmap
     * @return bitmap
     */
    public static synchronized Bitmap cropBitmapBottom(Bitmap bitmap, float rate_w, float rate_h) {
        if (bitmap == null || bitmap.getHeight() < 0 || bitmap.getWidth() < 0) {
            return null;
        }
        if (!(rate_w > 0 && rate_w <= 1 && rate_h > 0 && rate_h <= 1))
            return bitmap;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        float dx = width * (1 - rate_w) * 0.5f;
        float dy = height - height * rate_h;

        bitmap = Bitmap.createBitmap(bitmap, Math.round(dx), Math.round(dy), (int) (width * rate_w), (int) (height * rate_h));
        return bitmap;
    }

    /**
     * ??????????????????????????????CenterCrop??????
     *
     * @param bitmap   bitmap
     * @param target_w
     * @param target_h
     * @return bitmap
     */
    public static synchronized Bitmap cropBitmap(Bitmap bitmap, int target_w, int target_h) {
        if (bitmap == null || bitmap.getHeight() < 0 || bitmap.getWidth() < 0) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale;
        float dx = 0, dy = 0;

        if (target_w * height > width * target_h) {
            scale = (float) target_h / (float) height;
            dx = (width - target_w / scale) * 0.5f;
        } else {
            scale = (float) target_w / (float) width;
            dy = (height - target_h / scale) * 0.5f;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        bitmap = Bitmap.createBitmap(bitmap, Math.round(dx), Math.round(dy), (int) (width - 2 * dx), (int) (height - 2 * dy), matrix, true);
        return bitmap;
    }

    /**
     * ????????????drawable
     *
     * @return gradientDrawable
     */
    public static GradientDrawable getCornerDrawable(float radius, int color, int alpha) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setAlpha(alpha);
        return gradientDrawable;
    }

    /**
     * ????????????selector
     *
     * @return stateListDrawable
     */
    public static StateListDrawable getDrawableSelector(Drawable selected, Drawable unSelect) {
        return getStateListDrawable(selected, selected, selected, unSelect);
    }

    /**
     * ????????????selector
     *
     * @return stateListDrawable
     */
    public static StateListDrawable getStateListDrawable(Drawable perssed, Drawable focused, Drawable selected, Drawable unabled) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed}, perssed);
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        bg.addState(new int[]{android.R.attr.state_selected}, selected);
        bg.addState(new int[]{}, unabled);
        return bg;
    }

    /**
     * ??????ColorStateList
     *
     * @return ColorStateList
     */
    public static ColorStateList getColorStateList(int normal, int pressed, int selected, int unable) {
        int[] colors = new int[]{pressed, pressed, selected, unable, normal};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[4] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    private static long lastPressTime = 0;

    /**
     * ????????????????????????????????????
     *
     * @param limitTime
     * @return
     */
    public static boolean isOverWhenPressAgain(long limitTime) {
        if (limitTime < 500)
            limitTime = 500;
        if ((System.currentTimeMillis() - lastPressTime) > limitTime) {
            lastPressTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    /**
     * TextView ????????????
     *
     * @param textView
     * @param defaultReturnStr
     * @return ????????????????????????????????????
     */
    public static String textViewDeleteSpace(TextView textView, String defaultReturnStr) {
        if (textView != null) {
            String strOld = textView.getText().toString();
            String strNew = strOld.replaceAll(" ", "");
            if (!strOld.equals("") && !strOld.equals(strNew))//?????????TextView?????????TextWatcher??????????????????
            {
                textView.setText(strNew);
                if (textView instanceof EditText) {
                    ((EditText) textView).setSelection(strNew.length());
                }
            }
            if (strNew.equals(""))
                return defaultReturnStr;
            return strNew;
        }
        return defaultReturnStr;
    }

    /**
     * ???view????????????view?????????
     *
     * @param rootView
     * @return
     */
    public static View getNoParentView(View rootView) {
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
            parent.removeView(rootView);
        return rootView;
    }

    /**
     * ??????????????????
     *
     * @param total     ?????????
     * @param pageSize  ??????????????????
     * @param pageIndex ????????????????????????????????????
     * @return
     */
    public static boolean hasMorePage(int total, int pageSize, int pageIndex) {
        int maxPageSize = (int) (Math.ceil((total * 1d) / (pageSize * 1d)));
        return pageIndex < maxPageSize;
    }

    /**
     * ??????T.class
     *
     * @param mClass    ??????????????????????????????
     * @param tPosition ???????????? ???????????????0
     * @return ???????????????type?????????????????????Class<T>???
     */
    public static Type getTClass(Object mClass, int tPosition) {
        Class<?> clazz = mClass.getClass();
        Type sType = clazz.getGenericSuperclass();
        while (!(sType instanceof ParameterizedType)) {
            //??????????????????????????????
            clazz = clazz.getSuperclass();
            sType = clazz.getGenericSuperclass();
        }
        ParameterizedType pType = (ParameterizedType) sType;//??????????????????????????????????????????????????????
        return pType.getActualTypeArguments()[tPosition];//???????????????
    }

    /**
     * ??????????????????TAG
     *
     * @param tag
     * @return
     */
    public static int getIntTag(int... tag) {
        if (tag == null)
            return 0;
        int value = 0;
        for (int i : tag) {
            value = value * 10 + i;
        }
        return value;
    }


    /**
     * ?????????????????????????????????
     *
     * @param propertiesFilePath
     * @param propertyKey
     * @return
     */
    public static String getProperty(String propertiesFilePath, String propertyKey) {
        String propValue = null;
        try {
            InputStream in = new FileInputStream(new File(propertiesFilePath));
            Properties prop = new Properties();
            prop.load(in);
            propValue = prop.getProperty(propertyKey);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            propValue = null;
        }
        return propValue;
    }

    /**
     * ????????????????????????????????????
     *
     * @param length ????????????????????????
     * @return
     */
    public static String generateRandomString(int length) {
        String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * ??????assets?????????????????????????????????
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getAssetsStr(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        String result = baos.toString();
        is.close();
        return result;
    }

    /**
     * ??????API19????????????fit???????????????
     *
     * @param context
     * @return
     */
    public static int getFitStatusBarHeight19(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? T.getStatusBarHeight(context) : 0;
    }

    /**
     * ??????API19??????????????????
     *
     * @param view
     */
    public static void fitSystemWindow19(View view) {
        if (view != null) {
            view.getLayoutParams().height = getFitStatusBarHeight19(view.getContext());
        }
    }


    public static void main(String[] args) throws Exception {
    }
}
