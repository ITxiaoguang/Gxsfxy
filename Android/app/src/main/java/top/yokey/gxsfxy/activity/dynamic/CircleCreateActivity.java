package top.yokey.gxsfxy.activity.dynamic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.FileUtil;
import top.yokey.gxsfxy.utility.ImageUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class CircleCreateActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String id;
    private String location;

    private int imageInt;
    private File[] imageFile;
    private String[] imagePath;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView editImageView;

    private EditText contentEditText;
    private ImageView faceImageView;
    private TextView calcTextView;

    private LinearLayout faceLinearLayout;
    private ImageView[] faceImageViews;

    private TextView locationTextView;
    private ImageView[] mImageView;

    private AMapLocationClient mLocationClient;

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == RESULT_OK) {
            switch (req) {
                case MyApplication.CODE_CHOOSE_PHOTO:
                    imagePath[imageInt] = AndroidUtil.getMediaPath(mActivity, data.getData());
                    mApplication.startPhotoCrop(mActivity, imagePath[imageInt]);
                    break;
                case MyApplication.CODE_CHOOSE_CAMERA:
                    mApplication.startPhotoCrop(mActivity, imagePath[imageInt]);
                    break;
                case MyApplication.CODE_CHOOSE_PHOTO_CROP:
                    imagePath[imageInt] = FileUtil.createJpgByBitmap("circle" + imageInt, mApplication.mBitmap);
                    imageFile[imageInt] = new File(imagePath[imageInt]);
                    setImageView();
                    break;
                default:
                    break;
            }
        } else {
            imageFile[imageInt] = null;
            imagePath[imageInt] = "";
            setImageView();
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
        setContentView(R.layout.activity_circle_create);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.backImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        editImageView = (ImageView) findViewById(R.id.moreImageView);

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

        locationTextView = (TextView) findViewById(R.id.locationTextView);
        mImageView = new ImageView[9];
        mImageView[0] = (ImageView) findViewById(R.id.oneImageView);
        mImageView[1] = (ImageView) findViewById(R.id.twoImageView);
        mImageView[2] = (ImageView) findViewById(R.id.thrImageView);
        mImageView[3] = (ImageView) findViewById(R.id.fouImageView);
        mImageView[4] = (ImageView) findViewById(R.id.fivImageView);
        mImageView[5] = (ImageView) findViewById(R.id.sixImageView);
        mImageView[6] = (ImageView) findViewById(R.id.sevImageView);
        mImageView[7] = (ImageView) findViewById(R.id.eigImageView);
        mImageView[8] = (ImageView) findViewById(R.id.nigImageView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        id = "";
        location = "";

        imageInt = 0;
        imageFile = new File[9];
        imagePath = new String[9];
        for (int i = 0; i < imageFile.length; i++) {
            imageFile[i] = null;
            imagePath[i] = "";
        }

        titleTextView.setText("发布圈子");
        editImageView.setImageResource(R.mipmap.ic_action_edit);

        faceLinearLayout.setVisibility(View.GONE);
        ControlUtil.setFocusable(contentEditText);
        locationTextView.setText("定位中...");

        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        locationTextView.setText(amapLocation.getAddress());
                        location = amapLocation.getAddress();
                    } else {
                        locationTextView.setText("定位失败...");
                        location = "";
                    }
                }
                mLocationClient.stopLocation();
            }
        };
        mLocationClient = new AMapLocationClient(mApplication);
        mLocationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(false);
        if (mLocationOption.isOnceLocationLatest()) {
            mLocationOption.setOnceLocationLatest(true);
        }
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

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

        for (int i = 0; i < 9; i++) {
            final int num = i;
            mImageView[num].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtil.isEmpty(imagePath[num])) {
                        imageInt = num;
                        DialogUtil.image(mActivity, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtil.cancel();
                                imageFile[imageInt] = new File(FileUtil.getImagePath() + "circle" + imageInt + ".jpg");
                                imagePath[imageInt] = imageFile[imageInt].getAbsolutePath();
                                mApplication.startCamera(mActivity, imageFile[imageInt]);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtil.cancel();
                                mApplication.startPhoto(mActivity);
                            }
                        });
                    } else {
                        new AlertDialog.Builder(mActivity).setMessage("您想做什么？")
                                .setPositiveButton("更换", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        imageInt = num;
                                        DialogUtil.image(mActivity, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DialogUtil.cancel();
                                                imageFile[imageInt] = new File(FileUtil.getImagePath() + "circle" + imageInt + ".jpg");
                                                imagePath[imageInt] = imageFile[imageInt].getAbsolutePath();
                                                mApplication.startCamera(mActivity, imageFile[imageInt]);
                                            }
                                        }, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DialogUtil.cancel();
                                                mApplication.startPhoto(mActivity);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("清除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        imagePath[num] = "";
                                        setImageView();
                                    }
                                }).show();
                    }
                }
            });
        }

    }

    private void setImageView() {

        Vector<String> imageVector = new Vector<>();
        for (int i = 0; i < 9; i++) {
            mImageView[i].setImageResource(R.mipmap.ic_transparent);
            if (!TextUtil.isEmpty(imagePath[i])) {
                imageVector.add(imagePath[i]);
            }
        }
        for (int i = 0; i < imageVector.size(); i++) {
            mImageView[i].setImageBitmap(ImageUtil.getSmall(imageVector.get(i)));
            imagePath[i] = imageVector.get(i);
        }
        for (int i = imageVector.size(); i < 9; i++) {
            imagePath[i] = "";
        }
        if (imageVector.size() < 9) {
            mImageView[imageVector.size()].setImageResource(R.mipmap.ic_upload_image);
        }

    }

    private void circleCreate() {

        if (TextUtil.isEmpty(contentEditText.getText().toString()) && TextUtil.isEmpty(imagePath[0])) {
            ToastUtil.show(mActivity, "没有内容也没有图耶...");
            return;
        }

        DialogUtil.progress(mActivity, "正在发表");

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userDynamic", "circleCreate");
        ajaxParams.put("content", TextUtil.replaceFace(contentEditText.getText()));
        ajaxParams.put("location", location);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userDynamic&a=circleCreate", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                id = mApplication.getJsonData(o.toString());
                if (TextUtil.isEmpty(id)) {
                    ToastUtil.show(mActivity, "发表失败了");
                } else {
                    upload(0);
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

    private void upload(final int i) {

        if (i == 9 || TextUtil.isEmpty(imagePath[i]) || TextUtil.isEmpty(id)) {
            ToastUtil.show(mActivity, "发表成功");
            mApplication.finishActivity(mActivity);
            return;
        }

        try {

            DialogUtil.progress(mActivity, "正在上传第 " + (i + 1) + " 张图片...");

            UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userUpload", "uploadImage");
            ajaxParams.put("file", imageFile[i]);
            ajaxParams.put("type", "circle");
            ajaxParams.put("tid", id);

            mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userUpload&a=uploadImage", ajaxParams, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    if (mApplication.getJsonSuccess(o.toString())) {
                        new MyCountTime(1500, 500) {
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                DialogUtil.cancel();
                                upload(i + 1);
                            }
                        }.start();
                    } else {
                        DialogUtil.cancel();
                        new AlertDialog.Builder(mActivity)
                                .setTitle("是否重试?")
                                .setMessage("上传第 " + (i + 1) + " 张图片失败")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int pos) {
                                        upload(i);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int pos) {
                                        ToastUtil.show(mActivity, "发表成功,部分图片上传失败");
                                        mApplication.finishActivity(mActivity);
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    DialogUtil.cancel();
                    new AlertDialog.Builder(mActivity)
                            .setTitle("是否重试?")
                            .setMessage("上传第 " + (i + 1) + " 张图片失败")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int pos) {
                                    upload(i);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int pos) {
                                    ToastUtil.show(mActivity, "发表成功,部分图片上传失败");
                                    mApplication.finishActivity(mActivity);
                                }
                            })
                            .show();
                }
            });
        } catch (FileNotFoundException e) {
            ToastUtil.show(mActivity, "发表成功,部分图片上传失败");
            mApplication.finishActivity(mActivity);
            e.printStackTrace();
        }

    }

    private void returnActivity() {

        new AlertDialog.Builder(mActivity)
                .setTitle("确认您的选择")
                .setMessage("取消发布圈子")
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