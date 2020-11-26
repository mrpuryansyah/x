package com.example.x.Utils;

import android.util.Log;

import com.example.x.BuildConfig;
import com.example.x.R;

public class LogConsole {

    public LogConsole(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            longInfo(tag, msg);
        }
    }

    public LogConsole(String msg) {
        if(BuildConfig.DEBUG) {
            longInfo(String.valueOf(R.string.app_name), msg);
        }
    }

    private static void longInfo(String tag, String msg) {
        try {
            if(msg.length() > 4000) {
                Log.i(tag, msg.substring(0, 4000));
                longInfo(tag, msg.substring(4000));
            } else
                Log.i(tag, msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
