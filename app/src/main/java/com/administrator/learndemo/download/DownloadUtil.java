package com.administrator.learndemo.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cvim on 17/10/11.
 * okhttp下载
 */

public class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param downUrl      下载连接
     * @param localDownDir 储存下载文件的SDCard目录
     */
    public Call download(final String downUrl, final String localDownDir, final String tmpFileName, final OnDownloadListener onDownloadListener) {
        Request request;
        try {
            request = new Request.Builder().url(downUrl).build();
        } catch (Exception e) {
            // 下载失败
            OnDownloadListeners.getListener(tmpFileName).onDownloadFailed();
            return null;
        }

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                OnDownloadListeners.getListener(tmpFileName).onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(localDownDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    if (total != 0) {
                        OnDownloadListeners.getListener(tmpFileName).onBeforeLoad(total);
                    }
                    File file = new File(savePath, tmpFileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        OnDownloadListeners.getListener(tmpFileName).onDownloading(progress, call);
                    }
                    fos.flush();
                    // 下载完成
                    OnDownloadListeners.getListener(tmpFileName).onDownloadSuccess();
                } catch (Exception e) {
                    if (e.toString().contains("closed")) {
                        //如果是主动取消的情况下
                        OnDownloadListeners.getListener(tmpFileName).onDownloadCancel();
                    } else {
                        OnDownloadListeners.getListener(tmpFileName).onDownloadFailed();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        OnDownloadListeners.getMap().put(tmpFileName, onDownloadListener);
        return call;
    }

    /**
     * @param url     下载连接
     * @param saveDir 储存下载文件的SDCard目录
     */
    public Call download2(final String url, final String saveDir, final String fileName, final OnDownloadListener listener) {
        Request request = null;
        Call call = null;
        try {
            request = new Request.Builder().url(url).build();
            call = okHttpClient.newCall(request);
        } catch (Exception e) {
            listener.onDownloadFailed();
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    if (call != null && total != 0) {
                        listener.onBeforeLoad(total);
                    }
                    File file = new File(savePath, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress, call);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        return call;
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

  /*  public interface OnDownloadListener {

        void onBeforeLoad(long total);
        *//**
     * 下载成功
     *//*
        void onDownloadSuccess();

        *//**
     * @param progress
     * 下载进度
     *//*
        void onDownloading(int progress);

        *//**
     * 下载失败
     *//*
        void onDownloadFailed();

    }*/

    /**
     * 插入到相册
     *
     * @param fileName 在相册中的名称(并不是文件名，更类似于title)
     */
    public static void insertIntoAlbum(final String fileName, String filePath, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String filePath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
                    File dir = new File(filePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    String tempDirStr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "cvim";

                    File tempDir = new File(tempDirStr);
                    if (!tempDir.exists()) {
                        tempDir.mkdirs();
                    }
                    File file1 = new File(filePath, fileName);
                    File file2 = new File(tempDir, fileName);
                    try {
                        InputStream fosfrom = new FileInputStream(file1);
                        OutputStream fosto = new FileOutputStream(file2);
                        byte bt[] = new byte[1024];
                        int c;
                        while ((c = fosfrom.read(bt)) > 0) {
                            fosto.write(bt, 0, c);
                        }
                        fosfrom.close();
                        fosto.close();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(file2); //out is your output file
                        mediaScanIntent.setData(contentUri);
                        context.sendBroadcast(mediaScanIntent);
                    } else {
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                    }

                } catch (Exception e) {
                    Log.i("ImageDownLoadActivity", "insertIntoAlbum e: " + e);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}