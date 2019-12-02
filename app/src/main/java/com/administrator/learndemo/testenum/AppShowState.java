package com.administrator.learndemo.testenum;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AppShowState {
    public static final int STOP = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;

    @IntDef({STOP, RUNNING, PAUSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppState {
    }

    private AppShowState() {
        throw new UnsupportedOperationException("Can't init AppShowState!!!");
    }

    public static final int APPLE_FUJI = 0;
    public static final int APPLE_PIPPIN  = 1;
    public static final int APPLE_GRANY_SMITH  = 2;

    public static final int ORANGE_NAVEL = 0;
    public static final int ORANGE_TEMPLE  = 1;
    public static final int ORANGE_BLOOD  = 2;
}
