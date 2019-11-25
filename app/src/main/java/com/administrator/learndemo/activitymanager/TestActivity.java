package com.administrator.learndemo.activitymanager;

import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.administrator.learndemo.R;
import com.xingfeng.FingerPrintLib.asm.AppTest;
import com.xingfeng.FingerPrintLib.asm.Time;

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
    }

    /**
     * 获取音频时长
     * @return
     */
    private String getAudiDuration() {
        long startTime = System.currentTimeMillis();
        String url = "http://pre-cdn.iov.changan.com.cn/static/cvrtmp3/tspdemo/a9955d796467bd3d543855c891cc4218e72d282cbfe10347a4386705e78cdf43157f230ae081cce089e184873541d6be.mp3";
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(url, new HashMap<String, String>());
        String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.i("DEBUG_TEST", " audio duration:" + duration + " cost time:" + (System.currentTimeMillis() - startTime));
        return duration;
    }
}
