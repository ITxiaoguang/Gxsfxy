package top.yokey.gxsfxy.activity.admin;

import android.os.Bundle;
import android.os.Handler;
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

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.adapter.AdminFeedbackListAdapter;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class AdminFeedbackActivity extends AppCompatActivity {

    private TextView tipsTextView;
    private TextView stateTextView;
    private RecyclerView mListView;
    private AdminFeedbackListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> mArrayList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_admin_feedback);
        initView();
        initData();
        initEven();
    }

    private void initView() {

        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

    }

    private void initData() {

        mArrayList = new ArrayList<>();
        mAdapter = new AdminFeedbackListAdapter(AdminMainActivity.mApplication, AdminMainActivity.mActivity, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(AdminMainActivity.mActivity));
        mListView.setAdapter(mAdapter);

        ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout);
        stateTextView.setVisibility(View.GONE);
        getFeedbackList();

    }

    private void initEven() {

        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipsTextView.getText().toString().contains("点击重试")) {
                    getFeedbackList();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFeedbackList();
                    }
                }, 1000);
            }
        });

    }

    private void getFeedbackList() {

        if (mArrayList.isEmpty()) {
            tipsTextView.setText("加载中...");
            tipsTextView.setVisibility(View.VISIBLE);
        }

        UserAjaxParams ajaxParams = new UserAjaxParams(AdminMainActivity.mApplication, "adminMessage", "feedbackList");

        AdminMainActivity.mApplication.mFinalHttp.post(AdminMainActivity.mApplication.apiUrlString + "c=adminMessage&a=feedbackList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    mArrayList.clear();
                    JSONArray jsonArray = new JSONArray(AdminMainActivity.mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                    }
                    if (mArrayList.isEmpty()) {
                        tipsTextView.setVisibility(View.VISIBLE);
                        tipsTextView.setText("暂无意见建议!");
                    } else {
                        tipsTextView.setVisibility(View.GONE);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    getFeedbackListFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getFeedbackListFailure();
            }
        });

    }

    private void getFeedbackListFailure() {

        tipsTextView.setText("读取数据失败\n\n点击重试！");
        tipsTextView.setVisibility(View.VISIBLE);

    }

}