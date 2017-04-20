package top.yokey.gxsfxy.activity.dynamic;

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
import android.view.View;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.adapter.ActivityListAdapter;
import top.yokey.gxsfxy.adapter.BasePagerAdapter;
import top.yokey.gxsfxy.adapter.CircleListAdapter;
import top.yokey.gxsfxy.adapter.TopicListAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class DynamicActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton createButton;

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

    private int activityPageInt;//返回 & 销毁 Activity
    private TextView activityTipsTextView;
    private RecyclerView activityListView;
    private TextView activityStateTextView;
    private ActivityListAdapter activityAdapter;
    private SwipeRefreshLayout activitySwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> activityArrayList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_dynamic);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        mTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        createButton = (FloatingActionButton) findViewById(R.id.createButton);

    }

    private void initData() {

        List<View> mViewList = new ArrayList<>();
        mViewList.add(MainActivity.mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        mViewList.add(MainActivity.mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        mViewList.add(MainActivity.mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        //mViewList.add(MainActivity.mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        //mViewList.add(MainActivity.mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));

        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("圈子");
        mTitleList.add("话题");
        mTitleList.add("活动");
        //mTitleList.add("提问");
        //mTitleList.add("表白墙");

        //圈子
        circlePageInt = 1;
        circleArrayList = new ArrayList<>();
        circleAdapter = new CircleListAdapter(MainActivity.mApplication, MainActivity.mActivity, circleArrayList);
        circleTipsTextView = (TextView) mViewList.get(0).findViewById(R.id.tipsTextView);
        circleStateTextView = (TextView) mViewList.get(0).findViewById(R.id.stateTextView);
        circleListView = (RecyclerView) mViewList.get(0).findViewById(R.id.mainListView);
        circleSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(0).findViewById(R.id.mainSwipeRefreshLayout);
        circleListView.setLayoutManager(new LinearLayoutManager(MainActivity.mActivity));
        ControlUtil.setSwipeRefreshLayout(circleSwipeRefreshLayout);
        circleListView.setAdapter(circleAdapter);

        //话题
        topicPageInt = 1;
        topicArrayList = new ArrayList<>();
        topicAdapter = new TopicListAdapter(MainActivity.mApplication, MainActivity.mActivity, topicArrayList);
        topicTipsTextView = (TextView) mViewList.get(1).findViewById(R.id.tipsTextView);
        topicStateTextView = (TextView) mViewList.get(1).findViewById(R.id.stateTextView);
        topicListView = (RecyclerView) mViewList.get(1).findViewById(R.id.mainListView);
        topicSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(1).findViewById(R.id.mainSwipeRefreshLayout);
        topicListView.setLayoutManager(new LinearLayoutManager(MainActivity.mActivity));
        ControlUtil.setSwipeRefreshLayout(topicSwipeRefreshLayout);
        topicListView.setAdapter(topicAdapter);

        //活动
        activityPageInt = 1;
        activityArrayList = new ArrayList<>();
        activityAdapter = new ActivityListAdapter(MainActivity.mApplication, MainActivity.mActivity, activityArrayList);
        activityTipsTextView = (TextView) mViewList.get(2).findViewById(R.id.tipsTextView);
        activityStateTextView = (TextView) mViewList.get(2).findViewById(R.id.stateTextView);
        activityListView = (RecyclerView) mViewList.get(2).findViewById(R.id.mainListView);
        activitySwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(2).findViewById(R.id.mainSwipeRefreshLayout);
        activityListView.setLayoutManager(new LinearLayoutManager(MainActivity.mActivity));
        ControlUtil.setSwipeRefreshLayout(activitySwipeRefreshLayout);
        activityListView.setAdapter(activityAdapter);

        ControlUtil.setTabLayout(MainActivity.mActivity, mTabLayout, new BasePagerAdapter(mViewList, mTitleList), mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        getCircle();
        getTopic();
        getActivity();

    }

    private void initEven() {

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        intent = new Intent(MainActivity.mActivity, CircleCreateActivity.class);
                        MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.mActivity, TopicCreateActivity.class);
                        MainActivity.mApplication.startActivityWithLoginSuccess(MainActivity.mActivity, intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.mActivity, ActivityCreateActivity.class);
                        MainActivity.mApplication.startActivity(MainActivity.mActivity, intent);
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

        circleAdapter.setOnItemDelClickListener(new CircleListAdapter.onItemDelClickListener() {
            @Override
            public void onItemDelClick() {
                if (circleArrayList.isEmpty()) {
                    circleTipsTextView.setText("暂无圈子内容");
                    circleStateTextView.setVisibility(View.GONE);
                    circleTipsTextView.setVisibility(View.VISIBLE);
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

        topicAdapter.setOnItemDelClickListener(new TopicListAdapter.onItemDelClickListener() {
            @Override
            public void onItemDelClick() {
                if (topicArrayList.isEmpty()) {
                    topicTipsTextView.setText("暂无话题内容");
                    topicStateTextView.setVisibility(View.GONE);
                    topicTipsTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        activityTipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityTipsTextView.getText().toString().contains("点击重试")) {
                    activityPageInt = 1;
                    getActivity();
                }
            }
        });

        activitySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activityPageInt = 1;
                        getActivity();
                    }
                }, 1000);
            }
        });

        activityListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        getActivity();
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

        BaseAjaxParams ajaxParams = new BaseAjaxParams(MainActivity.mApplication, "base", "dynamicList");
        ajaxParams.put("page", circlePageInt + "");
        ajaxParams.put("type", "circle");

        MainActivity.mApplication.mFinalHttp.post(MainActivity.mApplication.apiUrlString + "c=base&a=dynamicList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = MainActivity.mApplication.getJsonData(o.toString());
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
                                            circleStateTextView.startAnimation(MainActivity.mApplication.goneAlphaAnimation);
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
                        circleStateTextView.startAnimation(MainActivity.mApplication.goneAlphaAnimation);
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

        BaseAjaxParams ajaxParams = new BaseAjaxParams(MainActivity.mApplication, "base", "dynamicList");
        ajaxParams.put("page", topicPageInt + "");
        ajaxParams.put("type", "topic");

        MainActivity.mApplication.mFinalHttp.post(MainActivity.mApplication.apiUrlString + "c=base&a=dynamicList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = MainActivity.mApplication.getJsonData(o.toString());
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
                                            topicStateTextView.startAnimation(MainActivity.mApplication.goneAlphaAnimation);
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
                        topicStateTextView.startAnimation(MainActivity.mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

    private void getActivity() {

        if (activityPageInt == 1) {
            if (activityArrayList.isEmpty()) {
                activityTipsTextView.setText("加载中...");
                activityStateTextView.setVisibility(View.GONE);
                activityTipsTextView.setVisibility(View.VISIBLE);
            }
        } else {
            activityStateTextView.setText("加载中...");
            activityTipsTextView.setVisibility(View.GONE);
            activityStateTextView.setVisibility(View.VISIBLE);
        }

        BaseAjaxParams ajaxParams = new BaseAjaxParams(MainActivity.mApplication, "base", "activityList");
        ajaxParams.put("page", activityPageInt + "");

        MainActivity.mApplication.mFinalHttp.post(MainActivity.mApplication.apiUrlString + "c=base&a=activityList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = MainActivity.mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            if (activityPageInt == 1) {
                                activityTipsTextView.setText("暂无话题内容");
                                activityStateTextView.setVisibility(View.GONE);
                                activityTipsTextView.setVisibility(View.VISIBLE);
                            } else {
                                activityStateTextView.setText("没有更多了");
                                activityTipsTextView.setVisibility(View.GONE);
                                if (activityStateTextView.getVisibility() == View.GONE) {
                                    activityStateTextView.setVisibility(View.VISIBLE);
                                }
                                new MyCountTime(1000, 500) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        if (activityStateTextView.getVisibility() == View.VISIBLE) {
                                            activityStateTextView.setVisibility(View.GONE);
                                            activityStateTextView.startAnimation(MainActivity.mApplication.goneAlphaAnimation);
                                        }
                                    }
                                }.start();
                            }
                        } else {
                            if (activityPageInt == 1) {
                                activityArrayList.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                activityArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            activityStateTextView.setVisibility(View.GONE);
                            activityTipsTextView.setVisibility(View.GONE);
                            activityPageInt++;
                        }
                        activitySwipeRefreshLayout.setRefreshing(false);
                        activityAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getActivityFailure();
                    }
                } else {
                    getActivityFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getActivityFailure();
            }
        });

    }

    private void getActivityFailure() {

        activitySwipeRefreshLayout.setRefreshing(false);
        activityAdapter.notifyDataSetChanged();

        if (activityPageInt == 1) {
            activityTipsTextView.setText("读取数据失败\n\n点击重试");
            activityStateTextView.setVisibility(View.GONE);
            activityTipsTextView.setVisibility(View.VISIBLE);
        } else {
            activityStateTextView.setText("读取数据失败");
            activityTipsTextView.setVisibility(View.GONE);
            if (activityStateTextView.getVisibility() == View.GONE) {
                activityStateTextView.setVisibility(View.VISIBLE);
            }
            new MyCountTime(1000, 500) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    if (activityStateTextView.getVisibility() == View.VISIBLE) {
                        activityStateTextView.setVisibility(View.GONE);
                        activityStateTextView.startAnimation(MainActivity.mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

}