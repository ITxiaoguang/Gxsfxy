package top.yokey.gxsfxy.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.AdminFeedbackDetailedListAdapter;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.FileUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class AdminFeedbackDetailedActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String user_id;
    private File imageFile;
    private String imagePath;

    private ImageView backImageView;
    private TextView titleTextView;

    private ImageView voiceImageView;
    private ImageView faceImageView;
    private ImageView locationImageView;
    private ImageView imageImageView;
    private ImageView cameraImageView;
    private EditText contentEditText;
    private ImageView sendImageView;
    private LinearLayout faceLinearLayout;
    private ImageView[] faceImageViews;

    private TextView tipsTextView;
    private TextView stateTextView;
    private RecyclerView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AdminFeedbackDetailedListAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mArrayList;

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == RESULT_OK) {
            switch (req) {
                case MyApplication.CODE_CHOOSE_PHOTO:
                    imagePath = AndroidUtil.getMediaPath(mActivity, data.getData());
                    mApplication.startPhotoCrop(mActivity, imagePath);
                    break;
                case MyApplication.CODE_CHOOSE_CAMERA:
                    mApplication.startPhotoCrop(mActivity, imagePath);
                    break;
                case MyApplication.CODE_CHOOSE_PHOTO_CROP:
                    imagePath = FileUtil.createJpgByBitmap("admin_feedback", mApplication.mBitmap);
                    imageFile = new File(imagePath);
                    sendImage();
                    break;
                default:
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
        setContentView(R.layout.activity_feedback);
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

        voiceImageView = (ImageView) findViewById(R.id.voiceImageView);
        faceImageView = (ImageView) findViewById(R.id.faceImageView);
        locationImageView = (ImageView) findViewById(R.id.locationImageView);
        imageImageView = (ImageView) findViewById(R.id.imageImageView);
        cameraImageView = (ImageView) findViewById(R.id.cameraImageView);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
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

        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        imageFile = null;
        imagePath = "null";

        user_id = mActivity.getIntent().getStringExtra("user_id");
        if (TextUtil.isEmpty(user_id)) {
            ToastUtil.show(mActivity, "参数不正确");
            mApplication.finishActivity(mActivity);
            return;
        }

        mArrayList = new ArrayList<>();
        mAdapter = new AdminFeedbackDetailedListAdapter(mApplication, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);

        ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout);
        locationImageView.setVisibility(View.GONE);
        faceLinearLayout.setVisibility(View.GONE);
        ControlUtil.setFocusable(contentEditText);
        stateTextView.setVisibility(View.GONE);
        titleTextView.setText("意见建议");
        getFeedback();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        voiceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(mActivity, "开发中");
            }
        });

        faceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceLinearLayout.getVisibility() == View.GONE) {
                    AndroidUtil.hideKeyboard(view);
                    faceLinearLayout.setVisibility(View.VISIBLE);
                    faceImageView.setImageResource(R.mipmap.ic_input_keyboard);
                    mListView.smoothScrollToPosition(mArrayList.size());
                } else {
                    faceLinearLayout.setVisibility(View.GONE);
                    faceImageView.setImageResource(R.mipmap.ic_input_face);
                    AndroidUtil.showKeyboard(view);
                    mListView.smoothScrollToPosition(mArrayList.size());
                }
            }
        });

        imageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startPhoto(mActivity);
            }
        });

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFile = new File(FileUtil.getImagePath() + "admin_feedback.jpg");
                imagePath = imageFile.getAbsolutePath();
                mApplication.startCamera(mActivity, imageFile);
            }
        });

        contentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentEditText.isFocusable()) {
                    AndroidUtil.showKeyboard(view);
                    faceLinearLayout.setVisibility(View.GONE);
                    faceImageView.setImageResource(R.mipmap.ic_input_face);
                    mListView.smoothScrollToPosition(mArrayList.size());
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

        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipsTextView.getText().toString().contains("点击重试")) {
                    getFeedback();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFeedback();
                    }
                }, 1000);
            }
        });

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = TextUtil.replaceFace(contentEditText.getText());
                if (!TextUtil.isEmpty(content)) {
                    sendFeedback("text", content);
                }
            }
        });

    }

    private void getFeedback() {

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "adminMessage", "feedbackRecord");
        ajaxParams.put("user_id", user_id);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=adminMessage&a=feedbackRecord", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    mArrayList.clear();
                    JSONArray jsonArray = new JSONArray(mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                    }
                    if (mArrayList.isEmpty()) {
                        tipsTextView.setText("暂无反馈记录\n\n快来反馈吧");
                    } else {
                        mAdapter.notifyDataSetChanged();
                        tipsTextView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mListView.smoothScrollToPosition(mArrayList.size());
                    }
                } catch (JSONException e) {
                    getFeedbackFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getFeedbackFailure();
            }
        });

    }

    private void getFeedbackFailure() {

        tipsTextView.setText("读取数据失败\n\n点击重试！");
        tipsTextView.setVisibility(View.VISIBLE);

    }

    private void sendFeedback(final String type, final String content) {

        //加入消息列表
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message_id", "-1");
        hashMap.put("message_uid", "-1");
        hashMap.put("message_tid", user_id);
        hashMap.put("message_type", type);
        hashMap.put("message_content", content);
        hashMap.put("message_time", TimeUtil.getAll());
        hashMap.put("uid_info", "");
        mArrayList.add(hashMap);
        mAdapter.notifyDataSetChanged();
        mListView.smoothScrollToPosition(mArrayList.size());
        contentEditText.setText("");

        if (tipsTextView.getVisibility() == View.VISIBLE) {
            tipsTextView.setVisibility(View.GONE);
        }

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "adminMessage", "feedbackAdd");
        ajaxParams.put("type", type);
        ajaxParams.put("user_id", user_id);
        ajaxParams.put("content", content);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=adminMessage&a=feedbackAdd", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (!mApplication.getJsonSuccess(o.toString())) {
                    mArrayList.remove(mArrayList.size() - 1);
                    mAdapter.notifyDataSetChanged();
                    mListView.smoothScrollToPosition(mArrayList.size());
                    if (mArrayList.isEmpty()) {
                        tipsTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                mArrayList.remove(mArrayList.size() - 1);
                mAdapter.notifyDataSetChanged();
                mListView.smoothScrollToPosition(mArrayList.size());
                if (mArrayList.isEmpty()) {
                    tipsTextView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void sendImage() {

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userUpload", "uploadImageChat");

        try {
            ajaxParams.put("file", imageFile);
        } catch (FileNotFoundException e) {
            ToastUtil.show(mActivity, "文件不存在");
            DialogUtil.cancel();
            e.printStackTrace();
            return;
        }

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userUpload&a=uploadImageChat", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                String error = mApplication.getJsonError(o.toString());
                if (TextUtil.isEmpty(error)) {
                    String data = mApplication.getJsonData(o.toString());
                    sendFeedback("image", data);
                } else {
                    ToastUtil.showFailure(mActivity);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                new MyCountTime(2000, 1000) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        sendImage();
                    }
                }.start();
            }
        });

    }

    private void returnActivity() {

        if (faceLinearLayout.getVisibility() == View.VISIBLE) {
            faceLinearLayout.setVisibility(View.GONE);
        } else {
            mApplication.finishActivity(mActivity);
        }

    }

}
