package com.administrator.learndemo.testenum;


public class TestAppState {
    @AppShowState.AppState
    private int state;

    public void setAppState(@AppShowState.AppState int state) {
        this.state = state;
    }
}
