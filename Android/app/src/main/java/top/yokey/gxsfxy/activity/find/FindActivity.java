package top.yokey.gxsfxy.activity.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.utility.ToastUtil;

public class FindActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private TextView shopTextView;
    private TextView friendTextView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_find);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        shopTextView = (TextView) findViewById(R.id.shopTextView);
        friendTextView = (TextView) findViewById(R.id.friendTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

    }

    private void initEven() {

        shopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(MainActivity.mActivity, "开发中...");
            }
        });

        friendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.startActivityWithLoginSuccess(mActivity, new Intent(mApplication, FriendActivity.class));
            }
        });

    }

}