package top.yokey.gxsfxy.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.NoticeListAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class NoticeActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private int pageInt;
    private TextView tipsTextView;
    private TextView stateTextView;
    private RecyclerView mListView;
    private NoticeListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> mArrayList;

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
        setContentView(R.layout.activity_notice);
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

        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        pageInt = 1;
        titleTextView.setText("通知公告");

        mArrayList = new ArrayList<>();
        mAdapter = new NoticeListAdapter(mApplication, mActivity, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);

        ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout);
        getJson();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });


        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipsTextView.getText().toString().contains("点击重试")) {
                    pageInt = 1;
                    getJson();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageInt = 1;
                        getJson();
                    }
                }, 1000);
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void getJson() {

        if (pageInt == 1) {
            if (mArrayList.isEmpty()) {
                tipsTextView.setText("加载中...");
                stateTextView.setVisibility(View.GONE);
                tipsTextView.setVisibility(View.VISIBLE);
            }
        } else {
            stateTextView.setText("加载中...");
            tipsTextView.setVisibility(View.GONE);
            stateTextView.setVisibility(View.VISIBLE);
        }

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "newsList");
        ajaxParams.put("type", "通知公告");
        ajaxParams.put("page", pageInt + "");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=newsList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            if (pageInt == 1) {
                                tipsTextView.setText("暂无通知公告");
                                stateTextView.setVisibility(View.GONE);
                                tipsTextView.setVisibility(View.VISIBLE);
                            } else {
                                stateTextView.setText("没有更多了");
                                tipsTextView.setVisibility(View.GONE);
                                if (stateTextView.getVisibility() == View.GONE) {
                                    stateTextView.setVisibility(View.VISIBLE);
                                }
                                new MyCountTime(1000, 500) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        if (stateTextView.getVisibility() == View.VISIBLE) {
                                            stateTextView.setVisibility(View.GONE);
                                            stateTextView.startAnimation(mApplication.goneAlphaAnimation);
                                        }
                                    }
                                }.start();
                            }
                        } else {
                            if (pageInt == 1) {
                                mArrayList.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            stateTextView.setVisibility(View.GONE);
                            tipsTextView.setVisibility(View.GONE);
                            pageInt++;
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
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

        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();

        if (pageInt == 1) {
            tipsTextView.setText("读取数据失败\n\n点击重试");
            stateTextView.setVisibility(View.GONE);
            tipsTextView.setVisibility(View.VISIBLE);
        } else {
            stateTextView.setText("读取数据失败");
            tipsTextView.setVisibility(View.GONE);
            if (stateTextView.getVisibility() == View.GONE) {
                stateTextView.setVisibility(View.VISIBLE);
            }
            new MyCountTime(1000, 500) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    if (stateTextView.getVisibility() == View.VISIBLE) {
                        stateTextView.setVisibility(View.GONE);
                        stateTextView.startAnimation(mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}