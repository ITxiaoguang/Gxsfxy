package top.yokey.gxsfxy.activity.admin;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.TextUtil;

public class AdminMainActivity extends ActivityGroup {

    public static Activity mActivity;
    public static MyApplication mApplication;

    private int[][] mInt;
    private TextView[] mTextView;
    private static TabHost mTabHost;

    private TextView titleTextView;
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
        setContentView(R.layout.activity_admin_main);
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

        mTabHost = (TabHost) findViewById(R.id.mainTabHost);
        mTextView = new TextView[4];
        mTextView[0] = (TextView) findViewById(R.id.homeTextView);
        mTextView[1] = (TextView) findViewById(R.id.userTextView);
        mTextView[2] = (TextView) findViewById(R.id.feedbackTextView);
        mTextView[3] = (TextView) findViewById(R.id.otherTextView);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();
        titleTextView.setText(mApplication.userHashMap.get("user_power"));
        titleTextView.append(" - ");
        if (TextUtil.isEmpty(mApplication.userHashMap.get("true_name"))) {
            titleTextView.append(mApplication.userHashMap.get("nick_name"));
        } else {
            titleTextView.append(mApplication.userHashMap.get("true_name"));
        }

        mInt = new int[2][4];
        mInt[0][0] = R.mipmap.ic_nav_main_home;
        mInt[0][1] = R.mipmap.ic_nav_main_mine;
        mInt[0][2] = R.mipmap.ic_nav_main_message;
        mInt[0][3] = R.mipmap.ic_nav_main_find;
        mInt[1][0] = R.mipmap.ic_nav_main_home_press;
        mInt[1][1] = R.mipmap.ic_nav_main_mine_press;
        mInt[1][2] = R.mipmap.ic_nav_main_message_press;
        mInt[1][3] = R.mipmap.ic_nav_main_find_press;

        mTabHost.setup(this.getLocalActivityManager());
        mTabHost.addTab(mTabHost.newTabSpec("Home").setIndicator("Home").setContent(new Intent(mActivity, AdminHomeActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("User").setIndicator("User").setContent(new Intent(mActivity, AdminUserActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Feedback").setIndicator("Feedback").setContent(new Intent(mActivity, AdminFeedbackActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Other").setIndicator("Other").setContent(new Intent(mActivity, AdminOtherActivity.class)));
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
                    case "User":
                        updateTab(1);
                        break;
                    case "Feedback":
                        updateTab(2);
                        break;
                    case "Find":
                        updateTab(3);
                        break;
                }
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private void returnActivity() {

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "admin", "configUpdate");
        ajaxParams.put("config_type", "public");
        ajaxParams.put("config_name", "admin_news_pos");
        ajaxParams.put("config_value", mApplication.adminNewsPosInt + "");
        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=admin&a=configUpdate", ajaxParams, null);
        mApplication.finishActivity(mActivity);
        mActivity = null;

    }

}