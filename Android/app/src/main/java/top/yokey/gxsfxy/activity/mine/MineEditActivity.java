package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.io.FileNotFoundException;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.AreaActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.FileUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

@SuppressWarnings("all")
public class MineEditActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private File imageFile;
    private String imagePath;
    private boolean saveBoolean;

    private String user_qq;
    private String nick_name;
    private String user_city;
    private String user_area;
    private String user_sign;
    private String user_province;

    private ImageView backImageView;
    private TextView titleTextView;

    private RelativeLayout avatarRelativeLayout;
    private ImageView avatarImageView;
    private LinearLayout nicknameLinearLayout;
    private TextView nicknameTextView;
    private LinearLayout qqLinearLayout;
    private TextView qqTextView;
    private LinearLayout signLinearLayout;
    private TextView signTextView;
    private LinearLayout provinceLinearLayout;
    private TextView provinceTextView;
    private LinearLayout cityLinearLayout;
    private TextView cityTextView;
    private LinearLayout areaLinearLayout;
    private TextView areaTextView;
    private TextView saveTextView;

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
                    imagePath = FileUtil.createJpgByBitmap("user_avatar", mApplication.mBitmap);
                    imageFile = new File(imagePath);
                    saveAvatar();
                    break;
                case MyApplication.CODE_CHOOSE_AREA:
                    user_province = data.getStringExtra("province");
                    user_city = data.getStringExtra("city");
                    user_area = data.getStringExtra("area");
                    provinceTextView.setText(user_province);
                    cityTextView.setText(user_city);
                    areaTextView.setText(user_area);
                    saveBoolean = true;
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
        setContentView(R.layout.activity_mine_edit);
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

        avatarRelativeLayout = (RelativeLayout) findViewById(R.id.avatarRelativeLayout);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        nicknameLinearLayout = (LinearLayout) findViewById(R.id.nicknameLinearLayout);
        nicknameTextView = (TextView) findViewById(R.id.nicknameTextView);
        qqLinearLayout = (LinearLayout) findViewById(R.id.qqLinearLayout);
        qqTextView = (TextView) findViewById(R.id.qqTextView);
        signLinearLayout = (LinearLayout) findViewById(R.id.signLinearLayout);
        signTextView = (TextView) findViewById(R.id.signTextView);
        provinceLinearLayout = (LinearLayout) findViewById(R.id.provinceLinearLayout);
        provinceTextView = (TextView) findViewById(R.id.provinceTextView);
        cityLinearLayout = (LinearLayout) findViewById(R.id.cityLinearLayout);
        cityTextView = (TextView) findViewById(R.id.cityTextView);
        areaLinearLayout = (LinearLayout) findViewById(R.id.areaLinearLayout);
        areaTextView = (TextView) findViewById(R.id.areaTextView);

        saveTextView = (TextView) findViewById(R.id.saveTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        imageFile = null;
        imagePath = "null";
        saveBoolean = false;
        titleTextView.setText("修改资料");

        user_qq = mApplication.userHashMap.get("user_qq");
        nick_name = mApplication.userHashMap.get("nick_name");
        user_city = mApplication.userHashMap.get("user_city");
        user_area = mApplication.userHashMap.get("user_area");
        user_sign = mApplication.userHashMap.get("user_sign");
        user_province = mApplication.userHashMap.get("user_province");

        if (TextUtil.isEmpty(user_qq)) {
            user_qq = "未填写";
        }

        if (TextUtil.isEmpty(nick_name)) {
            nick_name = "未填写";
        }

        if (TextUtil.isEmpty(user_city)) {
            user_city = "未填写";
        }

        if (TextUtil.isEmpty(user_area)) {
            user_area = "未填写";
        }

        if (TextUtil.isEmpty(user_sign)) {
            user_sign = "未填写";
        }

        if (TextUtil.isEmpty(user_province)) {
            user_province = "未填写";
        }

        if (TextUtil.isEmpty(mApplication.userHashMap.get("user_avatar"))) {
            avatarImageView.setImageResource(R.mipmap.ic_avatar);
        } else {
            ImageLoader.getInstance().displayImage(mApplication.userHashMap.get("user_avatar"), avatarImageView);
        }

        qqTextView.setText(user_qq);
        nicknameTextView.setText(nick_name);
        signTextView.setText(user_sign);
        provinceTextView.setText(user_province);
        cityTextView.setText(user_city);
        areaTextView.setText(user_area);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
            }
        });

        avatarRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.image(mActivity, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.cancel();
                        imageFile = new File(FileUtil.getImagePath() + "user_avatar.jpg");
                        imagePath = imageFile.getAbsolutePath();
                        mApplication.startCamera(mActivity, imageFile);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.cancel();
                        mApplication.startPhoto(mActivity);
                    }
                });
            }
        });

        nicknameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new AlertDialog.Builder(mActivity).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_input);
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
                titleTextView.setText("请输入您的昵称");

                final EditText contentEditText = (EditText) window.findViewById(R.id.contentEditText);
                contentEditText.setText(nick_name);
                contentEditText.setSelection(nick_name.length());

                TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
                confirmTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = contentEditText.getText().toString();
                        if (TextUtil.isEmpty(content)) {
                            ToastUtil.show(mActivity, "不能为空");
                            return;
                        }
                        nick_name = contentEditText.getText().toString();
                        nicknameTextView.setText(nick_name);
                        AndroidUtil.hideKeyboard(v);
                        saveBoolean = true;
                        dialog.cancel();
                    }
                });

                TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
        });

        qqLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new AlertDialog.Builder(mActivity).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_input);
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
                titleTextView.setText("请输入企鹅号码");

                final EditText contentEditText = (EditText) window.findViewById(R.id.contentEditText);
                contentEditText.setText(user_qq);
                contentEditText.setSelection(user_qq.length());

                TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
                confirmTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = contentEditText.getText().toString();
                        if (TextUtil.isEmpty(content)) {
                            ToastUtil.show(mActivity, "不能为空");
                            return;
                        }
                        user_qq = contentEditText.getText().toString();
                        qqTextView.setText(user_qq);
                        AndroidUtil.hideKeyboard(v);
                        saveBoolean = true;
                        dialog.cancel();
                    }
                });

                TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
        });

        signLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new AlertDialog.Builder(mActivity).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_input);
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
                titleTextView.setText("请输入签名");

                final EditText contentEditText = (EditText) window.findViewById(R.id.contentEditText);
                contentEditText.setText(user_sign);
                contentEditText.setSelection(user_sign.length());

                TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
                confirmTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = contentEditText.getText().toString();
                        if (TextUtil.isEmpty(content)) {
                            ToastUtil.show(mActivity, "不能为空");
                            return;
                        }
                        user_sign = contentEditText.getText().toString();
                        signTextView.setText(user_sign);
                        AndroidUtil.hideKeyboard(v);
                        saveBoolean = true;
                        dialog.cancel();
                    }
                });

                TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
                cancelTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
        });

        provinceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, AreaActivity.class), MyApplication.CODE_CHOOSE_AREA);
            }
        });

        cityLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, AreaActivity.class), MyApplication.CODE_CHOOSE_AREA);
            }
        });

        areaLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplication.startActivity(mActivity, new Intent(mActivity, AreaActivity.class), MyApplication.CODE_CHOOSE_AREA);
            }
        });

        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!saveBoolean) {
                    ToastUtil.show(mActivity, "信息未改变，不需要修改");
                } else {
                    saveInfo();
                }
            }
        });

    }

    private void saveAvatar() {

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "modifyAvatar");

        try {
            ajaxParams.put("file", imageFile);
        } catch (FileNotFoundException e) {
            ToastUtil.show(mActivity, "文件不存在");
            DialogUtil.cancel();
            e.printStackTrace();
            return;
        }

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=modifyAvatar", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                String error = mApplication.getJsonError(o.toString());
                if (TextUtil.isEmpty(error)) {
                    ToastUtil.show(mActivity, "头像更换成功，下次启动生效");
                    ImageLoader.getInstance().displayImage(mApplication.getJsonData(o.toString()), avatarImageView);
                    mApplication.userHashMap.put("user_avatar", mApplication.getJsonData(o.toString()));
                    File file = ImageLoader.getInstance().getDiskCache().get(mApplication.userHashMap.get("user_avatar"));
                    if (file != null) {
                        if (!file.isDirectory()) {
                            file.delete();
                        }
                    }
                } else {
                    ToastUtil.showFailure(mActivity);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.showFailure(mActivity);
            }
        });

    }

    private void saveInfo() {

        DialogUtil.progress(mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "editInfo");
        ajaxParams.put("user_qq", user_qq);
        ajaxParams.put("nick_name", nick_name);
        ajaxParams.put("user_sign", user_sign);
        ajaxParams.put("user_city", user_city);
        ajaxParams.put("user_area", user_area);
        ajaxParams.put("user_province", user_province);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=editInfo", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (mApplication.getJsonSuccess(o.toString())) {
                    mApplication.userHashMap.put("user_qq", user_qq);
                    mApplication.userHashMap.put("nick_name", nick_name);
                    mApplication.userHashMap.put("user_sign", user_sign);
                    mApplication.userHashMap.put("user_city", user_city);
                    mApplication.userHashMap.put("user_area", user_area);
                    mApplication.userHashMap.put("user_province", user_province);
                    ToastUtil.showSuccess(mActivity);
                    mActivity.setResult(RESULT_OK);
                    mApplication.finishActivity(mActivity);
                } else {
                    saveInfoFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                saveInfoFailure();
            }
        });

    }

    private void saveInfoFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("确认您的选择")
                    .setMessage("修改个人信息失败，是否重试")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveInfo();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mApplication.finishActivity(mActivity);
                        }
                    })
                    .show();
        }

    }

    private void returnActivity() {

        if (saveBoolean) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("确认您的选择")
                    .setMessage("放弃修改个人资料")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mApplication.finishActivity(mActivity);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else {
            mApplication.finishActivity(mActivity);
        }

    }

}