package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.admin.AdminMainActivity;
import top.yokey.gxsfxy.activity.dynamic.DynamicActivity;
import top.yokey.gxsfxy.activity.find.FindActivity;
import top.yokey.gxsfxy.activity.home.HomeActivity;
import top.yokey.gxsfxy.activity.message.MessageActivity;
import top.yokey.gxsfxy.activity.mine.MineActivity;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class MainActivity extends CheckActivity {

    public static Activity mActivity;
    public static MyApplication mApplication;

    private int[][] mInt;
    private long firstTime = 0;
    private TextView[] mTextView;
    private static TabHost mTabHost;

    private TextView titleTextView;
    private ImageView scanImageView;
    private ImageView searchImageView;

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
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEven();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        getUserInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {

        mTabHost = (TabHost) findViewById(R.id.mainTabHost);
        mTextView = new TextView[5];
        mTextView[0] = (TextView) findViewById(R.id.homeTextView);
        mTextView[1] = (TextView) findViewById(R.id.messageTextView);
        mTextView[2] = (TextView) findViewById(R.id.dynamicTextView);
        mTextView[3] = (TextView) findViewById(R.id.findTextView);
        mTextView[4] = (TextView) findViewById(R.id.mineTextView);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        scanImageView = (ImageView) findViewById(R.id.scanImageView);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        mInt = new int[2][5];
        mInt[0][0] = R.mipmap.ic_nav_main_home;
        mInt[0][1] = R.mipmap.ic_nav_main_message;
        mInt[0][2] = R.mipmap.ic_nav_main_dynamic;
        mInt[0][3] = R.mipmap.ic_nav_main_find;
        mInt[0][4] = R.mipmap.ic_nav_main_mine;
        mInt[1][0] = R.mipmap.ic_nav_main_home_press;
        mInt[1][1] = R.mipmap.ic_nav_main_message_press;
        mInt[1][2] = R.mipmap.ic_nav_main_dynamic_press;
        mInt[1][3] = R.mipmap.ic_nav_main_find_press;
        mInt[1][4] = R.mipmap.ic_nav_main_mine_press;

        mTabHost.setup(this.getLocalActivityManager());
        mTabHost.addTab(mTabHost.newTabSpec("Home").setIndicator("Home").setContent(new Intent(mActivity, HomeActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Message").setIndicator("Message").setContent(new Intent(mActivity, MessageActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Dynamic").setIndicator("Dynamic").setContent(new Intent(mActivity, DynamicActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Find").setIndicator("Find").setContent(new Intent(mActivity, FindActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Mine").setIndicator("Mine").setContent(new Intent(mActivity, MineActivity.class)));
        mTabHost.setCurrentTab(4);
        mTabHost.setCurrentTab(3);
        mTabHost.setCurrentTab(2);
        mTabHost.setCurrentTab(1);
        mTabHost.setCurrentTab(0);

    }

    private void initEven() {

        for (int i = 0; i < mTextView.length; i++) {
            final int pos = i;
            mTextView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTab(pos);
                }
            });
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "Home":
                        updateTab(0);
                        break;
                    case "Message":
                        updateTab(1);
                        break;
                    case "Dynamic":
                        updateTab(2);
                        break;
                    case "Find":
                        updateTab(3);
                        break;
                    case "Mine":
                        updateTab(4);
                        break;
                }
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mApplication.userHashMap.isEmpty()) {
                    return;
                }
                if (mApplication.userHashMap.get("user_power").equals("用户")) {
                    return;
                }
                mApplication.startActivityWithLoginSuccess(mActivity, new Intent(mActivity, AdminMainActivity.class));
            }
        });

        scanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, ScanActivity.class));
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, SearchActivity.class));
            }
        });

    }

    private void updateTab(int i) {

        mTabHost.setCurrentTab(i);

        for (int j = 0; j < mTextView.length; j++) {
            mTextView[j].setTextColor(ContextCompat.getColor(mActivity, R.color.textNormal));
            mTextView[j].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, mInt[0][j]), null, null);
        }

        mTextView[i].setTextColor(ContextCompat.getColor(mActivity, R.color.textPress));
        mTextView[i].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, mInt[1][i]), null, null);

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
                        mApplication.startActivityLogin(mActivity);
                    } else {
                        mApplication.userHashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(data));
                        if (mApplication.userHashMap.get("is_close").equals("1")) {
                            mApplication.userTokenString = "";
                            mApplication.userHashMap = new HashMap<>();
                            ToastUtil.show(mActivity, "系统检测到你的账户已停用，请重新登录");
                            mApplication.mSharedPreferencesEditor.putString("user_token", "").apply();
                            mApplication.startActivityLogin(mActivity);
                        } else {
                            updateUserPush();
                        }
                    }
                } else {
                    mApplication.userTokenString = "";
                    mApplication.userHashMap = new HashMap<>();
                    ToastUtil.show(mActivity, "系统检测到你的账户有变化，请重新登录");
                    mApplication.mSharedPreferencesEditor.putString("user_token", "").apply();
                    mApplication.startActivityLogin(mActivity);
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

    }

    private void returnActivity() {

        if (mTabHost.getCurrentTab() != 0) {
            mTabHost.setCurrentTab(0);
            return;
        }

        long secondTime = System.currentTimeMillis();

        if (secondTime - firstTime > 2000) {
            ToastUtil.show(mActivity, "再按一次退出程序...");
            firstTime = secondTime;
        } else {
            if (TextUtil.isEmpty(mApplication.userTokenString)) {
                mApplication.finishApp();
                mApplication.finishActivity(mActivity);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            } else {
                UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "changeLoginState");
                ajaxParams.put("state", "0");
                mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=changeLoginState", ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        mApplication.finishApp();
                        mApplication.finishActivity(mActivity);
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtil.show(mActivity, "请重试");
                    }
                });
            }
        }

    }

}