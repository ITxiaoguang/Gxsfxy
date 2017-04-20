package top.yokey.gxsfxy.activity.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MainActivity;
import top.yokey.gxsfxy.adapter.MessageListAdapter;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.TimeUtil;

public class MessageActivity extends AppCompatActivity {

    private boolean firstBoolean;
    private TextView loginTextView;
    public static boolean inBoolean;
    private RelativeLayout loginRelativeLayout;

    private TextView stateTextView;
    private static TextView tipsTextView;
    private static RecyclerView mListView;
    private static MessageListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static ArrayList<HashMap<String, String>> mArrayList;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_message);
        initView();
        initData();
        initEven();
    }

    @Override
    protected void onPause() {
        super.onPause();
        inBoolean = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        inBoolean = true;
        if (!TextUtil.isEmpty(MainActivity.mApplication.userTokenString)) {
            mAdapter.notifyDataSetChanged();
            loginRelativeLayout.setVisibility(View.GONE);
            if (firstBoolean) {
                getMessageList();
                firstBoolean = false;
            }
        } else {
            if (loginRelativeLayout.getVisibility() == View.GONE) {
                mArrayList.clear();
                firstBoolean = true;
                mAdapter.notifyDataSetChanged();
                loginRelativeLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initView() {

        loginRelativeLayout = (RelativeLayout) findViewById(R.id.loginRelativeLayout);
        loginTextView = (TextView) findViewById(R.id.loginTextView);

        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

    }

    private void initData() {

        inBoolean = false;
        firstBoolean = true;

        stateTextView.setVisibility(View.GONE);

        mArrayList = new ArrayList<>();
        mAdapter = new MessageListAdapter(MainActivity.mApplication, MainActivity.mActivity, mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(MainActivity.mActivity));
        mListView.setAdapter(mAdapter);

        ControlUtil.setSwipeRefreshLayout(mSwipeRefreshLayout);
        stateTextView.setVisibility(View.GONE);

    }

    private void initEven() {

        loginRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityLogin(MainActivity.mActivity);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mApplication.startActivityLogin(MainActivity.mActivity);
            }
        });

        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipsTextView.getText().toString().contains("点击重试")) {
                    getMessageList();
                }
            }
        });

        mAdapter.setOnItemLongClickListener(new MessageListAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick() {
                if (mArrayList.isEmpty()) {
                    tipsTextView.setVisibility(View.VISIBLE);
                    tipsTextView.setText("暂无聊天消息\n\n快找个人聊聊吧!");
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMessageList();
                    }
                }, 1000);
            }
        });

    }

    private void getMessageList() {

        if (mArrayList.isEmpty()) {
            tipsTextView.setText("加载中...");
            tipsTextView.setVisibility(View.VISIBLE);
        }

        UserAjaxParams ajaxParams = new UserAjaxParams(MainActivity.mApplication, "userMessage", "messageList");
        MainActivity.mApplication.mFinalHttp.post(MainActivity.mApplication.apiUrlString + "c=userMessage&a=messageList", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    mArrayList.clear();
                    JSONArray jsonArray = new JSONArray(MainActivity.mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hashMap = new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i)));
                        if (!MainActivity.mApplication.mSharedPreferences.getBoolean("message_list_remove_" + hashMap.get("user_id"), false)) {
                            mArrayList.add(hashMap);
                        }
                    }
                    if (mArrayList.isEmpty()) {
                        tipsTextView.setVisibility(View.VISIBLE);
                        tipsTextView.setText("暂无聊天消息\n\n快找个人聊聊吧!");
                    } else {
                        tipsTextView.setVisibility(View.GONE);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    getMessageListFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                getMessageListFailure();
            }
        });

    }

    private void getMessageListFailure() {

        tipsTextView.setText("读取数据失败\n\n点击重试！");
        tipsTextView.setVisibility(View.VISIBLE);

    }

    public static void updateMessageList(String uid, String nickname, String avatar, String type, String content) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message_id", "-1");
        hashMap.put("message_uid", "-1");
        hashMap.put("message_tid", "-1");
        hashMap.put("message_type", type);
        hashMap.put("message_content", content);
        hashMap.put("message_time", TimeUtil.getAll());
        hashMap.put("user_id", uid);
        hashMap.put("user_mobile", "-1");
        hashMap.put("user_college", "");
        hashMap.put("user_professional", "");
        hashMap.put("user_class", "");
        hashMap.put("nick_name", nickname);
        hashMap.put("user_avatar", avatar);
        hashMap.put("user_gender", "");
        hashMap.put("user_sign", "");
        arrayList.add(hashMap);

        for (int i = 0; i < mArrayList.size(); i++) {
            if (!mArrayList.get(i).get("user_id").equals(uid)) {
                arrayList.add(mArrayList.get(i));
            } else {
                int number = MainActivity.mApplication.mSharedPreferences.getInt("message_list_number_" + hashMap.get("user_id"), 0) + 1;
                MainActivity.mApplication.mSharedPreferencesEditor.putInt("message_list_number_" + hashMap.get("user_id"), number).apply();
            }
        }

        mArrayList.clear();
        mArrayList.addAll(arrayList);
        mAdapter.notifyDataSetChanged();
        tipsTextView.setVisibility(View.GONE);

    }

}