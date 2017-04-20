package top.yokey.gxsfxy.activity.admin;

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
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.activity.mine.MineEditActivity;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.LogUtil;
import top.yokey.gxsfxy.utility.QRCodeUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class AdminUserDetailedActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String user_id;

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
    private TextView adminTextView;

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
        setContentView(R.layout.activity_admin_user_detailed);
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
        adminTextView = (TextView) findViewById(R.id.adminTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("用户详细");
        moreImageView.setImageResource(R.mipmap.ic_action_more);
        user_id = mActivity.getIntent().getStringExtra("user_id");
        if (TextUtil.isEmpty(user_id)) {
            ToastUtil.show(mActivity, "传入参数有误");
            mApplication.finishActivity(mActivity);
            return;
        }

        getJson();

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
                    mApplication.startActivityLogin(AdminMainActivity.mActivity);
                } else {
                    String content = "[uid:" + mApplication.userHashMap.get("user_id") + "]";
                    DialogUtil.qrCode(mActivity, "二维码", QRCodeUtil.create(content, 512, 512));
                }
            }
        });

        adminTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adminTextView.getText().toString().equals("无权操作")) {
                    ToastUtil.show(mActivity, "无权操作");
                    return;
                }

                DialogUtil.progress(mActivity);
                UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "adminUser", "userSetPower");
                ajaxParams.put("user_id", user_id);
                ajaxParams.put("user_power", adminTextView.getText().toString().replace("设为", ""));

                mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=adminUser&a=userSetPower", ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        DialogUtil.cancel();
                        if (mApplication.getJsonSuccess(o.toString())) {
                            ToastUtil.showSuccess(mActivity);
                            getJson();
                        } else {
                            ToastUtil.showFailureNetwork(mActivity);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtil.showFailureNetwork(mActivity);
                        DialogUtil.cancel();
                    }
                });

            }
        });

    }

    private void getJson() {

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "adminUser", "userDetailed");
        ajaxParams.put("user_id", user_id);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=adminUser&a=userDetailed", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(mApplication.getJsonData(o.toString()));
                    LogUtil.show(jsonObject.toString());
                    nicknameTextView.setText(jsonObject.getString("nick_name"));
                    String ip = "http://ip.taobao.com/service/getIpInfo.php?ip=";
                    String loginIp = ip;
                    if (jsonObject.getString("login_ip") == null) {
                        loginIp += "192.168.0.1";
                    } else {
                        loginIp += jsonObject.getString("login_ip");
                    }
                    String loginLastIp = ip;
                    if (jsonObject.getString("login_last_ip") == null) {
                        loginLastIp += "192.168.0.1";
                    } else {
                        loginLastIp += jsonObject.getString("login_last_ip");
                    }
                    if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                        avatarImageView.setImageResource(R.mipmap.ic_avatar);
                    } else {
                        ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), avatarImageView);
                    }
                    if (jsonObject.getString("user_gender").equals("男")) {
                        genderImageView.setImageResource(R.mipmap.ic_default_boy);
                    } else {
                        genderImageView.setImageResource(R.mipmap.ic_default_girl);
                    }
                    if (jsonObject.getString("email_bind").equals("1")) {
                        emailTextView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_user_center_email_bind), null, null);
                    } else {
                        emailTextView.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_user_center_email), null, null);
                    }
                    if (!TextUtil.isEmpty(jsonObject.getString("stu_id"))) {
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
                    String user_qq = jsonObject.getString("user_qq");
                    String user_city = jsonObject.getString("user_city");
                    String user_area = jsonObject.getString("user_area");
                    String user_sign = jsonObject.getString("user_sign");
                    String user_power = jsonObject.getString("user_power");
                    String user_province = jsonObject.getString("user_province");
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
                    switch (user_power) {
                        case "超级管理员":
                            if (mApplication.userHashMap.get("user_power").equals("超级管理员")) {
                                if (mApplication.userHashMap.get("user_mobile").equals("15207713074")) {
                                    adminTextView.setText("设为管理员");
                                } else {
                                    adminTextView.setText("无权操作");
                                }
                            } else {
                                adminTextView.setText("无权操作");
                            }
                            break;
                        case "管理员":
                            if (mApplication.userHashMap.get("user_power").equals("超级管理员")) {
                                adminTextView.setText("设为超级管理员");
                            } else {
                                adminTextView.setText("无权操作");
                            }
                            break;
                        case "用户":
                            if (!mApplication.userHashMap.get("user_power").equals("用户")) {
                                adminTextView.setText("设为管理员");
                            } else {
                                adminTextView.setText("无权操作");
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getJsonFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                getJsonFailure();
            }
        });

    }

    private void getJsonFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("是否重试？")
                    .setMessage("读取用户信息失败")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getJson();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            returnActivity();
                        }
                    })
                    .show();
        }

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}