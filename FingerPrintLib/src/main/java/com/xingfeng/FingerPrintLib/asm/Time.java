package com.xingfeng.FingerPrintLib.asm;

import android.util.Log;

public class Time {
    public void myCount() {
        int i = 5;
        int j = 10;
        Log.i("DEBUG_TEST","myCount call:" + (j - i));
    }

    public void myDeal() {
        try {
            int[] myInt = {1, 2, 3, 4, 5};
            int f = myInt[10];
            Log.i("DEBUG_TEST","myDeal call:" + f);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
