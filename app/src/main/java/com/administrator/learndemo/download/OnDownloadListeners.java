package com.administrator.learndemo.download;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lythonliu on 2017/10/20.
 */

public class OnDownloadListeners {
    private static HashMap<String,OnDownloadListener> map = new HashMap<>();

    public static Map getMap() {
        if (map ==null) map = new HashMap<>();
        return map;
    }

    public static OnDownloadListener getListener(String nameFromUrl) {
        return map.get(nameFromUrl);
    }

    /*private OnDownloadListener listener = new OnDownloadListener() {
        @Override
        public void onBeforeLoad(long total) {

        }

        @Override
        public void onDownloadSuccess() {

        }

        @Override
        public void onDownloading(final int progress) {

        }

        @Override
        public void onDownloadFailed() {

        }
    };*/
}
