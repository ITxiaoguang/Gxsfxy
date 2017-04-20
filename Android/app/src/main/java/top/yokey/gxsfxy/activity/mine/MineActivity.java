package top.yokey.gxsfxy.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.QRCodeUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class MineActivity extends AppCompatActivity {

    private RelativeLayout loginRelativeLayout;
    private TextView loginTextView;

    private RelativeLayout mineRelativeLayout;
    private ImageView avatarImageView;
    private TextView nicknameTextView;
    private ImageView genderImageView;
    private ImageView qrCodeImageView;

    private TextView mineFollowTextView;
    private TextView followMineTextView;
    private TextView visitorTextView;
    private TextView praiseTextView;
    private TextView dynamicTextView;
    private TextView commentTextView;

    private LinearLayout pointsLinearLayout;
    private TextView pointsTextView;

    private TextView feedbackTextView;
    private TextView settingTextView;
    private TextView helpTextView;
    private TextView protocolTextView;
    private TextView aboutTextView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mine);
        initView();
        initEven();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }


    private void initView() {

        loginRelativeLayout = (RelativeLayout) findViewById(R.id.loginRelativeLayout);
        loginTextView = (TextView) findViewById(R.id.loginTextView);

        mineRelativeLayout = (RelativeLayout) findViewById(R.id.mineRelativeLayout);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
        genderImageView = (ImageView) findViewById(R.id.genderImageView);
        qrCodeImageView = (ImageView) findViewById(R.id.qrCodeImageView);

        mineFollowTextView = (TextView) findViewById(R.id.mineFollowTextView);
        followMineTextView = (TextView) findViewById(R.id.followMineTextView);
        visitorTextView = (TextView) findViewById(R.id.visitorTextView);
        dynamicTextView = (TextView) findViewById(R.id.dynamicTextView);
        commentTextView = (TextView) findViewById(R.id.commentTextView);
        praiseTextView = (TextView) findViewById(R.id.praiseTextView);

        pointsLinearLayout = (LinearLayout) findViewById(R.id.pointsLinearLayout);
        pointsTextView = (TextView) findViewById(R.id.pointsTextView);

        feedbackTextView = (TextView) findViewById(R.id.feedbackTextView);
        settingTextView = (TextView) findViewById(R.id.settingTextView);
        helpTextView = (TextView) findViewById(R.id.helpTextView);
        protocolTextView = (TextView) findViewById(R.id.protocolTextView);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);

    }


    private void initData() {

        if (!TextUtil.isEmpty(MainActivity.mApplication.userTokenString)) {
            if (loginRelativeLayout.getVisibility() == View.VISIBLE) {
                loginRelativeLayout.setVisibility(View.GONE);
            }
            avatarImageView.setImageResource(R.mipmap.ic_avatar);
        } else {
            if (loginRelativeLayout.getVisibility() == View.GONE) {
                loginRelativeLayout.setVisibility(View.VISIBLE);
                return;
            }
        }

        //个人信息处理
        if (!MainActivity.mApplication.userHashMap.isEmpty()) {
            if (TextUtil.isEmpty(MainActivity.mApplication.userHashMap.get("user_avatar"))) {
                avatarImageView.setImageResource(R.mipmap.ic_avatar);
            } else {
                ImageLoader.getInstance().displayImage(MainActivity.mApplication.userHashMap.get("user_avatar"), avatarImageView);
            }
            if (MainActivity.mApplication.userHashMap.get("user_gender").equals("男")) {
                genderImageView.setImageResource(R.mipmap.ic_default_boy);
            } else {
                genderImageView.setImageResource(R.mipmap.ic_default_girl);
            }
            nicknameTextView.setText(MainActivity.mApplication.userHashMap.get("nick_name"));
            String temp = MainActivity.mApplication.userHashMap.get("user_follow") + "<br><font color='#999999'>我关注的</font>";
            mineFollowTextView.setText(Html.fromHtml(temp));
            temp = MainActivity.mApplication.userHashMap.get("follow_mine") + "<br><font color='#999999'>关注我的</font>";
            followMineTextView.setText(Html.fromHtml(temp));
            temp = MainActivity.mApplication.userHashMap.get("user_visitor") + "<br><font color='#999999'>访问次数</font>";
            visitorTextView.setText(Html.fromHtml(temp));
            temp = MainActivity.mApplication.userHashMap.get("dynamic_number") + "<br><font color='#999999'>条动态</font>";
            dynamicTextView.setText(Html.fromHtml(temp));
            temp = MainActivity.mApplication.userHashMap.get("comment_number") + "<br><font color='#999999'>条评论</font>";
            commentTextView.setText(Html.fromHtml(temp));
            temp = MainActivity.mApplication.userHashMap.get("praise_number") + "<br><font color='#999999'>我赞的</font>";
            praiseTextView.setText(Html.fromHtml(temp));
            pointsTextView.setText(MainActivity.mApplication.userHashMap.get("user_points"));
        }

    }


    private void initEven() {

        loginRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityLogin(MainActivity.mActivity);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityLogin(MainActivity.mActivity);
            }
        });

        mineRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityWithLoginSuccess(
                        MainActivity.mActivity,
                        new Intent(MainActivity.mActivity, MineCenterActivity.class)
                );
            }
        });

        qrCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mApplication.userHashMap.isEmpty()) {
                    MainActivity.mApplication.startActivityLogin(MainActivity.mActivity);
                } else {
                    String content = "[uid:" + MainActivity.mApplication.userHashMap.get("user_id") + "]";
                    DialogUtil.qrCode(MainActivity.mActivity, "扫描二维码关注我吧!", QRCodeUtil.create(content, 512, 512));
                }
            }
        });

        mineFollowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, MineFollowActivity.class);
                intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        followMineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, FollowMineActivity.class);
                intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        visitorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, UserVisitorActivity.class);
                intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        dynamicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, UserDynamicActivity.class);
                intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, UserCommentActivity.class);
                intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        praiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, UserPraiseActivity.class);
                intent.putExtra("user_id", MainActivity.mApplication.userHashMap.get("user_id"));
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        pointsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityWithLoginSuccess(
                        MainActivity.mActivity,
                        new Intent(MainActivity.mActivity, PointsActivity.class)
                );
            }
        });

        feedbackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityWithLoginSuccess(
                        MainActivity.mActivity,
                        new Intent(MainActivity.mActivity, FeedbackActivity.class)
                );
            }
        });

        settingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityWithLoginSuccess(
                        MainActivity.mActivity,
                        new Intent(MainActivity.mActivity, SettingActivity.class)
                );
            }
        });

        helpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, MainActivity.mApplication.publicHtmlMobileUrlString + "help.html");
            }
        });

        protocolTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, MainActivity.mApplication.publicHtmlMobileUrlString + "protocol.html");
            }
        });

        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, MainActivity.mApplication.publicHtmlMobileUrlString + "about.html");
            }
        });

    }

}