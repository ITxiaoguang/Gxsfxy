package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
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
import top.yokey.gxsfxy.utility.ToastUtil;

public class MinePassActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private EditText passwordEditText;
    private EditText passwordNewEditText;
    private EditText passwordRepeatEditText;
    private TextView modifyTextView;

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
        setContentView(R.layout.activity_mine_pass);
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

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordNewEditText = (EditText) findViewById(R.id.passwordNewEditText);
        passwordRepeatEditText = (EditText) findViewById(R.id.passwordRepeatEditText);
        modifyTextView = (TextView) findViewById(R.id.modifyTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("修改密码");

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        modifyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyPass();
            }
        });

    }

    private void modifyPass() {

        String password = passwordEditText.getText().toString();
        String passwordNew = passwordNewEditText.getText().toString();
        String passwordRepeat = passwordRepeatEditText.getText().toString();

        if (!password.equals(mApplication.userHashMap.get("user_pass"))) {
            ToastUtil.show(mActivity, "旧密码不对");
            return;
        }

        if (password.equals(passwordNew)) {
            ToastUtil.show(mActivity, "新密码与旧密码相同");
            return;
        }

        if (!passwordNew.equals(passwordRepeat)) {
            ToastUtil.show(mActivity, "两次输入的新密码不一样");
            return;
        }

        if (passwordNew.length() < 6) {
            ToastUtil.show(mActivity, "新密码不能少于6位");
            return;
        }

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "modifyPass");
        ajaxParams.put("password", password);
        ajaxParams.put("password_new", passwordNew);
        ajaxParams.put("password_repeat", passwordRepeat);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=modifyPass", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    ToastUtil.show(mActivity, "修改密码成功，请重新登录");
                    AndroidUtil.hideKeyboard(modifyTextView);
                    mActivity.setResult(RESULT_OK);
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
                .setMessage("取消修改密码")
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