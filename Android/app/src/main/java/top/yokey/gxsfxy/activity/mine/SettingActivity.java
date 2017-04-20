package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;

public class SettingActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private CheckBox messageNotifyCheckBox;
    private CheckBox saveFlowCheckBox;
    private CheckBox messagePushCheckBox;
    private CheckBox updateCheckCheckBox;

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
        setContentView(R.layout.activity_setting);
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

        messageNotifyCheckBox = (CheckBox) findViewById(R.id.messageNotifyCheckBox);
        saveFlowCheckBox = (CheckBox) findViewById(R.id.saveFlowCheckBox);
        messagePushCheckBox = (CheckBox) findViewById(R.id.messagePushCheckBox);
        updateCheckCheckBox = (CheckBox) findViewById(R.id.updateCheckCheckBox);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("系统设置");

        messageNotifyCheckBox.setChecked(mApplication.messageNotifyBoolean);
        saveFlowCheckBox.setChecked(mApplication.saveFlowCheckBoxBoolean);
        messagePushCheckBox.setChecked(mApplication.messagePushCheckBoxBoolean);
        updateCheckCheckBox.setChecked(mApplication.updateCheckBoolean);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        messageNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mApplication.messageNotifyBoolean = b;
            }
        });

        saveFlowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mApplication.saveFlowCheckBoxBoolean = b;
            }
        });

        messagePushCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mApplication.messagePushCheckBoxBoolean = b;
            }
        });

        updateCheckCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mApplication.updateCheckBoolean = b;
            }
        });

    }

    private void returnActivity() {

        mApplication.mSharedPreferencesEditor.putBoolean("setting_message_notify", mApplication.messageNotifyBoolean);
        mApplication.mSharedPreferencesEditor.putBoolean("setting_save_flow", mApplication.saveFlowCheckBoxBoolean);
        mApplication.mSharedPreferencesEditor.putBoolean("setting_message_push", mApplication.messagePushCheckBoxBoolean);
        mApplication.mSharedPreferencesEditor.putBoolean("setting_update_check", mApplication.updateCheckBoolean);
        mApplication.mSharedPreferencesEditor.apply();
        mApplication.finishActivity(mActivity);

    }

}