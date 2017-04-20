package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;


import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.QRCodeUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class MineCenterActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private boolean logoutBoolean;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView moreImageView;

    private ImageView avatarImageView;
    private TextView nicknameTextView;
    private ImageView genderImageView;
    private TextView loginTextView;
    private TextView loginLastTextView;
    private ImageView qrCodeImageView;

    private TextView passwordTextView;
    private TextView mobileTextView;
    private TextView emailTextView;
    private TextView eduTextView;

    private TextView qqTextView;
    private TextView signTextView;
    private TextView provinceTextView;
    private TextView cityTextView;
    private TextView areaTextView;

    private TextView logoutTextView;

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == RESULT_OK) {
            switch (req) {
                case MyApplication.CODE_MINE_EDIT_PASS:
                    logoutBoolean = false;
                    logout();
                    break;
                case MyApplication.CODE_MINE_BIND_EMAIL:
                    setValue();
                    break;
                case MyApplication.CODE_MINE_BIND_EDU:
                    setValue();
                    break;
                case MyApplication.CODE_MINE_EDIT_INFO:
                    setValue();
                    break;
                default:
                    setValue();
                    break;
            }
        }
    }

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
        setContentView(R.layout.activity_mine_center);
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
        moreImageView = (ImageView) findViewById(R.id.moreImageView);

        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
        genderImageView = (ImageView) findViewById(R.id.genderImageView);
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        loginLastTextView = (TextView) findViewById(R.id.loginLastTextView);
        qrCodeImageView = (ImageView) findViewById(R.id.qrCodeImageView);

        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        mobileTextView = (TextView) findViewById(R.id.mobileTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        eduTextView = (TextView) findViewById(R.id.eduTextView);

        qqTextView = (TextView) findViewById(R.id.qqTextView);
        signTextView = (TextView) findViewById(R.id.signTextView);
        provinceTextView = (TextView) findViewById(R.id.provinceTextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);
        areaTextView = (TextView) findViewById(R.id.areaTextView);

        logoutTextView = (TextView) findViewById(R.id.logoutTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        logoutBoolean = true;
        titleTextView.setText("个人中心");
        moreImageView.setImageResource(R.mipmap.ic_action_edit);

        setValue();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivityWithLoginSuccess(mActivity, new Intent(mActivity, MineEditActivity.class), MyApplication.CODE_MINE_EDIT_INFO);
            }
        });

        qrCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mApplication.userHashMap.isEmpty()) {
                    mApplication.startActivityLogin(MainActivity.mActivity);
                } else {
                    String content = "[uid:" + mApplication.userHashMap.get("user_id") + "]";
                    DialogUtil.qrCode(mActivity, "扫描二维码关注我吧!", QRCodeUtil.create(content, 512, 512));
                }
            }
        });

        passwordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivityWithLoginSuccess(mActivity, new Intent(mActivity, MinePassActivity.class), MyApplication.CODE_MINE_EDIT_PASS);
            }
        });

        mobileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(mActivity, "您已绑定手机，可以使用手机号码快速登录");
            }
        });

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, BindEmailActivity.class), MyApplication.CODE_MINE_BIND_EMAIL);
            }
        });

        eduTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, BindEduActivity.class), MyApplication.CODE_MINE_BIND_EDU);
            }
        });

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("确认您的选择")
                        .setMessage("注销登录?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }

    private void setValue() {

        nicknameTextView.setText(mApplication.userHashMap.get("nick_name"));
        String ip = "http://ip.taobao.com/service/getIpInfo.php?ip=";
        String loginIp = ip + mApplication.userHashMap.get("login_ip");
        String loginLastIp = ip + mApplication.userHashMap.get("login_last_ip");

        if (TextUtil.isEmpty(mApplication.userHashMap.get("user_avatar"))) {
            avatarImageView.setImageResource(R.mipmap.ic_avatar);
        } else {
            ImageLoader.getInstance().displayImage(mApplication.userHashMap.get("user_avatar"), avatarImageView);
        }
        if (mApplication.userHashMap.get("user_gender").equals("男")) {
            genderImageView.setImageResource(R.mipmap.ic_default_boy);
        } else {
            genderImageView.setImageResource(R.mipmap.ic_default_girl);
        }
        if (mApplication.userHashMap.get("email_bind").equals("1")) {
            emailTextView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_user_center_email_bind), null, null);
        } else {
            emailTextView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_user_center_email), null, null);
        }
        if (!TextUtil.isEmpty(mApplication.userHashMap.get("stu_id"))) {
            eduTextView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_user_center_edu_bind), null, null);
        } else {
            eduTextView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_user_center_edu), null, null);
        }
        if (!loginTextView.getText().toString().contains("本次登录")) {
            loginTextView.setText("...");
            mApplication.mFinalHttp.get(loginIp, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    if (TextUtil.isEmpty(o.toString()) || o.toString().contains("invaild ip")) {
                        loginTextView.setText("本次登录：未知");
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            jsonObject = new JSONObject(jsonObject.getString("data"));
                            loginTextView.setText("本次登录：");
                            loginTextView.append(jsonObject.getString("city"));
                            loginTextView.append(" ");
                            loginTextView.append(jsonObject.getString("isp"));
                        } catch (JSONException e) {
                            loginTextView.setText("本次登录：未知");
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (!loginLastTextView.getText().toString().contains("上次登录")) {
            loginLastTextView.setText("...");
            mApplication.mFinalHttp.get(loginLastIp, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    if (TextUtil.isEmpty(o.toString()) || o.toString().contains("invaild ip")) {
                        loginLastTextView.setText("上次登录：未知");
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            jsonObject = new JSONObject(jsonObject.getString("data"));
                            loginLastTextView.setText("上次登录：");
                            loginLastTextView.append(jsonObject.getString("city"));
                            loginLastTextView.append(" ");
                            loginLastTextView.append(jsonObject.getString("isp"));
                        } catch (JSONException e) {
                            loginLastTextView.setText("上次登录：未知");
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        String user_qq = mApplication.userHashMap.get("user_qq");
        String user_city = mApplication.userHashMap.get("user_city");
        String user_area = mApplication.userHashMap.get("user_area");
        String user_sign = mApplication.userHashMap.get("user_sign");
        String user_province = mApplication.userHashMap.get("user_province");

        if (TextUtil.isEmpty(user_qq)) {
            user_qq = "未填写";
        }

        if (TextUtil.isEmpty(user_city)) {
            user_city = "未填写";
        }

        if (TextUtil.isEmpty(user_area)) {
            user_area = "未填写";
        }

        if (TextUtil.isEmpty(user_sign)) {
            user_sign = "未填写";
        }

        if (TextUtil.isEmpty(user_province)) {
            user_province = "未填写";
        }

        qqTextView.setText(user_qq);
        signTextView.setText(user_sign);
        provinceTextView.setText(user_province);
        cityTextView.setText(user_city);
        areaTextView.setText(user_area);

    }

    private void logout() {

        DialogUtil.progress(mActivity);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=logout", new UserAjaxParams(mApplication, "user", "logout"), new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    mApplication.mSharedPreferencesEditor.putString("user_token", "").apply();
                    if (logoutBoolean) {
                        ToastUtil.showSuccess(mActivity);
                    }
                    mApplication.userTokenString = "";
                    mApplication.userHashMap.clear();
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

        mApplication.finishActivity(mActivity);

    }

}