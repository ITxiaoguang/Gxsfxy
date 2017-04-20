package top.yokey.gxsfxy.activity.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class EduActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private TextView cjcxTextView;
    private TextView wdkbTextView;

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
        setContentView(R.layout.activity_edu);
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

        cjcxTextView = (TextView) findViewById(R.id.cjcxTextView);
        wdkbTextView = (TextView) findViewById(R.id.wdkbTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("登陆中...");
        loginEdu();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        cjcxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, EduCJCXActivity.class));
            }
        });

        wdkbTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, EduWDKBActivity.class));
            }
        });

    }

    private void loginEdu() {

        DialogUtil.progress(mActivity, "正在登录教务系统");

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("USERNAME", mApplication.userHashMap.get("stu_id"));
        ajaxParams.put("PASSWORD", mApplication.userHashMap.get("stu_pass"));

        mApplication.eduFinalHttp = new FinalHttp();
        mApplication.eduFinalHttp.configTimeout(5000);
        mApplication.eduFinalHttp.configCharset("UTF-8");
        mApplication.eduFinalHttp.post(mApplication.eduLoginUrlString, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                //读取个人信息
                mApplication.eduFinalHttp.get(mApplication.eduInfoUrlString, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        DialogUtil.cancel();
                        if (!o.toString().contains("学籍卡片")) {
                            ToastUtil.show(mActivity, "教务系统账号或密码错误，请到个人中心更新绑定信息");
                            mApplication.finishActivity(mActivity);
                        } else {
                            titleTextView.setText("欢迎您 - ");
                            titleTextView.append(mApplication.userHashMap.get("true_name"));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        DialogUtil.cancel();
                        loginEduFailure();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                loginEduFailure();
            }
        });

    }

    private void loginEduFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("是否重试?")
                    .setMessage("登录教务系统失败")
                    .setCancelable(false)
                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loginEdu();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mApplication.finishActivity(mActivity);
                        }
                    })
                    .show();
        }

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}