package top.yokey.gxsfxy.activity.dynamic;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class TopicCreateActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView editImageView;

    private EditText titleEditText;
    private EditText contentEditText;
    private ImageView faceImageView;
    private TextView calcTextView;

    private LinearLayout faceLinearLayout;
    private ImageView[] faceImageViews;

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
        setContentView(R.layout.activity_topic_create);
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
        editImageView = (ImageView) findViewById(R.id.moreImageView);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
        faceImageView = (ImageView) findViewById(R.id.faceImageView);
        calcTextView = (TextView) findViewById(R.id.calcTextView);

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

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("发布话题");
        editImageView.setImageResource(R.mipmap.ic_action_edit);

        faceLinearLayout.setVisibility(View.GONE);
        ControlUtil.setFocusable(titleEditText);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.hideKeyboard(view);
                circleCreate();
            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = contentEditText.getText().toString().length();
                if (length == 0) {
                    calcTextView.setText("");
                } else {
                    String temp = "已输入 " + length + " 个字符";
                    calcTextView.setText(temp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentEditText.isFocusable()) {
                    AndroidUtil.showKeyboard(view);
                    faceLinearLayout.setVisibility(View.GONE);
                    faceImageView.setImageResource(R.mipmap.ic_input_face);
                }
            }
        });

        faceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceLinearLayout.getVisibility() == View.GONE) {
                    AndroidUtil.hideKeyboard(view);
                    faceLinearLayout.setVisibility(View.VISIBLE);
                    faceImageView.setImageResource(R.mipmap.ic_input_keyboard);
                } else {
                    faceLinearLayout.setVisibility(View.GONE);
                    faceImageView.setImageResource(R.mipmap.ic_input_face);
                    AndroidUtil.showKeyboard(view);
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

    private void circleCreate() {

        if (TextUtil.isEmpty(contentEditText.getText().toString())) {
            ToastUtil.show(mActivity, "请输入话题内容");
            return;
        }

        if (TextUtil.isEmpty(contentEditText.getText().toString())) {
            ToastUtil.show(mActivity, "请输入话题内容");
            return;
        }

        DialogUtil.progress(mActivity, "正在发表");

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userDynamic", "topicCreate");
        ajaxParams.put("title", titleEditText.getText().toString());
        ajaxParams.put("content", TextUtil.replaceFace(contentEditText.getText()));

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userDynamic&a=topicCreate", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    ToastUtil.showSuccess(mActivity);
                    mApplication.finishActivity(mActivity);
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

    private void returnActivity() {

        new AlertDialog.Builder(mActivity)
                .setTitle("确认您的选择")
                .setMessage("取消发布话题")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mApplication.finishActivity(mActivity);
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

}