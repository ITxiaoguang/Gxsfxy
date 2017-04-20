package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class BindEduActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private EditText stuIdEditText;
    private EditText stuPassEditText;
    private TextView bindTextView;

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
        setContentView(R.layout.activity_bind_edu);
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

        stuIdEditText = (EditText) findViewById(R.id.stuIdEditText);
        stuPassEditText = (EditText) findViewById(R.id.stuPassEditText);
        bindTextView = (TextView) findViewById(R.id.bindTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("绑定教务系统账号");
        if (!TextUtil.isEmpty(mApplication.userHashMap.get("stu_id"))) {
            stuIdEditText.setText(mApplication.userHashMap.get("stu_id"));
            stuIdEditText.setSelection(mApplication.userHashMap.get("stu_id").length());
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

        bindTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
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

    private void login() {

        DialogUtil.progress(mActivity, "正在登录教务系统");

        final String username = stuIdEditText.getText().toString();
        final String password = stuPassEditText.getText().toString();

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
                if (!o.toString().contains("学籍卡片")) {
                    ToastUtil.show(mActivity, "教务系统账号或密码错误");
                    DialogUtil.cancel();
                    return;
                }
                //读取个人信息
                mApplication.eduFinalHttp.get(mApplication.eduInfoUrlString, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        String temp = o.toString().replace("&nbsp;", "");
                        temp = temp.substring(temp.indexOf("院系：") + 3, temp.length());
                        String college = temp.substring(0, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("专业：") + 3, temp.length());
                        String professional = temp.substring(0, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("班级：") + 3, temp.length());
                        String classmate = temp.substring(0, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("姓名</td>") + 7, temp.length());
                        String true_name = temp.substring(temp.indexOf(">") + 1, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("性别</td>") + 7, temp.length());
                        String gender = temp.substring(temp.indexOf(">") + 1, temp.indexOf("</td>"));
                        temp = temp.substring(temp.indexOf("编号</td>") + 7, temp.length());
                        String card = temp.substring(temp.indexOf(">") + 1, temp.indexOf("</td>"));
                        String birthday = card.substring(6, 14);
                        //跟服务器对接
                        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "bindEdu");
                        ajaxParams.put("stu_id", username);
                        ajaxParams.put("stu_pass", password);
                        ajaxParams.put("college", college);
                        ajaxParams.put("professional", professional);
                        ajaxParams.put("classmate", classmate);
                        ajaxParams.put("true_name", true_name);
                        ajaxParams.put("gender", gender);
                        ajaxParams.put("card", card);
                        ajaxParams.put("birthday", birthday);
                        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=bindEdu", ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                DialogUtil.cancel();
                                if (mApplication.getJsonSuccess(o.toString())) {
                                    ToastUtil.show(mActivity, "绑定成功");
                                    mApplication.userHashMap.put("stu_id", username);
                                    mApplication.userHashMap.put("stu_pass", password);
                                    mActivity.setResult(RESULT_OK);
                                    mApplication.finishActivity(mActivity);
                                } else {
                                    ToastUtil.show(mActivity, mApplication.getJsonError(o.toString()));
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

    private void returnActivity() {

        new AlertDialog.Builder(mActivity)
                .setTitle("确认您的选择")
                .setMessage("取消绑定教务系统账号?")
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