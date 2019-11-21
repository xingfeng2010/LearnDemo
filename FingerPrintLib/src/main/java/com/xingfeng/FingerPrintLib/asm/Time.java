package com.xingfeng.FingerPrintLib.asm;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Time {
    private List<AppTest> moduleApplications = new ArrayList<>();

    public void myCount() {
        int i = 5;
        int j = 10;
        Log.i("DEBUG_TEST", "moduleApplications size:" + moduleApplications.size());
    }

    public void myDeal() {
        try {
            int[] myInt = {1, 2, 3, 4, 5};
            int f = myInt[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
