package com.xingfeng.FingerPrintLib.asm;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

public class ActivityTimeManger {
    public static HashMap<String, Long> startTimeMap = new HashMap<>();

    public static void onCreateStart(Activity activity) {
        startTimeMap.put(activity.toString(), System.currentTimeMillis());
    }

    public static void onCreateEnd(Activity activity) {
        Long startTime = startTimeMap.get(activity.toString());
        if (startTime == null) {
            return;
        }
        long coastTime = System.currentTimeMillis() - startTime;
        Log.i("DEBUG_TEST",activity.toString() + " onCreate coast Time:" + coastTime);
        startTimeMap.remove(activity.toString());
    }
}
