package top.yokey.gxsfxy.activity.dynamic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.CommentListAdapter;
import top.yokey.gxsfxy.control.CustomScrollView;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class ActivityDetailedActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String idString;
    private boolean praiseBoolean;
    private HashMap<String, String> mHashMap;

    private ImageView backImageView;
    private TextView titleTextView;

    private CustomScrollView mScrollView;
    private ImageView mImageView;
    private TextView nameTextView;
    private TextView stateTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView contentTextView;

    private ImageView shareImageView;
    private ImageView commentImageView;
    private ImageView praiseImageView;
    private EditText contentEditText;
    private ImageView faceImageView;
    private ImageView sendImageView;
    private LinearLayout faceLinearLayout;
    private ImageView[] faceImageViews;

    private String rid;
    private int pageInt;
    private RecyclerView mListView;
    private TextView commentTextView;
    private CommentListAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mArrayList;

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
        setContentView(R.layout.activity_activity_detailed);
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

        mScrollView = (CustomScrollView) findViewById(R.id.mainScrollView);
        mImageView = (ImageView) findViewById(R.id.mainImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);
        endTimeTextView = (TextView) findViewById(R.id.endTimeTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);

        shareImageView = (ImageView) findViewById(R.id.shareImageView);
        commentImageView = (ImageView) findViewById(R.id.commentImageView);
        praiseImageView = (ImageView) findViewById(R.id.praiseImageView);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
        commentTextView = (TextView) findViewById(R.id.commentTextView);
        faceImageView = (ImageView) findViewById(R.id.faceImageView);
        sendImageView = (ImageView) findViewById(R.id.sendImageView);
        faceLinearLayout = (LinearLayout) findViewById(R.id.faceLinearLayout);
        faceImageViews = new ImageView[48];
        faceImageViews[0] = (ImageView) findViewById(R.id.e000ImageView);
        faceImageViews[1] = (ImageView) findViewById(R.id.e001ImageView);
        faceImageViews[2] = (ImageView) findViewById(R.id.e002ImageView);
        faceImageViews[3] = (ImageView) findViewById(R.id.e003ImageView);
        faceImageViews[4] = (ImageView) findViewById(R.id.e004ImageView);
        faceImageViews[5] = (ImageView) findViewById(R.id.e005ImageView);
        faceImageViews[6] = (ImageView) findViewById(R.id.e006ImageView);
        faceImageViews[7] = (ImageView) findViewById(R.id.e007ImageView);
        faceImageViews[8] = (ImageView) findViewById(R.id.e008ImageView);
        faceImageViews[9] = (ImageView) findViewById(R.id.e009ImageView);
        faceImageViews[10] = (ImageView) findViewById(R.id.e010ImageView);
        faceImageViews[11] = (ImageView) findViewById(R.id.e011ImageView);
        faceImageViews[12] = (ImageView) findViewById(R.id.e012ImageView);
        faceImageViews[13] = (ImageView) findViewById(R.id.e013ImageView);
        faceImageViews[14] = (ImageView) findViewById(R.id.e014ImageView);
        faceImageViews[15] = (ImageView) findViewById(R.id.e015ImageView);
        faceImageViews[16] = (ImageView) findViewById(R.id.e016ImageView);
        faceImageViews[17] = (ImageView) findViewById(R.id.e017ImageView);
        faceImageViews[18] = (ImageView) findViewById(R.id.e018ImageView);
        faceImageViews[19] = (ImageView) findViewById(R.id.e019ImageView);
        faceImageViews[20] = (ImageView) findViewById(R.id.e020ImageView);
        faceImageViews[21] = (ImageView) findViewById(R.id.e021ImageView);
        faceImageViews[22] = (ImageView) findViewById(R.id.e022ImageView);
        faceImageViews[23] = (ImageView) findViewById(R.id.e023ImageView);
        faceImageViews[24] = (ImageView) findViewById(R.id.e024ImageView);
        faceImageViews[25] = (ImageView) findViewById(R.id.e025ImageView);
        faceImageViews[26] = (ImageView) findViewById(R.id.e026ImageView);
        faceImageViews[27] = (ImageView) findViewById(R.id.e027ImageView);
        faceImageViews[28] = (ImageView) findViewById(R.id.e028ImageView);
        faceImageViews[29] = (ImageView) findViewById(R.id.e029ImageView);
        faceImageViews[30] = (ImageView) findViewById(R.id.e030ImageView);
        faceImageViews[31] = (ImageView) findViewById(R.id.e031ImageView);
        faceImageViews[32] = (ImageView) findViewById(R.id.e032ImageView);
        faceImageViews[33] = (ImageView) findViewById(R.id.e033ImageView);
        faceImageViews[34] = (ImageView) findViewById(R.id.e034ImageView);
        faceImageViews[35] = (ImageView) findViewById(R.id.e035ImageView);
        faceImageViews[36] = (ImageView) findViewById(R.id.e036ImageView);
        faceImageViews[37] = (ImageView) findViewById(R.id.e037ImageView);
        faceImageViews[38] = (ImageView) findViewById(R.id.e038ImageView);
        faceImageViews[39] = (ImageView) findViewById(R.id.e039ImageView);
        faceImageViews[40] = (ImageView) findViewById(R.id.e040ImageView);
        faceImageViews[41] = (ImageView) findViewById(R.id.e041ImageView);
        faceImageViews[42] = (ImageView) findViewById(R.id.e042ImageView);
        faceImageViews[43] = (ImageView) findViewById(R.id.e043ImageView);
        faceImageViews[44] = (ImageView) findViewById(R.id.e044ImageView);
        faceImageViews[45] = (ImageView) findViewById(R.id.e045ImageView);
        faceImageViews[46] = (ImageView) findViewById(R.id.e046ImageView);
        faceImageViews[47] = (ImageView) findViewById(R.id.e047ImageView);

        mListView = (RecyclerView) findViewById(R.id.mainListView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        praiseBoolean = false;
        idString = mActivity.getIntent().getStringExtra("activity_id");
        if (TextUtil.isEmpty(idString)) {
            ToastUtil.show(mActivity, "参数错误");
            mApplication.finishActivity(mActivity);
            return;
        }

        titleTextView.setText("活动详细");

        rid = "-1";
        pageInt = 1;
        mArrayList = new ArrayList<>();
        mAdapter = new CommentListAdapter(mApplication, mActivity, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);

        faceLinearLayout.setVisibility(View.GONE);
        ControlUtil.setFocusable(contentEditText);
        getJson();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(mActivity, "活动不支持转发");
            }
        });

        praiseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtil.isEmpty(mApplication.userTokenString)) {
                    mApplication.startActivityLogin(mActivity);
                } else {
                    if (praiseBoolean) {
                        cancelPraise();
                    } else {
                        praise();
                    }
                }
            }
        });

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtil.isEmpty(mApplication.userTokenString)) {
                    mApplication.startActivityLogin(mActivity);
                } else {
                    comment();
                }
            }
        });

        faceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceLinearLayout.getVisibility() == View.GONE) {
                    AndroidUtil.hideKeyboard(view);
                    faceLinearLayout.setVisibility(View.VISIBLE);
                    faceImageView.setImageResource(R.mipmap.ic_input_keyboard_comment);
                } else {
                    faceLinearLayout.setVisibility(View.GONE);
                    faceImageView.setImageResource(R.mipmap.ic_input_face_comment);
                    AndroidUtil.showKeyboard(view);
                }
            }
        });

        contentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentEditText.isFocusable()) {
                    AndroidUtil.showKeyboard(view);
                    faceLinearLayout.setVisibility(View.GONE);
                    faceImageView.setImageResource(R.mipmap.ic_input_face_comment);
                }
            }
        });

        mScrollView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (!rid.equals("-1")) {
                    rid = "-1";
                    contentEditText.setHint("请输入内容");
                    AndroidUtil.hideKeyboard(contentEditText);
                }
            }
        });

        mAdapter.setOnItemClickListener(new CommentListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String comment_id, String nick_name) {
                rid = comment_id;
                String hint = "回复：" + nick_name;
                contentEditText.setHint(hint);
                ControlUtil.setFocusable(contentEditText);
                if (faceLinearLayout.getVisibility() == View.GONE) {
                    AndroidUtil.showKeyboard(contentEditText);
                }
            }
        });

        for (int i = 0; i < faceImageViews.length; i++) {
            final String sNum;
            if (i < 10) {
                sNum = "e00" + i;
            } else {
                sNum = "e0" + i;
            }
            faceImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = TextUtil.replaceFace(contentEditText.getText()) + "<img src=\"" + sNum + "\">";
                    contentEditText.setText(Html.fromHtml(content, mApplication.mImageGetter, null));
                    contentEditText.setSelection(contentEditText.getText().length());
                }
            });
        }

    }

    private void getJson() {

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "activityDetailed");
        ajaxParams.put("id", idString);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=activityDetailed", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                String data = mApplication.getJsonData(o.toString());
                if (TextUtil.isEmpty(data)) {
                    ToastUtil.show(mActivity, "活动不存在或已删除");
                    returnActivity();
                } else {
                    mHashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(data));
                    ImageLoader.getInstance().displayImage(mHashMap.get("activity_image"), mImageView);
                    nameTextView.setText(mHashMap.get("activity_name"));
                    String temp = "活动状态：<font color='red'>审核中</font>";
                    switch (mHashMap.get("activity_state")) {
                        case "0":
                            temp = "活动状态：<font color='red'>审核中</font>";
                            break;
                        case "1":
                            temp = "活动状态：<font color='red'>进行中</font>";
                            break;
                        case "2":
                            temp = "活动状态：<font color='red'>已结束</font>";
                    }
                    stateTextView.setText(Html.fromHtml(temp));
                    temp = "开始时间：" + mHashMap.get("activity_start_time");
                    startTimeTextView.setText(temp);
                    temp = "结束时间：" + mHashMap.get("activity_end_time");
                    endTimeTextView.setText(temp);
                    contentTextView.setText(Html.fromHtml(mHashMap.get("activity_content")));
                    getComment();
                    isComment();
                    isPraise();
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

    private void getComment() {

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "commentList");
        ajaxParams.put("table", "activity");
        ajaxParams.put("tid", mHashMap.get("activity_id"));
        ajaxParams.put("page", pageInt + "");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=commentList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isJson(o.toString())) {
                    try {
                        String data = mApplication.getJsonData(o.toString());
                        if (data.equals("[]")) {
                            if (pageInt != 1) {
                                ToastUtil.show(mActivity, "没有评论了");
                            } else {
                                commentTextView.setVisibility(View.GONE);
                            }
                        } else {
                            if (pageInt == 1) {
                                mArrayList.clear();
                            }
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            commentTextView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getCommentFailure();
                    }
                } else {
                    getCommentFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getCommentFailure();
            }
        });

    }

    private void getCommentFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog
                    .Builder(mActivity)
                    .setCancelable(false)
                    .setTitle("是否重试?")
                    .setMessage("读取数据失败")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getCommentFailure();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

    }

    private void isComment() {

        if (TextUtil.isEmpty(mApplication.userTokenString)) {
            return;
        }

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userComment", "isComment");
        ajaxParams.put("table", "activity");
        ajaxParams.put("tid", mHashMap.get("activity_id"));

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userComment&a=isComment", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (mApplication.getJsonSuccess(o.toString())) {
                    commentImageView.setImageResource(R.mipmap.ic_tool_comment_press);
                } else {
                    commentImageView.setImageResource(R.mipmap.ic_tool_comment);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                new MyCountTime(2000, 1000) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        isPraise();
                    }
                }.start();
            }
        });

    }

    private void isPraise() {

        if (TextUtil.isEmpty(mApplication.userTokenString)) {
            return;
        }

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userPraise", "isPraise");
        ajaxParams.put("table", "activity");
        ajaxParams.put("tid", mHashMap.get("activity_id"));

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userPraise&a=isPraise", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (mApplication.getJsonSuccess(o.toString())) {
                    praiseBoolean = true;
                    praiseImageView.setImageResource(R.mipmap.ic_tool_praise_press);
                } else {
                    praiseBoolean = false;
                    praiseImageView.setImageResource(R.mipmap.ic_tool_praise);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                new MyCountTime(2000, 1000) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        isPraise();
                    }
                }.start();
            }
        });

    }

    private void comment() {

        if (TextUtil.isEmpty(contentEditText.getText().toString())) {
            ControlUtil.setFocusable(contentEditText);
            if (faceLinearLayout.getVisibility() == View.GONE) {
                AndroidUtil.showKeyboard(contentEditText);
            }
        } else {
            contentEditText.setEnabled(false);
            AndroidUtil.hideKeyboard(contentEditText);
            UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userComment", "commentActivity");
            ajaxParams.put("rid", rid);
            ajaxParams.put("activity_id", mHashMap.get("activity_id"));
            ajaxParams.put("content", TextUtil.replaceFace(contentEditText.getText()));
            mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userComment&a=commentActivity", ajaxParams, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    contentEditText.setEnabled(true);
                    if (mApplication.getJsonSuccess(o.toString())) {
                        commentImageView.setImageResource(R.mipmap.ic_tool_comment);
                        ToastUtil.showSuccess(mActivity);
                        contentEditText.setText("");
                        pageInt = 1;
                        getComment();
                    } else {
                        ToastUtil.showFailure(mActivity);
                    }
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    ToastUtil.showFailure(mActivity);
                    contentEditText.setEnabled(true);
                }
            });
        }

    }

    private void praise() {

        praiseBoolean = true;
        praiseImageView.setImageResource(R.mipmap.ic_tool_praise_press);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userPraise", "praiseActivity");
        ajaxParams.put("activity_id", mHashMap.get("activity_id"));
        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userPraise&a=praiseActivity", ajaxParams, null);

    }

    private void cancelPraise() {

        praiseBoolean = false;
        praiseImageView.setImageResource(R.mipmap.ic_tool_praise);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userPraise", "cancelActivity");
        ajaxParams.put("activity_id", mHashMap.get("activity_id"));
        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userPraise&a=cancelActivity", ajaxParams, null);

    }

    private void returnActivity() {

        if (faceLinearLayout.getVisibility() == View.VISIBLE) {
            AndroidUtil.hideKeyboard(contentEditText);
            faceLinearLayout.setVisibility(View.GONE);
            faceImageView.setImageResource(R.mipmap.ic_input_face_comment);
        } else {
            mApplication.finishActivity(mActivity);
        }

    }

}