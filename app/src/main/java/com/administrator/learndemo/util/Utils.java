package com.administrator.learndemo.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class Utils {
    public final static int MAX_LENGTH = 1500;
    public final static int MAX_SIZE = 500;
    private static final String TAG = "Utils";

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //使用Bitmap加Matrix来缩放
    public static Bitmap resizeImage(Bitmap bitmap, int reqWidht, int reqHeight) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = reqWidht;
        int newHeight = reqHeight;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    public static byte[] convertFileToBytes(File file) {
        long filesize = file.length();
        if (filesize > Integer.MAX_VALUE) {
            //LogUtils.i("Utils", "convertFileToBytes,filesize beyond MAX_VALUE filesize:" + filesize);
            return null;
        }

        byte[] buffer = new byte[(int) filesize];
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
            int offset = 0;
            int numread = 0;
            while (offset < buffer.length && (numread = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numread;
            }

            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return buffer;
    }

    public static Bitmap convertDrawableToBitMap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        return bitmap;
    }

    public static byte[] toByteArray(InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output)
            throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static String getCurrentTime(int leaveSecond) {
        long time = System.currentTimeMillis();
        time = time + leaveSecond * 1000;
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        StringBuffer sb = new StringBuffer();
        if (hour < 10) {
            sb.append("0" + hour);
        } else {
            sb.append(hour + "");
        }
        sb.append(":");

        if (min < 10) {
            sb.append("0" + min);
        } else {
            sb.append(min + "");
        }

        return sb.toString();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // 计算 inSampleSize 的值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 根据计算出的 inSampleSize 来解码图片生成Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return
     */
    public static void copyFile(final String oldPath, final String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                fs.close();
                inStream.close();
                oldfile.delete();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 图片压缩到指定大小
     *
     * @param
     * @param compressSize
     */
    public static File compress(String srcPath, int compressSize, String desPath) {
        if (TextUtils.isEmpty(desPath)) {
            return null;
        }
        File srcFile = new File(srcPath);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);

        /**
         * 判断文件大小是否超过指定的阈值
         * 1 是，判断bitmap的长宽是否超过指定的大小
         *          1 是，先尺寸压缩，再质量压缩
         *          2 否，直接质量压缩
         * 2 否，直接copy一份原文件
         */
        if (srcFile.length() <= compressSize * 1024) {
            return srcFile;
        } else {
            if (opts.outWidth < MAX_LENGTH && opts.outHeight < MAX_LENGTH) {
                return qualityCompress(compressSize, desPath, bitmap);
            } else {
                float scaleWidth = 0;
                float scaleHeight = 0;
                if (opts.outWidth > opts.outHeight) {
                    scaleWidth = MAX_LENGTH;
                    float scale = opts.outWidth / MAX_LENGTH;
                    scaleHeight = opts.outHeight / scale;
                } else {
                    scaleHeight = MAX_LENGTH;
                    float scale = opts.outHeight / MAX_LENGTH;
                    scaleWidth = opts.outWidth / scale;
                }
                bitmap = sizeCompres(srcPath, (int) scaleWidth, (int) scaleHeight);
                return qualityCompress(compressSize, desPath, bitmap);
            }
        }
    }

    /**
     * 质量压缩
     *
     * @param compressSize
     * @param desPath
     * @param bitmap
     */
    private static File qualityCompress(int compressSize, String desPath, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File desFile = null;
        int quality = 100;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            baos.writeTo(new FileOutputStream(desPath));
            desFile = new File(desPath);
            while (desFile.length() > compressSize * 1024) {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                Log.i(TAG,"qualityCompress quality:" + quality);
                baos.writeTo(new FileOutputStream(desPath));
                desFile = new File(desPath);
                quality -= 10;
            }
            bitmap.recycle();
            bitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return desFile;
    }

    /**
     * 尺寸压缩
     *
     * @param path
     * @param rqsW
     * @param rqsH
     * @return
     */
    public static Bitmap sizeCompres(String path, int rqsW, int rqsH) {
        // 用option设置返回的bitmap对象的一些属性参数
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置仅读取Bitmap的宽高而不读取内容
        BitmapFactory.decodeFile(path, options);// 获取到图片的宽高，放在option里边
        final int height = options.outHeight;//图片的高度放在option里的outHeight属性中
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) {
            options.inSampleSize = 1;
        } else if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            options.inSampleSize = inSampleSize;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);// 主要通过option里的inSampleSize对原图片进行按比例压缩
    }

    private static final String FILE_FORMAT = ".jpg";
    private static final String PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static File saveBitmapToFile(Bitmap bitmap, String path) {
        File sysFile = new File(PIC_PATH);
        File filePic = new File(sysFile, path + FILE_FORMAT);

        Log.i(TAG, "responsAlbumPic file name:" + filePic.getName());
        if (filePic.exists()) {
            filePic.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {

        }

        return filePic;
    }

    public static String getDesFileName(Context context) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            String savePath = context.getExternalCacheDir().getPath();
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            // 照片命名
            String compressFileName = "compress_" + timeStamp + ".jpg";
            return new File(savePath, compressFileName).getAbsolutePath();
        } else {
            return null;
        }
    }

}
