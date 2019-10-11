package com.administrator.learndemo.download;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.util.LinkedList;
import java.util.List;

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mMediaScanConn;

    private LinkedList<String[]> mLinkedList = new LinkedList<>();
    private String[] mCurrentScanPaths;
    private int mScanCount = 0;

    /**
     * Create scanner.
     *
     * @param context context.
     */
    public MediaScanner(Context context) {
        this.mMediaScanConn = new MediaScannerConnection(context.getApplicationContext(), this);
    }


    /**
     * Scanner is running.
     *
     * @return true, other wise false.
     */
    public boolean isRunning() {
        return mMediaScanConn.isConnected();
    }

    /**
     * Scan file.
     *
     * @param filePath file absolute path.
     */
    public void scan(String filePath) {
        scan(new String[]{filePath});
    }

    /**
     * Scan file list.
     *
     * @param filePaths file absolute path list.
     */
    public void scan(List<String> filePaths) {
        scan(filePaths.toArray(new String[filePaths.size()]));
    }

    /**
     * Scan file array.
     *
     * @param filePaths file absolute path array.
     */
    public void scan(String[] filePaths) {
        if (filePaths != null && filePaths.length > 0) {
            this.mLinkedList.add(filePaths);
            executeOnce();
        }
    }

    /**
     * Execute scanner.
     */
    private void executeOnce() {
        if (!isRunning() && mLinkedList.size() > 0) {
            this.mCurrentScanPaths = mLinkedList.remove(0);
            this.mMediaScanConn.connect();
        }
    }

    @Override
    public void onMediaScannerConnected() {
        for (String filePath : mCurrentScanPaths) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            mMediaScanConn.scanFile(filePath, mimeType);
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mScanCount += 1;
        if (mScanCount == mCurrentScanPaths.length) {
            mMediaScanConn.disconnect();
            mScanCount = 0;
            mCurrentScanPaths = null;
            executeOnce();
        }
    }
}
