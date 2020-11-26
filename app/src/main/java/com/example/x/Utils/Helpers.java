package com.example.x.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Helpers {

    public static void hideSoftKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            try{
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e){

            }
        }
    }
    public static void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            try{
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e){

            }
        }
    }

    public static String getDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                return "Senin";
            case Calendar.TUESDAY:
                return "Selasa";
            case Calendar.WEDNESDAY:
                return "Rabu";
            case Calendar.THURSDAY:
                return "Kamis";
            case Calendar.FRIDAY:
                return "Jumat";
            case Calendar.SATURDAY:
                return "Sabtu";
            case Calendar.SUNDAY:
                return "Ahad";
            default: return "";
        }
    }

    public static String getDayFromDate(String sdate){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(sdate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            switch (day) {
                case Calendar.MONDAY:
                    return "Senin";
                case Calendar.TUESDAY:
                    return "Selasa";
                case Calendar.WEDNESDAY:
                    return "Rabu";
                case Calendar.THURSDAY:
                    return "Kamis";
                case Calendar.FRIDAY:
                    return "Jumat";
                case Calendar.SATURDAY:
                    return "Sabtu";
                case Calendar.SUNDAY:
                    return "Ahad";
                default: return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return sdate;
        }
    }


    // yyyy-MM-dd
    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat mdformat = new SimpleDateFormat(format);
            String strDate = mdformat.format(calendar.getTime());
            return strDate;
        } catch (Exception e){
            return null;
        }
    }

    public static String getSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {}
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("EEEEEERRRRRROOOOOOORRRR", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }

        assert dir != null;
        return dir.delete();
    }

    // https://stackoverflow.com/questions/11753000/how-to-open-the-google-play-store-directly-from-my-android-application
    public static void openAppRating(Context context) {
        // you can also use BuildConfig.APPLICATION_ID
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }

    public static String nomorBulanToNamaBulan(String nomorBulan){
        String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "Novmeber", "Desember"};
        try{
            return namaBulan[Integer.parseInt(nomorBulan)-1];
        } catch (Exception e){
            return nomorBulan;
        }
    }
    public static String formatDateToReadable(String date){
        String[] temp = date.split("-");
        if(temp.length==3){
            if(temp[0].length()==4){
                return temp[2]+" "+nomorBulanToNamaBulan(temp[1])+" "+temp[0];
            } else return date;
        } else return date;
    }
    public static String formatDate(String date){
        String[] temp = date.split("-");
        if(temp.length==3){
            if(temp[0].length()==4){
                int currDay = Integer.valueOf(temp[2]);
                String day = String.valueOf(currDay);
                if (currDay < 10) day = "0" + currDay;
                int currMonth = Integer.valueOf(temp[1]);
                String month = String.valueOf(currMonth);
                if (currMonth < 10) month = "0" + currMonth;
                int currYear = Integer.valueOf(temp[0]);
                return day + "-" + month + "-" + currYear;
            } else return date;
        } else return date;
    }
    public static String formatTime(String time){
        String[] temp = time.split(":");
        if(temp.length==3){

            int iHour = Integer.valueOf(temp[2]);
            String sHour = String.valueOf(iHour);
            if (iHour < 10) sHour = "0" + iHour;

            int iMin = Integer.valueOf(temp[1]);
            String sMin = String.valueOf(iMin);
            if (iMin < 10) sMin = "0" + iMin;

            int iSec = Integer.valueOf(temp[0]);
            String sSec = String.valueOf(iSec);
            if (iSec < 10) sSec = "0" + iSec;

            return sHour + ":" + sMin + ":" + sSec;

        } else if(temp.length==2) {

            int iHour = Integer.valueOf(temp[0]);
            String sHour = String.valueOf(iHour);
            if (iHour < 10) sHour = "0" + iHour;

            int iMin = Integer.valueOf(temp[1]);
            String sMin = String.valueOf(iMin);
            if (iMin < 10) sMin = "0" + iMin;

            return sHour + ":" + sMin + ":" + "00";

        } else return time;
    }

    public static String removeSecond(String time){
        String[] temp = time.split(":");
        if(temp.length==3){
            return temp[0]+":"+temp[1];
        } else return time;
    }

//    public static boolean checkLastActivity(Context context){
//        ActivityManager mngr = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
//        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//
//        if(taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(context.getClass().getName())) {
//            Log.e("checkLastActivity", "This is last activity in the stack");
//            Intent intent = new Intent(context, ActivityMain2.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            context.startActivity(intent);
//            return true;
//        } else {
//            Log.e("checkLastActivity", "Not the last");
//            return false;
//        }
//    }

}
