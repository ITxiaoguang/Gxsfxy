package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class BindEmailActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private int iCalc;
    private String code;
    private String email;

    private ImageView backImageView;
    private TextView titleTextView;

    private EditText emailEditText;
    private EditText verifyEditText;
    private TextView bindTextView;
    private TextView getTextView;

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
        setContentView(R.layout.activity_bind_email);
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

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        verifyEditText = (EditText) findViewById(R.id.verifyEditText);
        bindTextView = (TextView) findViewById(R.id.bindTextView);
        getTextView = (TextView) findViewById(R.id.getTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        code = "";
        email = "";
        titleTextView.setText("绑定邮箱");
        if (!TextUtil.isEmpty(mApplication.userHashMap.get("user_email"))) {
            emailEditText.setText(mApplication.userHashMap.get("user_email"));
            emailEditText.setSelection(mApplication.userHashMap.get("user_email").length());
        }
        verifyPass();

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
                email = emailEditText.getText().toString();
                if (!TextUtil.isEmailAddress(email)) {
                    ToastUtil.show(mActivity, "邮箱格式不对");
                    return;
                }
                getEmailCode();
            }
        });

        bindTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmailCode();
            }
        });

    }

    private void verifyPass() {

        final Dialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_input);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
        titleTextView.setText("请输入账号密码");
        final EditText contentEditText = (EditText) window.findViewById(R.id.contentEditText);
        contentEditText.setText("");
        TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                if (content.equals(mApplication.userHashMap.get("user_pass"))) {
                    AndroidUtil.hideKeyboard(v);
                    dialog.cancel();
                } else {
                    ToastUtil.show(mActivity, "密码不正确");
                }
            }
        });
        TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.finishActivity(mActivity);
                dialog.cancel();
            }
        });

    }

    private void getEmailCode() {

        email = emailEditText.getText().toString();

        if (!TextUtil.isEmailAddress(email)) {
            ToastUtil.show(mActivity, "邮箱地址格式不正确");
            return;
        }

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "sendEMailCode");
        ajaxParams.put("email", email);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=sendEMailCode", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    ToastUtil.show(mActivity, "发送成功，请登录邮箱查看验证码");
                    iCalc = 60;
                    getTextView.setEnabled(false);
                    emailEditText.setEnabled(false);
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            iCalc--;
                            String temp = iCalc + " S";
                            getTextView.setText(temp);
                        }

                        @Override
                        public void onFinish() {
                            emailEditText.setEnabled(true);
                            getTextView.setEnabled(true);
                            getTextView.setText("获取");
                        }
                    }.start();
                } else {
                    ToastUtil.showFailure(mActivity);
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

    private void verifyEmailCode() {

        code = verifyEditText.getText().toString();

        if (TextUtil.isEmpty(code)) {
            ToastUtil.show(mActivity, "请输入验证码");
            return;
        }

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "verifyEMailCode");
        ajaxParams.put("email", email);
        ajaxParams.put("code", code);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=verifyEMailCode", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    ToastUtil.showSuccess(mActivity);
                    mApplication.userHashMap.put("user_email", email);
                    mApplication.userHashMap.put("email_bind", "1");
                    mActivity.setResult(RESULT_OK);
                    mApplication.finishActivity(mActivity);
                } else {
                    ToastUtil.showFailure(mActivity);
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
                .setMessage("取消绑定邮箱?")
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