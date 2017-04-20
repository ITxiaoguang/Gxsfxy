package top.yokey.gxsfxy.activity.dynamic;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;

public class ActivityCreateActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private TextView qqTextView;
    private TextView phoneTextView;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            returnActivity();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_activity_create);
        initView();
        initData();
        initEven();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.backImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        qqTextView = (TextView) findViewById(R.id.qqTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("发布活动");

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        qqTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivityBrowser(mActivity, "http://wpa.qq.com/msgrd?v=3&uin=1002285057&site=qq&menu=yes");
            }
        });

        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startCall(mActivity, "15207713074");
            }
        });

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}