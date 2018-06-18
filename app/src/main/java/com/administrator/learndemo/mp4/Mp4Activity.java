package com.administrator.learndemo.mp4;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.administrator.learndemo.R;

import java.nio.ByteBuffer;
import java.util.HashMap;

@SuppressWarnings("aa")
public class Mp4Activity extends AppCompatActivity implements SurfaceTexture.OnFrameAvailableListener, TextureView.SurfaceTextureListener{

    public String TAG = "VIDEO_TEST";

   // public String videoPath = Environment.getExternalStorageDirectory().getPath()
   //         + "/DCIM/Camera/VID_20180520_161800.mp4";

    public String videoPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    private EGLHelper mEGLHelper;
    private int videoDecodeTrack;
    private int mInputVideoWidth;
    private int mInputVideoHeight;
    private MediaCodec mVideoDecoder;
    private MediaExtractor mMediaExtractor;

    private int mVideoTextureId;
    private SurfaceTexture mVideoSurfaceTexture;

    private boolean isPlaying;
    private VideoThread videoThread;
    //private AudioThread audioThread;

    private static final long TIMEOUT_US = 1000;
    private Context mContext;

    private TextureView mTextureView;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate 222");
        setContentView(R.layout.activity_mp4);
        Log.d(TAG, "onCreate 111");

        mTextureView = (TextureView) this.findViewById(R.id.texture);
        mTextureView.setSurfaceTextureListener(this);

        mEGLHelper = new EGLHelper();
        mContext = this;
        //play();
    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        mediaPlayer = new MediaPlayer();

        Surface surface = new Surface(surfaceTexture);
        mediaPlayer.setSurface(surface);
        surface.release();

        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                }
            });
        } catch (Exception e) {
            Log.i(TAG,"playVideo exception:" + e);
        }
    }

    private void play() {
        isPlaying = true;
        if (videoThread == null) {
            videoThread = new VideoThread();
            videoThread.start();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        playVideo(surfaceTexture);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    public class VideoThread extends Thread {

        @Override
        public void run() {
            try {
                playByTrack();
            } catch (Exception e) {
                Log.e(TAG," exception:" + e);
                e.printStackTrace();
            }

        }
    }


    private void stop() {
        isPlaying = false;
    }

    private void playByTrack() throws Exception{
        Log.d(TAG, "playByTrack 111");

        Resources res = mContext.getResources();
        AssetFileDescriptor afd = res.openRawResourceFd(R.raw.video);
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        getVideoInfo(metaRetriever);


        Log.d(TAG, "playByTrack 333");
        mMediaExtractor = new MediaExtractor();
        mMediaExtractor.setDataSource(videoPath);
        int count = mMediaExtractor.getTrackCount();
        Log.d(TAG, "playByTrack 222");
        for (int i = 0; i < count; i ++) {
            MediaFormat format = mMediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio")) {

            } else if (mime.startsWith("video")) {
                Log.d(TAG, "playByTrack video FOUND");
                videoDecodeTrack = i;
                mInputVideoWidth = format.getInteger(MediaFormat.KEY_WIDTH);
                mInputVideoHeight = format.getInteger(MediaFormat.KEY_HEIGHT);
                mVideoDecoder = MediaCodec.createDecoderByType(mime);
                mVideoTextureId = mEGLHelper.createTextureID();
                mVideoSurfaceTexture = new SurfaceTexture(mVideoTextureId);
                mVideoSurfaceTexture.setOnFrameAvailableListener(this);
                mMediaExtractor.selectTrack(i);

                //将SurfaceTexture作为参数创建一个Surface，用来接收解码视频流
                mVideoDecoder.configure(format, new Surface(mVideoSurfaceTexture),null,0);
                mVideoDecoder.start();

                MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
                ByteBuffer[] inputBuffers = mVideoDecoder.getInputBuffers();


                boolean isVideoEOS = false;
                while (!Thread.interrupted()) {
                    if (!isVideoEOS) {
                        isVideoEOS = putBufferToCoder(mMediaExtractor,mVideoDecoder);
                    }

                    int outputBufferIndex = mVideoDecoder.dequeueOutputBuffer(videoBufferInfo, TIMEOUT_US);


                }

            }
        }

    }

    private void getVideoInfo(MediaMetadataRetriever mmr) {
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
           // 专辑名
           String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
           // 媒体格式
           String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
          // 艺术家
          String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
          // 播放时长单位为毫秒
          String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
          // 从api level 14才有，即从ICS4.0才有此功能
          String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
          // 路径
          String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);

          Log.i(TAG, "getVideoInfo title:" + title);
        Log.i(TAG, "getVideoInfo album:" + album);
        Log.i(TAG, "getVideoInfo mime:" + mime);
        Log.i(TAG, "getVideoInfo artist:" + artist);
        Log.i(TAG, "getVideoInfo duration:" + duration);
        Log.i(TAG, "getVideoInfo date:" + date);
    }

    //将缓冲区传递至解码器
    private boolean putBufferToCoder(MediaExtractor extractor, MediaCodec decoder) {
        boolean isMediaEOS = false;
        int inputBufferIndex = decoder.dequeueInputBuffer(TIMEOUT_US);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = decoder.getInputBuffer(inputBufferIndex);
            int sampleSize = extractor.readSampleData(inputBuffer, 0);
            Log.i(TAG,"putBufferToCoder sampleSize:" + sampleSize);
            if (sampleSize < 0) {
                decoder.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isMediaEOS = true;
                Log.v(TAG, "media eos");
            } else {
                decoder.queueInputBuffer(inputBufferIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                extractor.advance();
            }
        }
        return isMediaEOS;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.d(TAG, "onFrameAvailable");
        //surfaceTexture.releaseTexImage();
        surfaceTexture.updateTexImage();
    }
}
