package com.administrator.learndemo.download;

import okhttp3.Call;

/**
 * Created by lythonliu on 2017/10/20.
 */

public interface OnDownloadListener {

    void onBeforeLoad(long total);

    /**
     * 下载成功
     */
    void onDownloadSuccess();

    /**
     * @param progress
     * 下载进度
     */
    void onDownloading(int progress, Call call);

    /**
     * 下载失败
     */
    void onDownloadFailed();

    /**
     * 下载取消
     */
    void onDownloadCancel();
}
