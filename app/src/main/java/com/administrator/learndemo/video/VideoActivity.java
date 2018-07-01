package com.administrator.learndemo.video;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.administrator.learndemo.R;
import com.administrator.learndemo.mp4.VideoRender;

import java.io.IOException;

public class VideoActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener,  SurfaceHolder.Callback{
    public String TAG = "VIDEO_TEST";

//    public String videoPath = Environment.getExternalStorageDirectory().getPath()
//            + "/DCIM/Camera/VID_20180520_161800.mp4";

    public String videoPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    private TextureSurfaceRenderer videoRenderer;
    private int surfaceWidth;
    private int surfaceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        GLSurfaceView surfaceView = new GLSurfaceView(this);
//        surfaceView.setRenderer(new VideoRender(this));
//        this.setContentView(surfaceView);

        setContentView(R.layout.activity_mp4);

        textureView = (TextureView) findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(this);

    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        videoRenderer = new VideoTextureSurfaceRenderer(this, surfaceTexture, surfaceWidth, surfaceHeight);
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            this.mediaPlayer = new MediaPlayer();

            while (videoRenderer.getSurfaceTexture() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Surface surface = new Surface(videoRenderer.getSurfaceTexture());
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setSurface(surface);

            surface.release();

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setLooping(true);
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            if (mp != null) {
                mp.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.v(TAG, "GLViewMediaActivity::onResume()");
        super.onResume();
    }


    @Override protected void onStart()
    {
        Log.v(TAG, "GLViewMediaActivity::onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "GLViewMediaActivity::onPause()");
        super.onPause();
        if (videoRenderer != null) {
            videoRenderer.onPause();
            videoRenderer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer =null;
        }
    }

    @Override protected void onStop()
    {
        Log.v(TAG, "GLViewMediaActivity::onStop()");
        super.onStop();
    }

    @Override protected void onDestroy()
    {
        Log.v(TAG, "GLViewMediaActivity::onDestroy()");
        super.onDestroy();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.v( TAG, "GLViewMediaActivity::onSurfaceTextureAvailable()"+ " tName:" + Thread.currentThread().getName() + "  tid:");

        surfaceWidth = width;
        surfaceHeight = height;
        playVideo(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /****************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v( TAG, "GLViewMediaActivity::surfaceCreated()" );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v( TAG, "GLViewMediaActivity::surfaceChanged()" );
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v( TAG, "GLViewMediaActivity::surfaceDestroyed()" );
    }
}
