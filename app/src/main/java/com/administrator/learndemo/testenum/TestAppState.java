package com.administrator.learndemo.testenum;

import android.support.annotation.IntDef;

public class TestAppState {
    @AppShowState.AppState
    private int state;

    public void setAppState(@AppShowState.AppState int state) {
        this.state = state;
    }
}
