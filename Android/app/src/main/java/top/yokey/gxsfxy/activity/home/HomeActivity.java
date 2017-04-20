package top.yokey.gxsfxy.activity.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.activity.mine.BindEduActivity;
import top.yokey.gxsfxy.adapter.ViewPagerAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.utility.TextUtil;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TextView[] dotTextView;
    private TextView[] mTextView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        dotTextView = new TextView[3];
        dotTextView[0] = (TextView) findViewById(R.id.dot1TextView);
        dotTextView[1] = (TextView) findViewById(R.id.dot2TextView);
        dotTextView[2] = (TextView) findViewById(R.id.dot3TextView);
        mTextView = new TextView[11];
        mTextView[0] = (TextView) findViewById(R.id.newsTextView);
        mTextView[1] = (TextView) findViewById(R.id.noticeTextView);
        mTextView[2] = (TextView) findViewById(R.id.collegeTextView);
        mTextView[3] = (TextView) findViewById(R.id.associationTextView);
        mTextView[4] = (TextView) findViewById(R.id.eduTextView);
        mTextView[5] = (TextView) findViewById(R.id.phoneTextView);
        mTextView[6] = (TextView) findViewById(R.id.summaryTextView);
        mTextView[7] = (TextView) findViewById(R.id.libraryTextView);
        mTextView[8] = (TextView) findViewById(R.id.yiBanTextView);
        mTextView[9] = (TextView) findViewById(R.id.newspaperTextView);
        mTextView[10] = (TextView) findViewById(R.id.mapTextView);

    }

    private void initData() {

        getJson();

    }

    private void initEven() {

        //新闻资讯
        mTextView[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivity(MainActivity.mActivity, new Intent(MainActivity.mActivity, NewsActivity.class));
            }
        });

        //通知公告
        mTextView[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivity(MainActivity.mActivity, new Intent(MainActivity.mActivity, NoticeActivity.class));
            }
        });

        //二级学院
        mTextView[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, MainActivity.mApplication.publicHtmlMobileUrlString + "college.html");
            }
        });

        //学生社团
        mTextView[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, "http://www.gxtc.edu.cn/Category_8/Index.aspx");
            }
        });

        //教务系统
        mTextView[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtil.isEmpty(MainActivity.mApplication.userTokenString)) {
                    MainActivity.mApplication.startActivityLogin(MainActivity.mActivity);
                } else {
                    if (TextUtil.isEmpty(MainActivity.mApplication.userHashMap.get("stu_id"))) {
                        new AlertDialog.Builder(MainActivity.mActivity)
                                .setTitle("绑定教务系统账号?")
                                .setMessage("您尚未绑定教务系统账号,请先绑定")
                                .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, new Intent(MainActivity.mActivity, BindEduActivity.class));
                                    }
                                })
                                .setNegativeButton("算了", null)
                                .show();
                    } else {
                        MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, new Intent(MainActivity.mActivity, EduActivity.class));
                    }
                }
            }
        });

        //学校电话
        mTextView[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivity(MainActivity.mActivity, new Intent(MainActivity.mActivity, PhoneActivity.class));
            }
        });

        //师院概述
        mTextView[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, MainActivity.mApplication.publicHtmlMobileUrlString + "summary.html");
            }
        });

        //图书馆
        mTextView[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, "http://www.niowoo.com/weixin.php/Home/Library/searchBook/library_id/2877");
            }
        });

        //师院易班
        mTextView[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, "http://www.yiban.cn/school/index?school_id=424");
            }
        });

        //师院校报
        mTextView[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivity(MainActivity.mActivity, new Intent(MainActivity.mActivity, NewspaperActivity.class));
            }
        });

        //校平面图
        mTextView[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityBrowser(MainActivity.mActivity, "http://mp.weixin.qq.com/s?__biz=MjM5NzIyNzM1OQ==&mid=400414608&idx=4&sn=eaa4997031c7ae34e7746b39e6e48ed2");
            }
        });

    }

    private void getJson() {

        MainActivity.mApplication.mFinalHttp.post(MainActivity.mApplication.apiUrlString + "c=base&a=newsHome", new BaseAjaxParams(MainActivity.mApplication, "news", "getHome"), new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = MainActivity.mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        //解析数据
                        List<View> list = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(data);
                        TextView[] textView = new TextView[jsonArray.length()];
                        ImageView[] imageView = new ImageView[jsonArray.length()];
                        final int size = jsonArray.length();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            final JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                            list.add(MainActivity.mActivity.getLayoutInflater().inflate(R.layout.include_home, null));
                            textView[j] = (TextView) list.get(j).findViewById(R.id.mainTextView);
                            imageView[j] = (ImageView) list.get(j).findViewById(R.id.mainImageView);
                            if (TextUtil.isEmpty(jsonObject.getString("news_image"))) {
                                imageView[j].setImageResource(R.mipmap.bg_default_img);
                            } else {
                                ImageLoader.getInstance().displayImage(jsonObject.getString("news_image"), imageView[j]);
                            }
                            textView[j].setText(jsonObject.getString("news_title"));
                            imageView[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Intent intent = new Intent(MainActivity.mActivity, NewsDetailedActivity.class);
                                        intent.putExtra("title", "新闻详细内容");
                                        intent.putExtra("news_id", jsonObject.getString("news_id"));
                                        intent.putExtra("news_link", jsonObject.getString("news_link"));
                                        intent.putExtra("news_from", jsonObject.getString("news_from"));
                                        intent.putExtra("news_time", jsonObject.getString("news_time"));
                                        intent.putExtra("news_title", jsonObject.getString("news_title"));
                                        MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            textView[j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Intent intent = new Intent(MainActivity.mActivity, NewsDetailedActivity.class);
                                        intent.putExtra("title", "新闻详细内容");
                                        intent.putExtra("news_id", jsonObject.getString("news_id"));
                                        intent.putExtra("news_link", jsonObject.getString("news_link"));
                                        intent.putExtra("news_from", jsonObject.getString("news_from"));
                                        intent.putExtra("news_time", jsonObject.getString("news_time"));
                                        intent.putExtra("news_title", jsonObject.getString("news_title"));
                                        MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        //适配器
                        mViewPager.setAdapter(new ViewPagerAdapter(list));
                        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                for (int i = 0; i < dotTextView.length; i++) {
                                    if (position >= dotTextView.length) {
                                        return;
                                    } else {
                                        if (i == position) {
                                            dotTextView[i].setBackgroundResource(R.drawable.border_text_view_dot_home1_press);
                                        } else {
                                            dotTextView[i].setBackgroundResource(R.drawable.border_text_view_dot_home1_normal);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        //自动轮播
                        new MyCountTime(6000000, 5000) {
                            @Override
                            public void onTick(long l) {
                                if (mViewPager.getCurrentItem() == (size - 1)) {
                                    mViewPager.setCurrentItem(0);
                                } else {
                                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                                }
                            }
                        }.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getJsonFailure();
                    }
                } else {
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

        new MyCountTime(1000, 500) {
            @Override
            public void onFinish() {
                super.onFinish();
                getJson();
            }
        }.start();

    }

}