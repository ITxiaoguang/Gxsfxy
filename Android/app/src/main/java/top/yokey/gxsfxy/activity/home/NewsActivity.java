package top.yokey.gxsfxy.activity.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.BasePagerAdapter;
import top.yokey.gxsfxy.adapter.NewsListAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class NewsActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private int posInt;
    private int[] pageInt;
    private String[] typeString;
    private TextView[] tipsTextView;
    private TextView[] stateTextView;
    private RecyclerView[] mListView;
    private NewsListAdapter[] mAdapter;
    private SwipeRefreshLayout[] mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>>[] mArrayList;

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
        setContentView(R.layout.activity_news);
        initView();
        initData();
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
        mTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("新闻资讯");
        posInt = 0;
        getType();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                posInt = position;
                if (pageInt[posInt] == 1) {
                    getJson();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (final TextView textView : tipsTextView) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (textView.getText().toString().contains("点击重试")) {
                        pageInt[posInt] = 1;
                        getJson();
                    }
                }
            });
        }

        for (SwipeRefreshLayout swipeRefreshLayout : mSwipeRefreshLayout) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageInt[posInt] = 1;
                            getJson();
                        }
                    }, 1000);
                }
            });
        }

        for (RecyclerView recyclerView : mListView) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();
                        if (lastVisibleItem == (totalItemCount - 1)) {
                            getJson();
                        }
                    }
                }
            });
        }

    }

    @SuppressWarnings("all")
    private void getType() {

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "newsType");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=newsType", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                String data = mApplication.getJsonData(o.toString());
                data = data.replace(",{\"news_type\":\"通知公告\"}", "");
                data = data.replace(",{\"news_type\":\"师院校报\"}", "");
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            ToastUtil.show(mActivity, "暂无新闻资讯");
                            returnActivity();
                        } else {
                            pageInt = new int[jsonArray.length()];
                            List<View> mViewList = new ArrayList<>();
                            List<String> mTitleList = new ArrayList<>();
                            typeString = new String[jsonArray.length()];
                            mArrayList = new ArrayList[jsonArray.length()];
                            tipsTextView = new TextView[jsonArray.length()];
                            stateTextView = new TextView[jsonArray.length()];
                            mListView = new RecyclerView[jsonArray.length()];
                            mAdapter = new NewsListAdapter[jsonArray.length()];
                            mSwipeRefreshLayout = new SwipeRefreshLayout[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                pageInt[i] = 1;
                                mArrayList[i] = new ArrayList<>();
                                mAdapter[i] = new NewsListAdapter(mApplication, mActivity, mArrayList[i]);
                                typeString[i] = new JSONObject(jsonArray.getString(i)).getString("news_type");
                                mTitleList.add(new JSONObject(jsonArray.getString(i)).getString("news_type"));
                                mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
                                tipsTextView[i] = (TextView) mViewList.get(i).findViewById(R.id.tipsTextView);
                                stateTextView[i] = (TextView) mViewList.get(i).findViewById(R.id.stateTextView);
                                mListView[i] = (RecyclerView) mViewList.get(i).findViewById(R.id.mainListView);
                                mSwipeRefreshLayout[i] = (SwipeRefreshLayout) mViewList.get(i).findViewById(R.id.mainSwipeRefreshLayout);
                                mListView[i].setLayoutManager(new LinearLayoutManager(mActivity));
                                ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout[i]);
                                mListView[i].setAdapter(mAdapter[i]);
                            }
                            ControlUtil.setTabLayout(mActivity, mTabLayout, new BasePagerAdapter(mViewList, mTitleList), mViewPager);
                            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                            initEven();
                            getJson();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getTypeFailure();
                    }
                } else {
                    getTypeFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                getTypeFailure();
            }
        });

    }

    private void getTypeFailure() {

        if (mActivity.isFinishing()) {
            new AlertDialog
                    .Builder(mActivity)
                    .setCancelable(false)
                    .setTitle("是否重试?")
                    .setMessage("读取分类失败")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getType();
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

    private void getJson() {

        if (pageInt[posInt] == 1) {
            if (mArrayList[posInt].isEmpty()) {
                tipsTextView[posInt].setText("加载中...");
                stateTextView[posInt].setVisibility(View.GONE);
                tipsTextView[posInt].setVisibility(View.VISIBLE);
            }
        } else {
            stateTextView[posInt].setText("加载中...");
            tipsTextView[posInt].setVisibility(View.GONE);
            stateTextView[posInt].setVisibility(View.VISIBLE);
        }

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "newsList");
        ajaxParams.put("type", typeString[posInt]);
        ajaxParams.put("page", pageInt[posInt] + "");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=newsList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            if (pageInt[posInt] == 1) {
                                tipsTextView[posInt].setText("暂无新闻");
                                stateTextView[posInt].setVisibility(View.GONE);
                                tipsTextView[posInt].setVisibility(View.VISIBLE);
                            } else {
                                stateTextView[posInt].setText("没有更多了");
                                tipsTextView[posInt].setVisibility(View.GONE);
                                if (stateTextView[posInt].getVisibility() == View.GONE) {
                                    stateTextView[posInt].setVisibility(View.VISIBLE);
                                }
                                new MyCountTime(1000, 500) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        if (stateTextView[posInt].getVisibility() == View.VISIBLE) {
                                            stateTextView[posInt].setVisibility(View.GONE);
                                            stateTextView[posInt].startAnimation(mApplication.goneAlphaAnimation);
                                        }
                                    }
                                }.start();
                            }
                        } else {
                            if (pageInt[posInt] == 1) {
                                mArrayList[posInt].clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mArrayList[posInt].add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            stateTextView[posInt].setVisibility(View.GONE);
                            tipsTextView[posInt].setVisibility(View.GONE);
                            pageInt[posInt]++;
                        }
                        mSwipeRefreshLayout[posInt].setRefreshing(false);
                        mAdapter[posInt].notifyDataSetChanged();
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

        mSwipeRefreshLayout[posInt].setRefreshing(false);
        mAdapter[posInt].notifyDataSetChanged();

        if (pageInt[posInt] == 1) {
            tipsTextView[posInt].setText("读取数据失败\n\n点击重试");
            stateTextView[posInt].setVisibility(View.GONE);
            tipsTextView[posInt].setVisibility(View.VISIBLE);
        } else {
            stateTextView[posInt].setText("读取数据失败");
            tipsTextView[posInt].setVisibility(View.GONE);
            if (stateTextView[posInt].getVisibility() == View.GONE) {
                stateTextView[posInt].setVisibility(View.VISIBLE);
            }
            new MyCountTime(1000, 500) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    if (stateTextView[posInt].getVisibility() == View.VISIBLE) {
                        stateTextView[posInt].setVisibility(View.GONE);
                        stateTextView[posInt].startAnimation(mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}