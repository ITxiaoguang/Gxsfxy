package top.yokey.gxsfxy.activity.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.adapter.AdminMessageIndexListAdapter;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;

public class AdminHomeActivity extends AppCompatActivity {

    private ScrollView mScrollView;

    private TextView userCountTextView;
    private TextView userRegTextView;
    private TextView userLoginTextView;

    private TextView dynamicCountTextView;
    private TextView newsCountTextView;
    private TextView uploadCountTextView;

    private TextView commentCountTextView;
    private TextView praiseCountTextView;
    private TextView followCountTextView;

    private TextView messageTitleTextView;
    private RecyclerView messageListView;

    private TextView newsTitleTextView;
    private TextView newsUpdateTextView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_admin_home);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        mScrollView = (ScrollView) findViewById(R.id.mainScrollView);

        userCountTextView = (TextView) findViewById(R.id.userCountTextView);
        userRegTextView = (TextView) findViewById(R.id.userRegTextView);
        userLoginTextView = (TextView) findViewById(R.id.userLoginTextView);

        dynamicCountTextView = (TextView) findViewById(R.id.dynamicCountTextView);
        newsCountTextView = (TextView) findViewById(R.id.newsCountTextView);
        uploadCountTextView = (TextView) findViewById(R.id.uploadCountTextView);

        commentCountTextView = (TextView) findViewById(R.id.commentCountTextView);
        praiseCountTextView = (TextView) findViewById(R.id.praiseCountTextView);
        followCountTextView = (TextView) findViewById(R.id.followCountTextView);

        messageTitleTextView = (TextView) findViewById(R.id.messageTitleTextView);
        messageListView = (RecyclerView) findViewById(R.id.messageListView);

        newsTitleTextView = (TextView) findViewById(R.id.newsTitleTextView);
        newsUpdateTextView = (TextView) findViewById(R.id.newsUpdateTextView);

    }

    private void initData() {

        if (AdminMainActivity.mApplication.userHashMap.get("user_power").equals("超级管理员")) {
            newsTitleTextView.setVisibility(View.VISIBLE);
        } else {
            newsTitleTextView.setVisibility(View.GONE);
        }

        newsUpdateTextView.setText("");
        getMessage();
        getJson();

    }

    private void initEven() {

        messageTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminMainActivity.mApplication.startActivity(AdminMainActivity.mActivity,
                        new Intent(AdminMainActivity.mActivity, AdminMessageActivity.class));
            }
        });

        newsTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsUpdateTextView.setVisibility(View.VISIBLE);
                pushNews();
            }
        });

    }

    private void pushNews() {

        if (AdminMainActivity.mActivity == null) {
            return;
        }

        if (newsUpdateTextView.getText().toString().length() > 8192) {
            newsUpdateTextView.setText("");
        }

        final String link = "http://www.gxtc.edu.cn/Item/" + AdminMainActivity.mApplication.adminNewsPosInt + ".aspx";
        String content = "[" + TimeUtil.getTime() + "]：链接 - " + link + "\n";
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        newsUpdateTextView.append(content);

        AdminMainActivity.mApplication.newsFinalHttp.get(link, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                //处理一下
                String content = "[" + TimeUtil.getTime() + "]：读取链接内容成功，正在解析数据\n";
                newsUpdateTextView.append(content);
                content = o.toString();
                if (!content.contains("产生错误的可能原因")) {
                    try {
                        //新闻类型
                        content = content.substring(content.lastIndexOf("<h3>") + 4, content.length());
                        String news_type = content.substring(0, content.indexOf("</h3>"));
                        if (TextUtil.isEmpty(news_type)) {
                            news_type = "学校要闻";
                        } else {
                            if (news_type.equals("DV速递") || news_type.equals("评论") || news_type.equals("悦读") || news_type.equals("光影万千") || news_type.equals("老茶馆") || news_type.equals("师院观察")) {
                                news_type = "校园文化";
                            }
                            if (news_type.equals("热门排行") || news_type.equals("招生就业")) {
                                news_type = "学校要闻";
                            }
                        }
                        //新闻标题
                        content = content.substring(content.indexOf("<h2 class=\"title\">") + 18, content.length());
                        String news_title = content.substring(0, content.indexOf("</h2>"));
                        if (news_title.contains("校报") && news_title.contains("期")) {
                            news_type = "师院校报";
                        }
                        //新闻出自
                        content = content.substring(content.indexOf("文章来源：") + 5, content.length());
                        String news_from = content.substring(0, content.indexOf("</span>"));
                        if (TextUtil.isEmpty(news_from)) {
                            news_from = "广西师范学院";
                        }
                        //新闻图片
                        String news_image;
                        content = content.substring(content.indexOf("<div class=\"conTxt\">"), content.length());
                        content = content.substring(0, content.indexOf("<div class=\"page\">"));
                        if (content.contains("<img")) {
                            content = content.substring(content.indexOf("<img"), content.length());
                            content = content.substring(content.indexOf("src=\"") + 5, content.length());
                            news_image = "http://www.gxtc.edu.cn" + content.substring(0, content.indexOf("\""));
                        } else {
                            news_image = "http://gxsfxy.yokey.top/Public/upload/public/default.png";
                        }
                        //输出数据
                        content = "[" + TimeUtil.getTime() + "]：新闻类型：" + news_type + "\n";
                        newsUpdateTextView.append(content);
                        content = "[" + TimeUtil.getTime() + "]：新闻出自：" + news_from + "\n";
                        newsUpdateTextView.append(content);
                        content = "[" + TimeUtil.getTime() + "]：新闻标题：" + news_title + "\n";
                        newsUpdateTextView.append(content);
                        content = "[" + TimeUtil.getTime() + "]：新闻图片：" + news_image + "\n";
                        newsUpdateTextView.append(content);
                        content = "[" + TimeUtil.getTime() + "]：新闻链接：" + link + "\n";
                        newsUpdateTextView.append(content);
                        content = "[" + TimeUtil.getTime() + "]：新闻时间：" + TimeUtil.getAll() + "\n";
                        newsUpdateTextView.append(content);
                        content = "[" + TimeUtil.getTime() + "]：上传到服务器...\n";
                        newsUpdateTextView.append(content);
                        //上传到服务器
                        UserAjaxParams ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "admin", "newsAdd");
                        ajaxParams.put("news_type", news_type);
                        ajaxParams.put("news_from", news_from);
                        ajaxParams.put("news_title", news_title);
                        ajaxParams.put("news_image", news_image);
                        ajaxParams.put("news_link", link);
                        ajaxParams.put("news_time", TimeUtil.getAll());
                        AdminMainActivity.mApplication.mFinalHttp.post(AdminMainActivity.mApplication.apiUrlString + "c=admin&a=newsAdd", ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                if (AdminMainActivity.mApplication.getJsonSuccess(o.toString())) {
                                    String content = "[" + TimeUtil.getTime() + "]：上传数据成功！\n";
                                    AdminMainActivity.mApplication.adminNewsPosInt++;
                                    newsUpdateTextView.append(content);
                                    pushNews();
                                } else {
                                    String error = AdminMainActivity.mApplication.getJsonError(o.toString());
                                    if (error.equals("新闻已存在")) {
                                        String content = "[" + TimeUtil.getTime() + "]：" + error + "\n";
                                        AdminMainActivity.mApplication.adminNewsPosInt++;
                                        newsUpdateTextView.append(content);
                                        pushNews();
                                    } else {
                                        String content = "[" + TimeUtil.getTime() + "]：上传数据失败，2秒后重试\n";
                                        newsUpdateTextView.append(content);
                                        new MyCountTime(2000, 1000) {
                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                                pushNews();
                                            }
                                        }.start();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                String content = "[" + TimeUtil.getTime() + "]：上传数据失败，2秒后重试\n";
                                newsUpdateTextView.append(content);
                                new MyCountTime(2000, 1000) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        pushNews();
                                    }
                                }.start();
                            }
                        });
                    } catch (Exception e) {
                        content = "[" + TimeUtil.getTime() + "]：数据解析出错，已跳过！\n";
                        AdminMainActivity.mApplication.adminNewsPosInt++;
                        newsUpdateTextView.append(content);
                        pushNews();
                    }
                } else {
                    if (content.contains("您访问的内容信息不存在")) {
                        if (AdminMainActivity.mActivity != null) {
                            new AlertDialog.Builder(AdminMainActivity.mActivity)
                                    .setTitle("是否跳过?")
                                    .setMessage("这个链接暂无内容")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            AdminMainActivity.mApplication.adminNewsPosInt++;
                                            pushNews();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //更新内容到服务器
                                            UserAjaxParams ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "admin", "configUpdate");
                                            ajaxParams.put("config_type", "public");
                                            ajaxParams.put("config_name", "admin_news_pos");
                                            ajaxParams.put("config_value", AdminMainActivity.mApplication.adminNewsPosInt + "");
                                            AdminMainActivity.mApplication.mFinalHttp.post(AdminMainActivity.mApplication.apiUrlString + "c=admin&a=configUpdate", ajaxParams, null);
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        content = "[" + TimeUtil.getTime() + "]：您访问的内容信息需要经过审核才能浏览\n";
                        AdminMainActivity.mApplication.adminNewsPosInt++;
                        newsUpdateTextView.append(content);
                        pushNews();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                String content = "[" + TimeUtil.getTime() + "]：网络链接失败，2秒后重试\n";
                newsUpdateTextView.append(content);
                new MyCountTime(2000, 1000) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        pushNews();
                    }
                }.start();
            }
        });

    }

    private void getJson() {

        DialogUtil.progress(AdminMainActivity.mActivity);

        UserAjaxParams ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "admin", "index");

        AdminMainActivity.mApplication.mFinalHttp.post(AdminMainActivity.mApplication.apiUrlString + "c=admin&a=index", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(AdminMainActivity.mApplication.getJsonData(o.toString()));
                    String temp = jsonObject.getString("user_count") + "<br><font color='#999999'>用户总数</font>";
                    userCountTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("user_day_reg") + "<br><font color='#999999'>今日注册</font>";
                    userRegTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("user_day_login") + "<br><font color='#999999'>今日登录</font>";
                    userLoginTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("dynamic_count") + "<br><font color='#999999'>动态总数</font>";
                    dynamicCountTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("news_count") + "<br><font color='#999999'>新闻总数</font>";
                    newsCountTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("upload_count") + "<br><font color='#999999'>上传总数</font>";
                    uploadCountTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("comment_count") + "<br><font color='#999999'>评论总数</font>";
                    commentCountTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("praise_count") + "<br><font color='#999999'>赞的总数</font>";
                    praiseCountTextView.setText(Html.fromHtml(temp));
                    temp = jsonObject.getString("follow_count") + "<br><font color='#999999'>关注总数</font>";
                    followCountTextView.setText(Html.fromHtml(temp));
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

        if (!AdminMainActivity.mActivity.isFinishing()) {
            new AlertDialog.Builder(AdminMainActivity.mActivity)
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
                            AdminMainActivity.mApplication.finishActivity(AdminMainActivity.mActivity);
                        }
                    })
                    .show();
        }

    }

    private void getMessage() {

        messageTitleTextView.setText("管理团队留言 - 正在读取");

        UserAjaxParams ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "adminMessage", "messageIndex");

        AdminMainActivity.mApplication.mFinalHttp.post(AdminMainActivity.mApplication.apiUrlString + "c=adminMessage&a=messageIndex", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(AdminMainActivity.mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                    }
                    messageListView.setLayoutManager(new LinearLayoutManager(AdminMainActivity.mActivity));
                    messageListView.setAdapter(new AdminMessageIndexListAdapter(AdminMainActivity.mApplication, AdminMainActivity.mActivity, arrayList));
                    messageTitleTextView.setText("管理团队留言");
                } catch (JSONException e) {
                    messageTitleTextView.setText("管理团队留言 - 点击重试");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                messageTitleTextView.setText("管理团队留言 - 点击重试");
            }
        });

    }

}