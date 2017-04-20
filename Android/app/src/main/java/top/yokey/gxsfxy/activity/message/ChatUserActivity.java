package top.yokey.gxsfxy.activity.message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import top.yokey.gxsfxy.activity.mine.FollowMineActivity;
import top.yokey.gxsfxy.activity.mine.MineFollowActivity;
import top.yokey.gxsfxy.activity.mine.UserCommentActivity;
import top.yokey.gxsfxy.activity.mine.UserDynamicActivity;
import top.yokey.gxsfxy.activity.mine.UserPraiseActivity;
import top.yokey.gxsfxy.activity.mine.UserVisitorActivity;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class ChatUserActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String user_id;

    private ImageView backImageView;
    private TextView titleTextView;

    private ImageView avatarImageView;
    private TextView nicknameTextView;
    private ImageView genderImageView;
    private TextView collegeTextView;
    private TextView signTextView;

    private TextView mineFollowTextView;
    private TextView followMineTextView;
    private TextView visitorTextView;
    private TextView dynamicTextView;
    private TextView commentTextView;
    private TextView praiseTextView;

    private TextView delTextView;
    private TextView followTextView;

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
        setContentView(R.layout.activity_chat_user);
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

        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
        genderImageView = (ImageView) findViewById(R.id.genderImageView);
        collegeTextView = (TextView) findViewById(R.id.collegeTextView);
        signTextView = (TextView) findViewById(R.id.signTextView);

        mineFollowTextView = (TextView) findViewById(R.id.mineFollowTextView);
        followMineTextView = (TextView) findViewById(R.id.followMineTextView);
        visitorTextView = (TextView) findViewById(R.id.visitorTextView);
        dynamicTextView = (TextView) findViewById(R.id.dynamicTextView);
        commentTextView = (TextView) findViewById(R.id.commentTextView);
        praiseTextView = (TextView) findViewById(R.id.praiseTextView);

        delTextView = (TextView) findViewById(R.id.delTextView);
        followTextView = (TextView) findViewById(R.id.followTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        user_id = mActivity.getIntent().getStringExtra("user_id");

        if (TextUtil.isEmpty(user_id)) {
            ToastUtil.show(mActivity, "参数错误");
            returnActivity();
            return;
        }

        titleTextView.setText("详细资料");

        getJson();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        mineFollowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MineFollowActivity.class);
                intent.putExtra("user_id", user_id);
                MainActivity.mApplication.startActivityWithLoginSuccess(mActivity, intent);
            }
        });

        followMineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, FollowMineActivity.class);
                intent.putExtra("user_id", user_id);
                mApplication.startActivityWithLoginSuccess(mActivity, intent);
            }
        });

        visitorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.mActivity, UserVisitorActivity.class);
                intent.putExtra("user_id", user_id);
                MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
            }
        });

        dynamicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, UserDynamicActivity.class);
                intent.putExtra("user_id", user_id);
                mApplication.startActivityWithLoginSuccess(mActivity, intent);
            }
        });

        commentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, UserCommentActivity.class);
                intent.putExtra("user_id", user_id);
                mApplication.startActivityWithLoginSuccess(mActivity, intent);
            }
        });

        praiseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, UserPraiseActivity.class);
                intent.putExtra("user_id", user_id);
                mApplication.startActivityWithLoginSuccess(mActivity, intent);
            }
        });

        delTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mActivity).setTitle("确认您的选择").setMessage("删除您与对方的聊天记录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DialogUtil.progress(mActivity);
                                UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userMessage", "messageClear");
                                ajaxParams.put("user_id", user_id);
                                mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userMessage&a=messageClear", ajaxParams, new AjaxCallBack<Object>() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        super.onSuccess(o);
                                        DialogUtil.cancel();
                                        if (mApplication.getJsonSuccess(o.toString())) {
                                            ToastUtil.showSuccess(mActivity);
                                            mActivity.setResult(RESULT_OK);
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
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("确认您的选择")
                        .setMessage("不在关注他/她")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DialogUtil.progress(mActivity);
                                UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userFollow", "followCancel");
                                ajaxParams.put("user_id", user_id);
                                mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userFollow&a=followCancel", ajaxParams, new AjaxCallBack<Object>() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        super.onSuccess(o);
                                        DialogUtil.cancel();
                                        if (mApplication.getJsonSuccess(o.toString())) {
                                            mActivity.setResult(RESULT_OK);
                                            ToastUtil.show(mActivity, "取消关注成功");
                                            mApplication.finishActivity(mActivity);
                                        } else {
                                            ToastUtil.show(mActivity, mApplication.getJsonError(o.toString()));
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
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }

    private void getJson() {

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userCenter", "userCenter");
        ajaxParams.put("user_id", user_id);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userCenter&a=userCenter", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(mApplication.getJsonData(o.toString()));
                    if (TextUtil.isEmpty(jsonObject.getString("user_avatar"))) {
                        avatarImageView.setImageResource(R.mipmap.ic_avatar);
                    } else {
                        ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), avatarImageView);
                    }
                    nicknameTextView.setText(jsonObject.getString("nick_name"));
                    if (jsonObject.getString("user_gender").equals("男")) {
                        genderImageView.setImageResource(R.mipmap.ic_default_boy);
                    } else {
                        genderImageView.setImageResource(R.mipmap.ic_default_girl);
                    }
                    if (TextUtil.isEmpty(jsonObject.getString("user_college"))) {
                        collegeTextView.setText("尚未绑定教务系统账号");
                    } else {
                        collegeTextView.setText(jsonObject.getString("user_college"));
                    }
                    if (TextUtil.isEmpty(jsonObject.getString("user_sign"))) {
                        signTextView.setText("他很懒，什么都没留下。。。");
                    } else {
                        signTextView.setText(jsonObject.getString("user_sign"));
                    }
                    String temp = jsonObject.getString("user_follow") + "<br><font color='#999999'>他关注的</font>";
                    mineFollowTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("follow_mine") + "<br><font color='#999999'>关注他的</font>";
                    followMineTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("user_visitor") + "<br><font color='#999999'>访问次数</font>";
                    visitorTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("dynamic_number") + "<br><font color='#999999'>条动态</font>";
                    dynamicTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("comment_number") + "<br><font color='#999999'>条评论</font>";
                    commentTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("praise_number") + "<br><font color='#999999'>他赞过的</font>";
                    praiseTextView.setText(Html.fromHtml(temp));
                    if (!jsonObject.getString("is_follow").equals("true")) {
                        mActivity.setResult(RESULT_OK);
                        mApplication.finishActivity(mActivity);
                        ToastUtil.show(mActivity, "你们没有互相关注");
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
                    .setTitle("是否重试?")
                    .setMessage("读取数据失败")
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