package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.dynamic.ActivityCreateActivity;
import top.yokey.gxsfxy.activity.dynamic.CircleCreateActivity;
import top.yokey.gxsfxy.activity.dynamic.TopicCreateActivity;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.BasePagerAdapter;
import top.yokey.gxsfxy.adapter.CircleListAdapter;
import top.yokey.gxsfxy.adapter.TopicListAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class UserDynamicActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton createButton;

    private String user_id;
    private int circlePageInt;
    private TextView circleTipsTextView;
    private RecyclerView circleListView;
    private TextView circleStateTextView;
    private CircleListAdapter circleAdapter;
    private SwipeRefreshLayout circleSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> circleArrayList;

    private int topicPageInt;
    private TextView topicTipsTextView;
    private RecyclerView topicListView;
    private TextView topicStateTextView;
    private TopicListAdapter topicAdapter;
    private SwipeRefreshLayout topicSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> topicArrayList;

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
        setContentView(R.layout.activity_user_dynamic);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.backImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        mTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        createButton = (FloatingActionButton) findViewById(R.id.createButton);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        user_id = mActivity.getIntent().getStringExtra("user_id");
        if (TextUtil.isEmpty(user_id)) {
            ToastUtil.show(mActivity, "传入参数有误");
            mApplication.finishActivity(mActivity);
            return;
        }
        if (user_id.equals(mApplication.userHashMap.get("user_id"))) {
            titleTextView.setText("我的动态");
        } else {
            titleTextView.setText("他的动态");
        }

        List<View> mViewList = new ArrayList<>();
        mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));

        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("圈子");
        mTitleList.add("话题");

        //圈子
        circlePageInt = 1;
        circleArrayList = new ArrayList<>();
        circleAdapter = new CircleListAdapter(mApplication, mActivity, circleArrayList);
        circleTipsTextView = (TextView) mViewList.get(0).findViewById(R.id.tipsTextView);
        circleStateTextView = (TextView) mViewList.get(0).findViewById(R.id.stateTextView);
        circleListView = (RecyclerView) mViewList.get(0).findViewById(R.id.mainListView);
        circleSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(0).findViewById(R.id.mainSwipeRefreshLayout);
        circleListView.setLayoutManager(new LinearLayoutManager(mActivity));
        ControlUtil.setSwipeRefreshLayout(circleSwipeRefreshLayout);
        circleListView.setAdapter(circleAdapter);

        //话题
        topicPageInt = 1;
        topicArrayList = new ArrayList<>();
        topicAdapter = new TopicListAdapter(mApplication, mActivity, topicArrayList);
        topicTipsTextView = (TextView) mViewList.get(1).findViewById(R.id.tipsTextView);
        topicStateTextView = (TextView) mViewList.get(1).findViewById(R.id.stateTextView);
        topicListView = (RecyclerView) mViewList.get(1).findViewById(R.id.mainListView);
        topicSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(1).findViewById(R.id.mainSwipeRefreshLayout);
        topicListView.setLayoutManager(new LinearLayoutManager(mActivity));
        ControlUtil.setSwipeRefreshLayout(topicSwipeRefreshLayout);
        topicListView.setAdapter(topicAdapter);

        ControlUtil.setTabLayout(mActivity, mTabLayout, new BasePagerAdapter(mViewList, mTitleList), mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        getCircle();
        getTopic();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        intent = new Intent(mActivity, CircleCreateActivity.class);
                        mApplication.startActivityWithLoginSuccess(mActivity, intent);
                        break;
                    case 1:
                        intent = new Intent(mActivity, TopicCreateActivity.class);
                        mApplication.startActivityWithLoginSuccess(mActivity, intent);
                        break;
                    case 2:
                        intent = new Intent(mActivity, ActivityCreateActivity.class);
                        mApplication.startActivity(mActivity, intent);
                        break;
                }
            }
        });

        circleTipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (circleTipsTextView.getText().toString().contains("点击重试")) {
                    circlePageInt = 1;
                    getCircle();
                }
            }
        });

        circleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circlePageInt = 1;
                        getCircle();
                    }
                }, 1000);
            }
        });

        circleListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        getCircle();
                    }
                }
            }
        });

        topicTipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topicTipsTextView.getText().toString().contains("点击重试")) {
                    topicPageInt = 1;
                    getTopic();
                }
            }
        });

        topicSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        topicPageInt = 1;
                        getTopic();
                    }
                }, 1000);
            }
        });

        topicListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        getTopic();
                    }
                }
            }
        });

    }

    private void getCircle() {

        if (circlePageInt == 1) {
            if (circleArrayList.isEmpty()) {
                circleTipsTextView.setText("加载中...");
                circleStateTextView.setVisibility(View.GONE);
                circleTipsTextView.setVisibility(View.VISIBLE);
            }
        } else {
            circleStateTextView.setText("加载中...");
            circleTipsTextView.setVisibility(View.GONE);
            circleStateTextView.setVisibility(View.VISIBLE);
        }

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "dynamicListUser");
        ajaxParams.put("page", circlePageInt + "");
        ajaxParams.put("user_id", user_id);
        ajaxParams.put("type", "circle");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=dynamicListUser", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            if (circlePageInt == 1) {
                                circleTipsTextView.setText("暂无圈子内容");
                                circleStateTextView.setVisibility(View.GONE);
                                circleTipsTextView.setVisibility(View.VISIBLE);
                            } else {
                                circleStateTextView.setText("没有更多了");
                                circleTipsTextView.setVisibility(View.GONE);
                                if (circleStateTextView.getVisibility() == View.GONE) {
                                    circleStateTextView.setVisibility(View.VISIBLE);
                                }
                                new MyCountTime(1000, 500) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        if (circleStateTextView.getVisibility() == View.VISIBLE) {
                                            circleStateTextView.setVisibility(View.GONE);
                                            circleStateTextView.startAnimation(mApplication.goneAlphaAnimation);
                                        }
                                    }
                                }.start();
                            }
                        } else {
                            if (circlePageInt == 1) {
                                circleArrayList.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                circleArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            circleStateTextView.setVisibility(View.GONE);
                            circleTipsTextView.setVisibility(View.GONE);
                            circlePageInt++;
                        }
                        circleSwipeRefreshLayout.setRefreshing(false);
                        circleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getCircleFailure();
                    }
                } else {
                    getCircleFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getCircleFailure();
            }
        });

    }

    private void getCircleFailure() {

        circleSwipeRefreshLayout.setRefreshing(false);
        circleAdapter.notifyDataSetChanged();

        if (circlePageInt == 1) {
            circleTipsTextView.setText("读取数据失败\n\n点击重试");
            circleStateTextView.setVisibility(View.GONE);
            circleTipsTextView.setVisibility(View.VISIBLE);
        } else {
            circleStateTextView.setText("读取数据失败");
            circleTipsTextView.setVisibility(View.GONE);
            if (circleStateTextView.getVisibility() == View.GONE) {
                circleStateTextView.setVisibility(View.VISIBLE);
            }
            new MyCountTime(1000, 500) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    if (circleStateTextView.getVisibility() == View.VISIBLE) {
                        circleStateTextView.setVisibility(View.GONE);
                        circleStateTextView.startAnimation(mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

    private void getTopic() {

        if (topicPageInt == 1) {
            if (topicArrayList.isEmpty()) {
                topicTipsTextView.setText("加载中...");
                topicStateTextView.setVisibility(View.GONE);
                topicTipsTextView.setVisibility(View.VISIBLE);
            }
        } else {
            topicStateTextView.setText("加载中...");
            topicTipsTextView.setVisibility(View.GONE);
            topicStateTextView.setVisibility(View.VISIBLE);
        }

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "dynamicListUser");
        ajaxParams.put("page", topicPageInt + "");
        ajaxParams.put("user_id", user_id);
        ajaxParams.put("type", "topic");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=dynamicListUser", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            if (topicPageInt == 1) {
                                topicTipsTextView.setText("暂无话题内容");
                                topicStateTextView.setVisibility(View.GONE);
                                topicTipsTextView.setVisibility(View.VISIBLE);
                            } else {
                                topicStateTextView.setText("没有更多了");
                                topicTipsTextView.setVisibility(View.GONE);
                                if (topicStateTextView.getVisibility() == View.GONE) {
                                    topicStateTextView.setVisibility(View.VISIBLE);
                                }
                                new MyCountTime(1000, 500) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        if (topicStateTextView.getVisibility() == View.VISIBLE) {
                                            topicStateTextView.setVisibility(View.GONE);
                                            topicStateTextView.startAnimation(mApplication.goneAlphaAnimation);
                                        }
                                    }
                                }.start();
                            }
                        } else {
                            if (topicPageInt == 1) {
                                topicArrayList.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                topicArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            topicStateTextView.setVisibility(View.GONE);
                            topicTipsTextView.setVisibility(View.GONE);
                            topicPageInt++;
                        }
                        topicSwipeRefreshLayout.setRefreshing(false);
                        topicAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getTopicFailure();
                    }
                } else {
                    getTopicFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getTopicFailure();
            }
        });

    }

    private void getTopicFailure() {

        topicSwipeRefreshLayout.setRefreshing(false);
        topicAdapter.notifyDataSetChanged();

        if (topicPageInt == 1) {
            topicTipsTextView.setText("读取数据失败\n\n点击重试");
            topicStateTextView.setVisibility(View.GONE);
            topicTipsTextView.setVisibility(View.VISIBLE);
        } else {
            topicStateTextView.setText("读取数据失败");
            topicTipsTextView.setVisibility(View.GONE);
            if (topicStateTextView.getVisibility() == View.GONE) {
                topicStateTextView.setVisibility(View.VISIBLE);
            }
            new MyCountTime(1000, 500) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    if (topicStateTextView.getVisibility() == View.VISIBLE) {
                        topicStateTextView.setVisibility(View.GONE);
                        topicStateTextView.startAnimation(mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}