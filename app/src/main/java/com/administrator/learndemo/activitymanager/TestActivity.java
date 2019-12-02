package com.administrator.learndemo.activitymanager;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.administrator.learndemo.R;
import com.xingfeng.FingerPrintLib.asm.AppTest;
import com.xingfeng.FingerPrintLib.asm.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@FileAnnotation("TestActivity")
public class TestActivity extends AppCompatActivity {

    public List<AppTest> moduleApplications = new ArrayList<>();

    @ActivityAnnotation("onCreate")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DEBUG_TEST", " start size:" + moduleApplications.size());
        setContentView(R.layout.activity_test);
        Log.i("DEBUG_TEST", " end size:" + moduleApplications.size());

        AppTest appTest = new AppTest();

        getAudiDuration();

        getOnLingDuration();
    }

    /**
     * 获取音频时长
     * @return
     */
    private String getAudiDuration() {
        long startTime = System.currentTimeMillis();
        String url = "http://pre-cdn.iov.changan.com.cn/static/cvrtmp3/tspdemo/763f8325ca83b368e2f46e118344d13b89e5e8cc78c8f5fc3d835dcbf5cb60c12fc58e57751a0df3c41131226bfdcdd9.mp3";
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(url, new HashMap<String, String>());
        String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.i("DEBUG_TEST", " MediaMetadataRetriever duration:" + duration + " cost time:" + (System.currentTimeMillis() - startTime));
        return duration;
    }

    private void getOnLingDuration() {
        long startTime = System.currentTimeMillis();
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("http://pre-cdn.iov.changan.com.cn/static/cvrtmp3/tspdemo/e7f55f42abfc9575d62096b02a46219997489dc8aaab0c263fe2b7e4f1090cf45364fae18ae87cc1fe6e248a66e93396");
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            Log.i("DEBUG_TEST", " MediaPlayer duration:" + duration + " cost time:" + (System.currentTimeMillis() - startTime));
            if (0 != duration) {
                mediaPlayer.release();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
