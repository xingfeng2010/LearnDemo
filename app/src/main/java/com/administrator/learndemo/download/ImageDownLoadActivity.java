package com.administrator.learndemo.download;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.administrator.learndemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

public class ImageDownLoadActivity extends AppCompatActivity {

    private String TAG = "ImageDownLoadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_down_load);
    }

    public void downImage(View view) {
        downImage();
    }

    private void downImage() {
        final String filePath = this.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();
        final String thumbNaiUrl = "http://pic16.nipic.com/20111006/6239936_092702973000_2.jpg";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());

        final String fileName = time.replaceAll(" ", "-").replaceAll(":", "-") + ".jpg";

        DownloadUtil.getInstance().download2(thumbNaiUrl, filePath, fileName, new OnDownloadListener() {
            @Override
            public void onBeforeLoad(long total) {

            }

            @Override
            public void onDownloadSuccess() {
                Log.i(TAG, "downImage onDownloadSuccess!!");
                DownloadUtil.insertIntoAlbum(fileName, thumbNaiUrl, ImageDownLoadActivity.this);
            }

            @Override
            public void onDownloading(int progress, Call call) {
                Log.i(TAG, "downImage onDownloading progress:" + progress);
            }

            @Override
            public void onDownloadFailed() {
                Log.i(TAG, "downImage onDownloadFailed ");
            }

            @Override
            public void onDownloadCancel() {
            }
        });
    }

    private void mediaScaner(String filePath, String fileName) {
        MediaScanner scanner = new MediaScanner(ImageDownLoadActivity.this);
        scanner.scan(filePath);
    }

    private void notifyUpdate(String filePath, String fileName) {
        File file = new File(filePath, fileName);

        try {
            MediaStore.Images.Media.insertImage(ImageDownLoadActivity.this.getContentResolver(), filePath, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "downImage filePath: " + filePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{file.toString()};
            MediaScannerConnection.scanFile(ImageDownLoadActivity.this, paths, null, null);
        } else {
            final Intent intent;
            if (file.isDirectory()) {
                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
            } else {
                intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
            }
            sendBroadcast(intent);
        }
    }

    private void testData(String filePath, String fileName) {
        File file = new File(filePath, fileName);

        // Save the screenshot to the MediaStore
        ContentValues values = new ContentValues();
        ContentResolver resolver = ImageDownLoadActivity.this.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, filePath);
        values.put(MediaStore.Images.ImageColumns.TITLE, fileName);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.ImageColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg");
        values.put(MediaStore.Images.ImageColumns.WIDTH, 500);
        values.put(MediaStore.Images.ImageColumns.HEIGHT, 500);
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // update file size in the database
        values.clear();
        values.put(MediaStore.Images.ImageColumns.SIZE, file.length());
        resolver.update(uri, values, null, null);
    }
}
