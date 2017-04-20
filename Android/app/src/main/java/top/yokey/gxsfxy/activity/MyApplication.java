package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;
import top.yokey.gxsfxy.activity.mine.LoginActivity;
import top.yokey.gxsfxy.activity.message.ChatOnlyActivity;
import top.yokey.gxsfxy.activity.mine.MineCenterActivity;
import top.yokey.gxsfxy.activity.mine.UserCenterActivity;
import top.yokey.gxsfxy.utility.FileUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class MyApplication extends Application {

    public static final int CODE_CHOOSE_MAP = 4;
    public static final int CODE_CHOOSE_AREA = 5;
    public static final int CODE_CHOOSE_PHOTO = 6;
    public static final int CODE_CHOOSE_CAMERA = 7;
    public static final int CODE_CHOOSE_PHOTO_CROP = 8;

    public final static int CODE_REGISTER = 10;
    public final static int CODE_REGISTER_EDU = 11;
    public final static int CODE_REGISTER_BIND = 12;

    public final static int CODE_MINE_EDIT_PASS = 20;
    public final static int CODE_MINE_BIND_EMAIL = 21;
    public final static int CODE_MINE_BIND_EDU = 22;
    public final static int CODE_MINE_EDIT_INFO = 23;

    public final static int CODE_CHAT_USER = 23;

    //全局变量
    public Bitmap mBitmap;
    public FinalHttp mFinalHttp;
    public FinalHttp eduFinalHttp;
    public FinalHttp newsFinalHttp;
    public CookieManager mCookieManager;
    public Html.ImageGetter mImageGetter;
    public AlphaAnimation showAlphaAnimation;
    public AlphaAnimation goneAlphaAnimation;
    public SharedPreferences mSharedPreferences;
    public TranslateAnimation upTranslateAnimation;
    public NotificationManager mNotificationManager;
    public TranslateAnimation downTranslateAnimation;
    public SharedPreferences.Editor mSharedPreferencesEditor;

    //全局数组
    public List<Activity> activityList;
    public ArrayList<HashMap<String, String>> configArrayList;

    //链接变量
    public String urlString;
    public String apiUrlString;
    public String publicUrlString;
    public String publicHtmlUrlString;
    public String publicHtmlMobileUrlString;

    public String eduLoginUrlString;
    public String eduInfoUrlString;
    public String eduGradeUrlString;
    public String eduScheduleUrlString;

    public int adminNewsPosInt;

    //用户信息
    public String userTokenString;
    public String userUsernameString;
    public HashMap<String, String> userHashMap;

    public boolean messageNotifyBoolean;
    public boolean saveFlowCheckBoxBoolean;
    public boolean messagePushCheckBoxBoolean;
    public boolean updateCheckBoolean;

    @Override
    public void onCreate() {
        super.onCreate();

        //全局变量初始化
        mBitmap = null;
        mFinalHttp = new FinalHttp();
        mFinalHttp.configTimeout(5000);
        mFinalHttp.configCharset("UTF-8");
        newsFinalHttp = new FinalHttp();
        newsFinalHttp.configTimeout(5000);
        newsFinalHttp.configCharset("UTF-8");
        newsFinalHttp.configUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
        mCookieManager = CookieManager.getInstance();
        mSharedPreferences = this.getSharedPreferences("yokey_gxsfxy", MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mSharedPreferencesEditor.apply();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        showAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        showAlphaAnimation.setDuration(500);
        goneAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        goneAlphaAnimation.setDuration(500);
        upTranslateAnimation = new TranslateAnimation(0.0f, 0.0f, 600.0f, 0.0f);
        upTranslateAnimation.setDuration(500);
        downTranslateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 600.0f);
        downTranslateAnimation.setDuration(500);
        mImageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                try {
                    int id = getResources().getIdentifier(source, "mipmap", getPackageName());
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), id);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    return drawable;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        //全局数组
        activityList = new LinkedList<>();
        configArrayList = new ArrayList<>();

        //链接变量
        urlString = "http://gxsfxy.yokey.top/";
        publicUrlString = urlString + "Public/";
        publicHtmlUrlString = publicUrlString + "html/";
        publicHtmlMobileUrlString = publicHtmlUrlString + "mobile/";
        apiUrlString = urlString + "index.php?m=mobile&";

        //用户信息
        userHashMap = new HashMap<>();
        userTokenString = mSharedPreferences.getString("user_token", "");
        userUsernameString = mSharedPreferences.getString("user_username", "");

        messageNotifyBoolean = mSharedPreferences.getBoolean("setting_message_notify", true);
        saveFlowCheckBoxBoolean = mSharedPreferences.getBoolean("setting_save_flow", false);
        messagePushCheckBoxBoolean = mSharedPreferences.getBoolean("setting_message_push", true);
        updateCheckBoolean = mSharedPreferences.getBoolean("setting_update_check", true);

        //第三方库初始化
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(4)).build();
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(new File(FileUtil.getCachePath())))
                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
                .defaultDisplayImageOptions(displayImageOptions)
                .build());

        //SMSSDK
        SMSSDK.initSDK(this, "145e5ec939ccd", "106c2be2ea8707e7950d979a39e03904");

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //友盟统计
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //一些子程序
        FileUtil.createDownPath();
        FileUtil.createCachePath();
        FileUtil.createImagePath();

    }

    //退出 APP
    public void finishApp() {

        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //获取程序版本
    public String getVersion() {

        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0";
        }

    }

    //获取设备名称
    public String getDeviceName() {

        try {
            return Build.BRAND + " " + Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            return "未知设备";
        }

    }

    //获取系统名称版本
    public String getAndroidVersion() {

        try {
            return "Android" + " " + Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Android";
        }

    }

    //获取返回数据的 JSON 数据的 data
    public String getJsonData(String json) {

        if (!TextUtil.isJson(json)) {
            return "null";
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("data");
        } catch (JSONException e) {
            return "null";
        }

    }

    //获取返回数据的 JSON 数据的 error
    public String getJsonError(String json) {

        if (!TextUtil.isJson(json) || !json.contains("error")) {
            return "null";
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }

    }

    //判断返回的数据是否为 1
    public boolean getJsonSuccess(String json) {

        return getJsonData(json).equals("1");

    }

    //跳到相册
    public void startPhoto(Activity activity) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        activity.startActivityForResult(intent, CODE_CHOOSE_PHOTO);

    }

    //添加 Activity
    public void addActivity(Activity activity) {

        activityList.add(activity);

    }

    //结束 Activity
    public void finishActivity(Activity activity) {

        activity.finish();

    }

    //启动 Activity login
    public void startActivityLogin(Activity activity) {

        addActivity(activity);
        activity.startActivity(new Intent(activity, LoginActivity.class));

    }

    //跳到拍照
    public void startCamera(Activity activity, File file) {

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(intent, CODE_CHOOSE_CAMERA);
        } catch (Exception e) {
            ToastUtil.show(activity, "未检测到相机");
        }

    }

    //跳到拨打电话
    public void startCall(Activity activity, String phone) {

        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(activity, "未检测到电话程序");
        }

    }

    //跳转到安装 APK
    public void startInstallApk(Activity activity, File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);

    }

    //跳到图片裁剪
    public void startPhotoCrop(Activity activity, String path) {

        Intent intent = new Intent(activity, CropActivity.class);
        intent.putExtra("path", path);
        activity.startActivityForResult(intent, CODE_CHOOSE_PHOTO_CROP);

    }

    //启动 Activity
    public void startActivity(Activity activity, Intent intent) {

        addActivity(activity);
        activity.startActivity(intent);

    }

    //启动 Activity 单人聊天
    public void startActivityChatOnly(Activity activity, String id) {

        if (TextUtil.isEmpty(userTokenString) || TextUtil.isEmpty(userUsernameString)) {
            startActivityLogin(activity);
            return;
        }

        if (userHashMap.isEmpty()) {
            ToastUtil.show(activity, "请等待登录成功!");
            return;
        }

        addActivity(activity);
        Intent intent = new Intent(activity, ChatOnlyActivity.class);
        intent.putExtra("user_id", id);
        activity.startActivity(intent);

    }

    //启动 Activity 浏览器
    public void startActivityBrowser(Activity activity, String link) {

        if (TextUtil.isEmpty(link)) {
            ToastUtil.show(activity, "链接为空");
            return;
        }

        if (!link.contains("http")) {
            link = "http://" + link;
        }

        addActivity(activity);
        Intent intent = new Intent(activity, BrowserActivity.class);
        intent.putExtra("link", link);
        activity.startActivity(intent);

    }

    //启动 Activity 用户中心
    public void startActivityUserCenter(Activity activity, String id) {

        if (TextUtil.isEmpty(userTokenString) || TextUtil.isEmpty(userUsernameString)) {
            startActivityLogin(activity);
            return;
        }

        if (userHashMap.isEmpty()) {
            ToastUtil.show(activity, "请等待登录成功!");
            return;
        }

        addActivity(activity);
        Intent intent = new Intent();
        if (!userHashMap.isEmpty() && id.equals(userHashMap.get("user_id"))) {
            intent.setClass(activity, MineCenterActivity.class);
        } else {
            intent.setClass(activity, UserCenterActivity.class);
            intent.putExtra("user_id", id);
        }
        activity.startActivity(intent);

    }

    //启动 Activity 带 Result
    public void startActivity(Activity activity, Intent intent, int code) {

        addActivity(activity);
        activity.startActivityForResult(intent, code);

    }

    //启动 Activity 时判断是否登录
    public void startActivityWithLogin(Activity activity, Intent intent) {

        if (TextUtil.isEmpty(userTokenString) || TextUtil.isEmpty(userUsernameString)) {
            startActivityLogin(activity);
            return;
        }

        addActivity(activity);
        activity.startActivity(intent);

    }

    //启动 Activity 时判断是否登录带 Result
    public void startActivityWithLogin(Activity activity, Intent intent, int code) {

        if (userHashMap.isEmpty()) {
            startActivityLogin(activity);
            return;
        }

        addActivity(activity);
        activity.startActivityForResult(intent, code);

    }

    //启动 Activity 时判断是否登录成功
    public void startActivityWithLoginSuccess(Activity activity, Intent intent) {

        if (TextUtil.isEmpty(userTokenString) || TextUtil.isEmpty(userUsernameString)) {
            startActivityLogin(activity);
            return;
        }

        if (userHashMap.isEmpty()) {
            ToastUtil.show(activity, "请等待登录成功!");
            return;
        }

        addActivity(activity);
        activity.startActivity(intent);

    }

    //启动 Activity 时判断是否登录成功带 Result
    public void startActivityWithLoginSuccess(Activity activity, Intent intent, int code) {

        if (TextUtil.isEmpty(userTokenString) || TextUtil.isEmpty(userUsernameString)) {
            startActivityLogin(activity);
            return;
        }

        if (userHashMap.isEmpty()) {
            ToastUtil.show(activity, "请等待登录成功!");
            return;
        }

        addActivity(activity);
        activity.startActivityForResult(intent, code);

    }

    //启动 Activity 分享
    public void startActivityShare(Activity activity, String title, String name, String jingle, String image, String link) {

        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("name", name);
        intent.putExtra("jingle", jingle);
        intent.putExtra("image", image);
        intent.putExtra("link", link);
        startActivity(activity, intent);

    }

}