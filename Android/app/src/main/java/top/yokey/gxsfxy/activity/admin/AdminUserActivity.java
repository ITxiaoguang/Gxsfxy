package top.yokey.gxsfxy.activity.admin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.adapter.AdminUserListAdapter;
import top.yokey.gxsfxy.system.MyCountTime;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class AdminUserActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageView searchImageView;

    private int pageInt;
    private String keyword;
    private TextView tipsTextView;
    private TextView stateTextView;
    private RecyclerView mListView;
    private AdminUserListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> mArrayList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_admin_user);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);

        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

    }

    private void initData() {

        pageInt = 1;
        keyword = "";

        mArrayList = new ArrayList<>();
        mAdapter = new AdminUserListAdapter(AdminMainActivity.mApplication, AdminMainActivity.mActivity, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(AdminMainActivity.mActivity));
        mListView.setAdapter(mAdapter);

        ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout);
        getJson();

    }

    private void initEven() {

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = searchEditText.getText().toString();
                pageInt = 1;
                getJson();
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

        String url;
        UserAjaxParams ajaxParams;

        if (TextUtil.isEmpty(keyword)) {
            url = AdminMainActivity.mApplication.apiUrlString + "c=adminUser&a=userList";
            ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "adminUser", "userList");
            ajaxParams.put("page", pageInt + "");
        } else {
            url = AdminMainActivity.mApplication.apiUrlString + "c=adminUser&a=userSearch";
            ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "adminUser", "userSearch");
            ajaxParams.put("page", pageInt + "");
            ajaxParams.put("keyword", keyword);
        }

        AdminMainActivity.mApplication.mFinalHttp.post(url, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = AdminMainActivity.mApplication.getJsonData(o.toString());
                if (!TextUtil.isEmpty(data)) {
                    try {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            if (pageInt == 1) {
                                tipsTextView.setText("暂无用户");
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
                                            stateTextView.startAnimation(AdminMainActivity.mApplication.goneAlphaAnimation);
                                        }
                                    }
                                }.start();
                            }
                        } else {
                            if (pageInt == 1) {
                                mArrayList.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, String> hashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i)));
                                hashMap.put("keyword", keyword);
                                mArrayList.add(hashMap);
                            }
                            stateTextView.setVisibility(View.GONE);
                            tipsTextView.setVisibility(View.GONE);
                            pageInt++;
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        AndroidUtil.hideKeyboard(mListView);
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
        AndroidUtil.hideKeyboard(mListView);
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
                        stateTextView.startAnimation(AdminMainActivity.mApplication.goneAlphaAnimation);
                    }
                }
            }.start();
        }

    }

}