package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.home.NewsDetailedActivity;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.zxing.camera.CameraManager;
import top.yokey.gxsfxy.zxing.decoding.CaptureActivityHandler;
import top.yokey.gxsfxy.zxing.decoding.InactivityTimer;
import top.yokey.gxsfxy.zxing.view.ViewfinderView;

public class ScanActivity extends Activity implements SurfaceHolder.Callback {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    private Activity mActivity;
    private MyApplication mApplication;

    private TextView titleTextView;
    private ImageView backImageView;
    private ViewfinderView viewfinderView;

    private String charString;
    private boolean vibrateBoolean;
    private boolean playBeepBoolean;
    private MediaPlayer mMediaPlayer;
    private boolean hasSurfaceBoolean;
    private CaptureActivityHandler mHandler;
    private InactivityTimer mInactivityTimer;
    private Vector<BarcodeFormat> formatsVector;
    private MediaPlayer.OnCompletionListener beepListener;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurfaceBoolean = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurfaceBoolean) {
            hasSurfaceBoolean = true;
            initCamera(holder);
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            mApplication.finishActivity(mActivity);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_scan);
        initView();
        initData();
        initEven();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.mainSurfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurfaceBoolean) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        formatsVector = null;
        charString = null;

        playBeepBoolean = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeepBoolean = false;
        }
        initBeepSound();
        vibrateBoolean = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onCreate(new Bundle());
    }

    private void initView() {

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        viewfinderView = (ViewfinderView) findViewById(R.id.mainViewfinderView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        hasSurfaceBoolean = false;
        titleTextView.setText("扫一扫");
        mInactivityTimer = new InactivityTimer(mActivity);

        beepListener = new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
            }
        };

        CameraManager.init(mActivity);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.finishActivity(mActivity);
            }
        });

    }

    private void initBeepSound() {
        if (playBeepBoolean && mMediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    public void handleDecode(Result res) {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String result = res.getText();
        if (!TextUtil.isEmpty(result)) {
            //是否是专属APP数据格式
            if (result.substring(0, 1).equals("[") && result.substring(result.length() - 1, result.length()).equals("]")) {
                result = result.substring(1, result.length() - 1);
                //用户
                if (result.contains("uid")) {
                    String id = result.substring(result.indexOf(":") + 1, result.length());
                    mApplication.startActivityUserCenter(mActivity, id);
                }
                //新闻
                if (result.contains("http://www.gxtc.edu.cn/Item/") && result.contains(".aspx")) {
                    handlerNews(result);
                }
            } else {
                //网页
                if (result.contains("http")) {
                    mApplication.startActivityBrowser(mActivity, result);
                }
            }
        }

    }

    private void handlerNews(final String link) {

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "newsInfoByLink");
        ajaxParams.put("link", link);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=newsInfoByLink", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (TextUtil.isEmpty(mApplication.getJsonError(o.toString()))) {
                    try {
                        JSONObject jsonObject = new JSONObject(mApplication.getJsonData(o.toString()));
                        Intent intent = new Intent(mActivity, NewsDetailedActivity.class);
                        intent.putExtra("title", "新闻详细内容");
                        intent.putExtra("news_id", jsonObject.getString("news_id"));
                        intent.putExtra("news_link", jsonObject.getString("news_link"));
                        intent.putExtra("news_from", jsonObject.getString("news_from"));
                        intent.putExtra("news_time", jsonObject.getString("news_time"));
                        intent.putExtra("news_image", jsonObject.getString("news_image"));
                        intent.putExtra("news_title", jsonObject.getString("news_title"));
                        mApplication.startActivity(mActivity, intent);
                    } catch (JSONException e) {
                        mApplication.startActivityBrowser(mActivity, link);
                        e.printStackTrace();
                    }
                } else {
                    mApplication.startActivityBrowser(mActivity, link);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mApplication.startActivityBrowser(mActivity, link);
                DialogUtil.cancel();
            }
        });

    }

    private void playBeepSoundAndVibrate() {
        if (playBeepBoolean && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (vibrateBoolean) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }
        if (mHandler == null) {
            mHandler = new CaptureActivityHandler(this, formatsVector, charString);
        }
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

}