package com.administrator.learndemo.dynamic;

import android.util.Log;

public class RealPrint implements IPrint {
    @Override
    public void print(String msg) {
        Log.i("testDynamicPropxy","RealPrint called:" + msg);
    }
}
