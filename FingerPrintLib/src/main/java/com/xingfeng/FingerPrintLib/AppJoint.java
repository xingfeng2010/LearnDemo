package com.xingfeng.FingerPrintLib;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import com.xingfeng.FingerPrintLib.utils.BinaryKeyMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhounl on 2017/11/15.
 */

public class AppJoint {

    private List<Application> moduleApplications = new ArrayList<>();

    private BinaryKeyMap<Class, String, Class> routersMap = new BinaryKeyMap<>();

    public static final String DEFAULT_NAME = "__app_joint_default";

    private AppJoint() { }

    public void attachBaseContext(Context context) {
        for (Application app : moduleApplications) {
            try {
                // invoke each application's attachBaseContext
                Method attachBaseContext = ContextWrapper.class.getDeclaredMethod("attachBaseContext", Context.class);
                attachBaseContext.setAccessible(true);
                attachBaseContext.invoke(app, context);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreate() {
        for (Application app : moduleApplications) {
            app.onCreate();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        for (Application app : moduleApplications) {
            app.onConfigurationChanged(configuration);
        }
    }

    public void onLowMemory() {
        for (Application app : moduleApplications) {
            app.onLowMemory();
        }
    }

    public void onTerminate() {
        for (Application app : moduleApplications) {
            app.onTerminate();
        }

    }

    public void onTrimMemory(int level) {
        for (Application app : moduleApplications) {
            app.onTrimMemory(level);
        }
    }

    public List<Application> moduleApplications() {
        return moduleApplications;
    }

    public BinaryKeyMap<Class, String, Class> routersMap() {
        return routersMap;
    }

    public static AppJoint get() {
        return SingletonHolder.INSTANCE;
    }

    static class SingletonHolder {
        static AppJoint INSTANCE = new AppJoint();
    }
}