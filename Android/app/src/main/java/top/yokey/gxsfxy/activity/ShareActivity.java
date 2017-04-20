package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.share.OnekeyShare;
import top.yokey.gxsfxy.utility.QRCodeUtil;

public class ShareActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String link;
    private String name;
    private String jingle;
    private String image;
    private OnekeyShare mOnekeyShare;

    private ImageView backImageView;
    private TextView titleTextView;

    private ImageView qrCodeImageView;
    private TextView otherTextView;

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
        setContentView(R.layout.activity_share);
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

        qrCodeImageView = (ImageView) findViewById(R.id.qrCodeImageView);
        otherTextView = (TextView) findViewById(R.id.otherTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        link = mActivity.getIntent().getStringExtra("link");
        name = mActivity.getIntent().getStringExtra("name");
        jingle = mActivity.getIntent().getStringExtra("jingle");
        image = mActivity.getIntent().getStringExtra("image");

        titleTextView.setText(mActivity.getIntent().getStringExtra("title"));

        qrCodeImageView.setImageBitmap(QRCodeUtil.create(link, 512, 512));

        mOnekeyShare = new OnekeyShare();
        mOnekeyShare.disableSSOWhenAuthorize();
        mOnekeyShare.setTitle(name);
        mOnekeyShare.setText(jingle);
        mOnekeyShare.setImageUrl(image);
        mOnekeyShare.setUrl(link);
        mOnekeyShare.setTitleUrl(link);
        mOnekeyShare.show(mActivity);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
            }
        });

        otherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnekeyShare = new OnekeyShare();
                mOnekeyShare.disableSSOWhenAuthorize();
                mOnekeyShare.setTitle(name);
                mOnekeyShare.setText(jingle);
                mOnekeyShare.setImageUrl(image);
                mOnekeyShare.setUrl(link);
                mOnekeyShare.setTitleUrl(link);
                mOnekeyShare.show(mActivity);
            }
        });

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}