package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.adapter.SearchListAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class SearchActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private EditText searchEditText;
    private ImageView searchImageView;

    private TextView tipsTextView;
    private TextView stateTextView;
    private RecyclerView mListView;
    private SearchListAdapter mAdapter;
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
        setContentView(R.layout.activity_search);
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
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);

        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        tipsTextView.setText("输入关键字搜索\n\n");
        tipsTextView.setVisibility(View.VISIBLE);
        stateTextView.setVisibility(View.GONE);

        mArrayList = new ArrayList<>();
        mAdapter = new SearchListAdapter(mApplication, mActivity, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);

        ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.hideKeyboard(view);
                search();
            }
        });

    }

    private void search() {

        final String keyword = searchEditText.getText().toString();

        if (TextUtil.isEmpty(keyword)) {
            ToastUtil.show(mActivity, "关键字不能为空");
            return;
        }

        tipsTextView.setText("搜索中\n\n请稍后...");
        tipsTextView.setVisibility(View.VISIBLE);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "search");
        ajaxParams.put("uniKeyword", TextUtil.replaceFace(searchEditText.getText()));
        ajaxParams.put("keyword", keyword);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=search", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                String data = mApplication.getJsonData(o.toString());
                if (data.contains("\"user_list\":[]") && data.contains("\"dynamic_list\":[]") && data.contains("\"phone_list\":[]") && data.contains("\"activity_list\":[]")) {
                    tipsTextView.setText("暂无结果\n\n换个关键字试试吧");
                } else {
                    try {
                        mArrayList.clear();
                        mAdapter.notifyDataSetChanged();
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("user_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i)));
                            hashMap.put("search_type", "user");
                            hashMap.put("keyword", keyword);
                            if (i == 0) {
                                hashMap.put("search_title", "1");
                            } else {
                                hashMap.put("search_title", "0");
                            }
                            mArrayList.add(hashMap);
                        }
                        jsonArray = new JSONArray(jsonObject.getString("phone_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i)));
                            hashMap.put("search_type", "phone");
                            hashMap.put("keyword", keyword);
                            if (i == 0) {
                                hashMap.put("search_title", "1");
                            } else {
                                hashMap.put("search_title", "0");
                            }
                            mArrayList.add(hashMap);
                        }
                        jsonArray = new JSONArray(jsonObject.getString("dynamic_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i)));
                            hashMap.put("search_type", "dynamic");
                            hashMap.put("keyword", keyword);
                            if (i == 0) {
                                hashMap.put("search_title", "1");
                            } else {
                                hashMap.put("search_title", "0");
                            }
                            mArrayList.add(hashMap);
                        }
                        jsonArray = new JSONArray(jsonObject.getString("activity_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> hashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i)));
                            hashMap.put("search_type", "activity");
                            hashMap.put("keyword", keyword);
                            if (i == 0) {
                                hashMap.put("search_title", "1");
                            } else {
                                hashMap.put("search_title", "0");
                            }
                            mArrayList.add(hashMap);
                        }
                        tipsTextView.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        ToastUtil.showFailure(mActivity);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.showFailure(mActivity);
                DialogUtil.cancel();
            }
        });

    }

    private void returnActivity() {

        if (searchEditText.getText().toString().length() != 0) {
            searchEditText.setText("");
        } else {
            mApplication.finishActivity(mActivity);
        }

    }

}