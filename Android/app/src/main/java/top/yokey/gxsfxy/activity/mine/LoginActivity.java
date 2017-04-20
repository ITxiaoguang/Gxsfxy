package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView moreImageView;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView loginTextView;

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == RESULT_OK) {
            switch (req) {
                case MyApplication.CODE_REGISTER_BIND:
                    mApplication.finishActivity(mActivity);
                    break;
                case MyApplication.CODE_REGISTER_EDU:
                    loginEdu();
                    break;
                case MyApplication.CODE_REGISTER:
                    usernameEditText.setText(data.getStringExtra("mobile"));
                    ControlUtil.setFocusable(passwordEditText);
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
        setContentView(R.layout.activity_login);
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

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginTextView = (TextView) findViewById(R.id.loginTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("登录");
        moreImageView.setImageResource(R.mipmap.ic_action_register);
        usernameEditText.setText(mApplication.userUsernameString);
        usernameEditText.setSelection(mApplication.userUsernameString.length());

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
                mApplication.startActivity(mActivity, new Intent(mActivity, RegisterActivity.class), MyApplication.CODE_REGISTER);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {

        String type;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtil.isEmpty(username)) {
            ToastUtil.show(mActivity, "用户名不能为空");
            return;
        }

        if (TextUtil.isEmpty(password)) {
            ToastUtil.show(mActivity, "密码不能为空");
            return;
        }

        if (TextUtil.isMobileNumber(username)) {
            type = "user_mobile";
        } else if (username.contains("@")) {
            type = "user_email";
        } else {
            type = "stu_id";
        }

        if (type.equals("stu_id")) {

            loginEdu();

        } else {

            DialogUtil.progress(mActivity);

            BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "login");
            ajaxParams.put("type", type);
            ajaxParams.put("username", username);
            ajaxParams.put("password", password);

            mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=login", ajaxParams, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    DialogUtil.cancel();
                    String error = mApplication.getJsonError(o.toString());
                    if (TextUtil.isEmpty(error)) {
                        mApplication.userTokenString = mApplication.getJsonData(o.toString());
                        mApplication.userUsernameString = usernameEditText.getText().toString();
                        mApplication.mSharedPreferencesEditor.putString("user_token", mApplication.userTokenString);
                        mApplication.mSharedPreferencesEditor.putString("user_username", mApplication.userUsernameString);
                        mApplication.mSharedPreferencesEditor.apply();
                        getUserInfo();
                    } else {
                        ToastUtil.show(mActivity, error);
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

    }

    private void loginEdu() {

        DialogUtil.progress(mActivity, "正在登录教务系统");

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("USERNAME", username);
        ajaxParams.put("PASSWORD", password);

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
                        if (!o.toString().contains("学籍卡片")) {
                            ToastUtil.show(mActivity, "教务系统账号或密码错误");
                            DialogUtil.cancel();
                            return;
                        }
                        String temp = o.toString().replace("&nbsp;", "");
                        temp = temp.substring(temp.indexOf("院系：") + 3, temp.length());
                        final String college = temp.substring(0, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("专业：") + 3, temp.length());
                        final String professional = temp.substring(0, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("班级：") + 3, temp.length());
                        final String classmate = temp.substring(0, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("姓名</td>") + 7, temp.length());
                        final String true_name = temp.substring(temp.indexOf(">") + 1, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("性别</td>") + 7, temp.length());
                        final String gender = temp.substring(temp.indexOf(">") + 1, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("编号</td>") + 7, temp.length());
                        final String card = temp.substring(temp.indexOf(">") + 1, temp.indexOf("</td>"));
                        final String birthday = card.substring(6, 14);
                        //跟服务器对接
                        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "loginEdu");
                        ajaxParams.put("stu_id", username);
                        ajaxParams.put("stu_pass", password);
                        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=loginEdu", ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                DialogUtil.cancel();
                                String error = mApplication.getJsonError(o.toString());
                                if (TextUtil.isEmpty(error)) {
                                    mApplication.userTokenString = mApplication.getJsonData(o.toString());
                                    mApplication.userUsernameString = usernameEditText.getText().toString();
                                    mApplication.mSharedPreferencesEditor.putString("user_token", mApplication.userTokenString);
                                    mApplication.mSharedPreferencesEditor.putString("user_username", mApplication.userUsernameString);
                                    mApplication.mSharedPreferencesEditor.apply();
                                    getUserInfo();
                                } else {
                                    new AlertDialog
                                            .Builder(mActivity)
                                            .setTitle("请选择")
                                            .setCancelable(false)
                                            .setMessage("您是第一次登录系统，请先绑定或者注册手机号码")
                                            .setPositiveButton("绑定账号", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(mActivity, BindMobileActivity.class);
                                                    intent.putExtra("stu_id", username);
                                                    intent.putExtra("stu_pass", password);
                                                    intent.putExtra("college", college);
                                                    intent.putExtra("professional", professional);
                                                    intent.putExtra("classmate", classmate);
                                                    intent.putExtra("true_name", true_name);
                                                    intent.putExtra("gender", gender);
                                                    intent.putExtra("card", card);
                                                    intent.putExtra("birthday", birthday);
                                                    mApplication.startActivity(mActivity, intent, MyApplication.CODE_REGISTER_BIND);
                                                }
                                            })
                                            .setNegativeButton("注册账号", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(mActivity, RegisterEduActivity.class);
                                                    intent.putExtra("stu_id", username);
                                                    intent.putExtra("stu_pass", password);
                                                    intent.putExtra("college", college);
                                                    intent.putExtra("professional", professional);
                                                    intent.putExtra("classmate", classmate);
                                                    intent.putExtra("true_name", true_name);
                                                    intent.putExtra("gender", gender);
                                                    intent.putExtra("card", card);
                                                    intent.putExtra("birthday", birthday);
                                                    mApplication.startActivity(mActivity, intent, MyApplication.CODE_REGISTER_EDU);
                                                }
                                            })
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                ToastUtil.show(mActivity, "登录失败,请重试");
                                DialogUtil.cancel();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtil.show(mActivity, "登录失败,请重试");
                        DialogUtil.cancel();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "登录失败,请重试");
                DialogUtil.cancel();
            }
        });

    }

    private void getUserInfo() {

        if (TextUtil.isEmpty(mApplication.userTokenString)) {
            return;
        }

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "getInfo");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=getInfo", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isEmpty(mApplication.getJsonError(o.toString()))) {
                    String data = mApplication.getJsonData(o.toString());
                    if (TextUtil.isEmpty(data)) {
                        mApplication.userTokenString = "";
                        mApplication.userHashMap = new HashMap<>();
                        ToastUtil.show(mActivity, "系统检测到你的账户有变化，请重新登录");
                        mApplication.mSharedPreferencesEditor.putString("user_token", "").apply();
                    } else {
                        mApplication.userHashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(data));
                        if (mApplication.userHashMap.get("is_close").equals("1")) {
                            mApplication.userTokenString = "";
                            mApplication.userHashMap = new HashMap<>();
                            ToastUtil.show(mActivity, "系统检测到你的账户已停用");
                            mApplication.mSharedPreferencesEditor.putString("user_token", "").apply();
                        } else {
                            updateUserPush();
                        }
                    }
                } else {
                    mApplication.userTokenString = "";
                    mApplication.userHashMap = new HashMap<>();
                    ToastUtil.show(mActivity, "系统检测到你的账户有变化，请重新登录");
                    mApplication.mSharedPreferencesEditor.putString("user_token", "").apply();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                new MyCountTime(2000, 500) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getUserInfo();
                    }
                }.start();
            }
        });

    }

    private void updateUserPush() {

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "editPush");
        ajaxParams.put("push_id", JPushInterface.getRegistrationID(this));
        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=editPush", ajaxParams, null);
        AndroidUtil.hideKeyboard(loginTextView);
        ToastUtil.show(mActivity, "登录成功");
        mApplication.finishActivity(mActivity);

    }

    private void returnActivity() {

        new AlertDialog.Builder(mActivity)
                .setTitle("确认您的选择")
                .setMessage("取消登录?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AndroidUtil.hideKeyboard(backImageView);
                        mApplication.finishActivity(mActivity);
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

}