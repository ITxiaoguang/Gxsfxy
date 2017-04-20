package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.utility.DisplayUtil;
import top.yokey.gxsfxy.utility.FileUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class LoadActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private boolean updateBoolean;
    private String system_notify;
    private String now_version;
    private String apk_download_link;
    private String update_content;
    private String version_control;
    private String advert_image;
    private String advert_image_link;

    private ImageView mImageView;

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
        setContentView(R.layout.activity_load);
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

        mImageView = (ImageView) findViewById(R.id.mainImageView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        updateBoolean = true;
        system_notify = "";
        now_version = "";
        apk_download_link = "";
        update_content = "";
        version_control = "";
        advert_image = "";
        advert_image_link = "";

        DisplayUtil.setFullScreen(mActivity);
        getJson();

    }

    private void initEven() {

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void getJson() {

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=getConfig", new BaseAjaxParams(mApplication, "base", "getConfig"), new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    String data = mApplication.getJsonData(o.toString());
                    if (!TextUtil.isEmpty(data)) {
                        JSONArray jsonArray = new JSONArray(data);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            mApplication.configArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                        }
                        for (int i = 0; i < mApplication.configArrayList.size(); i++) {
                            String config_type = mApplication.configArrayList.get(i).get("config_type");
                            String config_name = mApplication.configArrayList.get(i).get("config_name");
                            String config_value = mApplication.configArrayList.get(i).get("config_value");
                            if (config_type.equals("android") || config_type.equals("public")) {
                                switch (config_name) {
                                    case "system_notify":
                                        system_notify = config_value;
                                        break;
                                    case "now_version":
                                        now_version = config_value;
                                        break;
                                    case "apk_down_link":
                                        apk_download_link = config_value;
                                        break;
                                    case "update_content":
                                        update_content = config_value;
                                        break;
                                    case "version_control":
                                        version_control = config_value;
                                        break;
                                    case "advert_image":
                                        advert_image = config_value;
                                        break;
                                    case "adver_image_link":
                                        advert_image_link = config_value;
                                        break;
                                    case "edu_login_link":
                                        mApplication.eduLoginUrlString = config_value;
                                        break;
                                    case "edu_info_link":
                                        mApplication.eduInfoUrlString = config_value;
                                        break;
                                    case "edu_grade_link":
                                        mApplication.eduGradeUrlString = config_value;
                                        break;
                                    case "edu_schedule_link":
                                        mApplication.eduScheduleUrlString = config_value;
                                        break;
                                    case "admin_news_pos":
                                        mApplication.adminNewsPosInt = Integer.parseInt(config_value);
                                        break;
                                }
                            }
                        }
                        ImageLoader.getInstance().displayImage(advert_image, mImageView, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {
                                mImageView.setImageResource(R.mipmap.bg_load);
                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                mImageView.setImageResource(R.mipmap.bg_load);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                mImageView.setImageBitmap(bitmap);
                            }
                        });
                        checkVersion();
                    } else {
                        getJsonFailure();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getJsonFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getJsonFailure();
            }
        });

    }

    private void getJsonFailure() {

        ToastUtil.show(mActivity, "读取数据失败,正在重试.");

        new MyCountTime(1500, 500) {
            @Override
            public void onFinish() {
                super.onFinish();
                getJson();
            }
        }.start();

    }

    private void checkVersion() {

        String version = mApplication.getVersion() + ":1";

        if (version_control.contains(version)) {
            checkUpdate();
        } else {
            new AlertDialog
                    .Builder(mActivity)
                    .setTitle("下载新版?")
                    .setMessage("当前版本已弃用")
                    .setCancelable(false)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateBoolean = false;
                            downloadApk();
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

    private void checkUpdate() {

        if (!mApplication.updateCheckBoolean) {

            startMain();

        } else {

            if (!now_version.equals(mApplication.getVersion())) {

                new AlertDialog
                        .Builder(mActivity)
                        .setTitle("发现新版本,是否更新?")
                        .setMessage(Html.fromHtml(update_content))
                        .setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateBoolean = true;
                                downloadApk();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startMain();
                            }
                        })
                        .show();

            } else {

                new MyCountTime(1000, 500) {
                    @Override
                    public void onFinish() {
                        startMain();
                    }
                }.start();

            }

        }

    }

    private void downloadApk() {

        final Dialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_query);
        final TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
        final TextView contentTextView = (TextView) window.findViewById(R.id.contentTextView);
        final TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
        final TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
        cancelTextView.setVisibility(View.GONE);
        confirmTextView.setText("取消");

        titleTextView.setText("正在下载");
        contentTextView.setText("已下载");
        contentTextView.append(": 0 %");

        final String filePath = FileUtil.getDownPath() + "/gxsfxy.apk";

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        final HttpHandler httpHandler;

        httpHandler = mApplication.mFinalHttp.download(apk_download_link, filePath, new AjaxCallBack<File>() {
            @Override
            public void onStart() {
                super.onStart();
                contentTextView.setText("已下载");
                contentTextView.append(": 0 %");
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                int progress;
                if (current != count && current != 0) {
                    progress = (int) (current / (float) count * 100);
                } else {
                    progress = 100;
                }
                String progressString = "已下载：" + progress + " %";
                contentTextView.setText(progressString);
            }

            @Override
            public void onSuccess(File t) {
                super.onSuccess(t);
                mApplication.startInstallApk(mActivity, new File(filePath));
                dialog.cancel();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "下载更新包失败,正在重试.");
                dialog.cancel();
                if (updateBoolean) {
                    checkUpdate();
                } else {
                    checkVersion();
                }
            }
        });

        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog
                        .Builder(mActivity)
                        .setTitle("确认您的选择")
                        .setMessage("取消下载?")
                        .setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                httpHandler.stop();
                                dialog.cancel();
                                if (updateBoolean) {
                                    startMain();
                                } else {
                                    mApplication.finishActivity(mActivity);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

    }

    private void startMain() {

        if (!TextUtil.isEmpty(system_notify)) {
            String old_notify = mApplication.mSharedPreferences.getString("system_notify", "");
            if (!old_notify.equals(system_notify)) {
                mApplication.mSharedPreferencesEditor.putString("system_notify", system_notify).apply();
                new AlertDialog.Builder(mActivity).setTitle("系统公告").setMessage(system_notify).setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mApplication.startActivity(mActivity, new Intent(mActivity, MainActivity.class));
                                mApplication.finishActivity(mActivity);
                            }
                        }).show();
            } else {
                mApplication.startActivity(mActivity, new Intent(mActivity, MainActivity.class));
                mApplication.finishActivity(mActivity);
            }
        } else {
            mApplication.startActivity(mActivity, new Intent(mActivity, MainActivity.class));
            mApplication.finishActivity(mActivity);
        }

    }

    private void returnActivity() {

        new AlertDialog
                .Builder(mActivity)
                .setTitle("确认您的选择")
                .setMessage("退出程序?")
                .setCancelable(false)
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