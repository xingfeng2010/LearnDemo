package com.administrator.learndemo.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/20.
 */

public class TestContentProvider extends ContentProvider {
    public static final String TAG = "TestContentProvider";

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File root = Environment.getExternalStorageDirectory();
      //  root.mkdirs();
        File path = new File(root, uri.getEncodedPath());

        Log.i(TAG, "opeFile:" + path + " mode:" + mode);
        int imode = 0;
       // if (mode.contains("w")) {
            imode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
        Log.i(TAG, "opeFile createNewFile 00");
            if (!path.exists()) {
                try {
                    Log.i(TAG, "opeFile createNewFile 11");
                    path.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        //}

        Log.i(TAG, "opeFile 222 ");
        if (mode.contains("r"))
            imode |= ParcelFileDescriptor.MODE_READ_ONLY;
        if (mode.contains("+"))
            imode |= ParcelFileDescriptor.MODE_APPEND;

        Log.i(TAG, "opeFile 333");
        ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(path, imode);

        Log.i(TAG, "opeFile 444 descriptor:" + descriptor);
        return descriptor;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
