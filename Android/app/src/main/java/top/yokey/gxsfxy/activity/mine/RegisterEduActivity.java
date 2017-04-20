package top.yokey.gxsfxy.activity.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class RegisterEduActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private int iCalc;
    private String mobile;
    private String nickname;
    private String password;
    private String passwordRepeat;

    private String stu_id;
    private String stu_pass;
    private String college;
    private String professional;
    private String classmate;
    private String true_name;
    private String gender;
    private String card;
    private String birthday;

    private ImageView backImageView;
    private TextView titleTextView;

    private RelativeLayout verifyRelativeLayout;
    private EditText mobileEditText;
    private EditText verifyEditText;
    private TextView verifyTextView;
    private TextView getTextView;

    private RelativeLayout inputRelativeLayout;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private EditText nicknameEditText;
    private TextView registerTextView;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            DialogUtil.cancel();
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    ToastUtil.show(mActivity, "短信验证成功");
                    showUI();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    ToastUtil.show(mActivity, "短信验证已发送");
                    iCalc = 60;
                    getTextView.setEnabled(false);
                    mobileEditText.setEnabled(false);
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            iCalc--;
                            String temp = iCalc + " S";
                            getTextView.setText(temp);
                        }

                        @Override
                        public void onFinish() {
                            mobileEditText.setEnabled(true);
                            getTextView.setEnabled(true);
                            getTextView.setText("获取");
                        }
                    }.start();
                } else {
                    ((Throwable) data).printStackTrace();
                }
            } else if (result == SMSSDK.RESULT_ERROR) {
                ToastUtil.show(mActivity, "失败了,请重试!");
            }
        }
    };

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
        setContentView(R.layout.activity_register_edu);
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
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.backImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        verifyRelativeLayout = (RelativeLayout) findViewById(R.id.verifyRelativeLayout);
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        verifyEditText = (EditText) findViewById(R.id.verifyEditText);
        verifyTextView = (TextView) findViewById(R.id.verifyTextView);
        getTextView = (TextView) findViewById(R.id.getTextView);

        inputRelativeLayout = (RelativeLayout) findViewById(R.id.inputRelativeLayout);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordRepeatEditText = (EditText) findViewById(R.id.passwordRepeatEditText);
        nicknameEditText = (EditText) findViewById(R.id.nicknameEditText);
        registerTextView = (TextView) findViewById(R.id.registerTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        iCalc = 0;
        mobile = "";
        password = "";
        nickname = "";
        passwordRepeat = "";

        titleTextView.setText("验证手机号码");
        stu_id = mActivity.getIntent().getStringExtra("stu_id");
        stu_pass = mActivity.getIntent().getStringExtra("stu_pass");
        college = mActivity.getIntent().getStringExtra("college");
        professional = mActivity.getIntent().getStringExtra("professional");
        classmate = mActivity.getIntent().getStringExtra("classmate");
        true_name = mActivity.getIntent().getStringExtra("true_name");
        gender = mActivity.getIntent().getStringExtra("gender");
        card = mActivity.getIntent().getStringExtra("card");
        birthday = mActivity.getIntent().getStringExtra("birthday");

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        getTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = mobileEditText.getText().toString();
                if (!TextUtil.isMobileNumber(mobile)) {
                    ToastUtil.show(mActivity, "手机号码格式不对");
                    return;
                }
                DialogUtil.progress(mActivity, "正在发送验证码");
                SMSSDK.getVerificationCode("86", mobile);
            }
        });

        verifyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.progress(mActivity, "正在验证...");
                SMSSDK.submitVerificationCode("86", mobile, verifyEditText.getText().toString());
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });

    }

    private void showUI() {

        verifyRelativeLayout.setVisibility(View.GONE);
        inputRelativeLayout.setVisibility(View.VISIBLE);
        inputRelativeLayout.startAnimation(mApplication.showAlphaAnimation);
        usernameEditText.setEnabled(false);
        usernameEditText.setText(mobile);

    }

    private void reg() {

        password = passwordEditText.getText().toString();
        passwordRepeat = passwordRepeatEditText.getText().toString();
        nickname = nicknameEditText.getText().toString();

        if (TextUtil.isEmpty(password)) {
            ToastUtil.show(mActivity, "密码不能为空");
            return;
        }

        if (TextUtil.isEmpty(passwordRepeat)) {
            ToastUtil.show(mActivity, "请再次输入密码");
            return;
        }

        if (!passwordRepeat.equals(password)) {
            ToastUtil.show(mActivity, "两次输入的密码不一样");
            return;
        }

        if (TextUtil.isEmpty(nickname)) {
            ToastUtil.show(mActivity, "昵称不能为空");
            return;
        }

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "registerEdu");
        ajaxParams.put("mobile", mobile);
        ajaxParams.put("password", password);
        ajaxParams.put("password_repeat", passwordRepeat);
        ajaxParams.put("nickname", nickname);
        ajaxParams.put("stu_id", stu_id);
        ajaxParams.put("stu_pass", stu_pass);
        ajaxParams.put("college", college);
        ajaxParams.put("professional", professional);
        ajaxParams.put("classmate", classmate);
        ajaxParams.put("true_name", true_name);
        ajaxParams.put("gender", gender);
        ajaxParams.put("card", card);
        ajaxParams.put("birthday", birthday);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=registerEdu", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    ToastUtil.showSuccess(mActivity);
                    Intent intent = new Intent();
                    intent.putExtra("mobile", mobile);
                    mActivity.setResult(RESULT_OK, intent);
                    mApplication.finishActivity(mActivity);
                } else {
                    ToastUtil.show(mActivity, mApplication.getJsonError(o.toString()));
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.showFailure(mActivity);
                DialogUtil.cancel();
            }
        });

    }

    private void returnActivity() {

        new AlertDialog.Builder(mActivity)
                .setTitle("确认您的选择")
                .setMessage("取消注册?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mApplication.finishActivity(mActivity);
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

}