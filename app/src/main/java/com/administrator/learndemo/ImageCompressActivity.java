package com.administrator.learndemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.administrator.learndemo.util.Utils;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.floor;
import static java.lang.Math.random;

public class ImageCompressActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_compress);

        mImageView = (ImageView) findViewById(R.id.image_view);
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = false;
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.newbitmap, opts);
//        mImageView.setImageBitmap(bitmap);

//        File srcFile = Utils.saveBitmapToFile(bitmap, "newbitmap");
//
//        int calSize = (int)Math.sqrt(srcFile.length() / 500 / 1024);
//
//
//
//        Log.i("TEST_BITMAP", " width:" + opts.outWidth);
//        Log.i("TEST_BITMAP", " height:" + opts.outHeight);
//
//
//        Log.i("TEST_BITMAP", " size:" + bitmap.getByteCount());
//        Bitmap newBimap = Utils.resizeImage(bitmap, opts.outWidth / calSize, opts.outHeight / calSize);
//        Log.i("TEST_BITMAP", " size:" + newBimap.getByteCount());
//        mImageView.setImageBitmap(newBimap);
//       File desFile = Utils.saveBitmapToFile(newBimap, "mybitmap");
//
//        Log.i("TEST_BITMAP", "srcFile size:" + srcFile.length());
//        Log.i("TEST_BITMAP", "desFile size:" + desFile.length());

        File file = Utils.compress("/storage/emulated/0/DCIM/Camera/IMG_20190501_133313.jpg", 500, Utils.getDesFileName(this));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int newOrder[] = upsetOrder(numsArr);
        for (int i = 0; i < newOrder.length; i++) {
           // Log.i("TEST_BITMAP", "newOrder:" + newOrder[i]);
        }
    }


    int[] numsArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    public int[] upsetOrder(int[] arr) {
        int len = arr.length;

        int i, j, temp, n = (int)floor(len / 2) + 1;

        while (n-- > 0) {
            double ran1 = random();
            double ran2 = random();
            Log.i("TEST_BITMAP", "ran1:" + ran1);
            Log.i("TEST_BITMAP", "ran2:" + ran2);
            i = (int)floor(ran1 * len);
            j = (int)floor(ran2 * len);
            if (i != j) {
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        return arr;
    }

}
