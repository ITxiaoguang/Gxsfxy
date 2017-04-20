package top.yokey.gxsfxy.activity.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.FollowMineListAdapter;
import top.yokey.gxsfxy.control.SideBarView;
import top.yokey.gxsfxy.system.CharacterParser;
import top.yokey.gxsfxy.system.FriendBean;
import top.yokey.gxsfxy.system.PinyinComparator;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.TextUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class FollowMineActivity extends AppCompatActivity implements SectionIndexer {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private String user_id;
    private ListView mListView;
    private int lastFirstInt = -1;
    private TextView tipsTextView;
    private List<FriendBean> mList;
    private TextView catalogTextView;
    private SideBarView mSideBarView;
    private LinearLayout mLinearLayout;
    private FollowMineListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            returnActivity();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public int getSectionForPosition(int position) {
        return mList.get(position).getLetters().charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mList.size(); i++) {
            String sortStr = mList.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mine_follow);
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

        mListView = (ListView) findViewById(R.id.mainListView);
        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        catalogTextView = (TextView) findViewById(R.id.catalogTextView);
        mSideBarView = (SideBarView) findViewById(R.id.mainSideBarView);
        mLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        mSideBarView.setTextView((TextView) findViewById(R.id.dialogTextView));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

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
            titleTextView.setText("关注我的");
        } else {
            titleTextView.setText("关注他的");
        }

        mList = new ArrayList<>();
        mAdapter = new FollowMineListAdapter(mApplication, mActivity, mList);
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
                    getJson();
                }
            }
        });

        mSideBarView.setOnTouchingLetterChangedListener(new SideBarView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (mList != null) {
                    if (mList.size() != 0) {
                        int nextSection;
                        int section = getSectionForPosition(firstVisibleItem);
                        if (mList.size() == 1) {
                            nextSection = getSectionForPosition(firstVisibleItem);
                        } else {
                            nextSection = getSectionForPosition(firstVisibleItem + 1);
                        }
                        int nextSecPosition = getPositionForSection(+nextSection);
                        if (firstVisibleItem != lastFirstInt) {
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLinearLayout.getLayoutParams();
                            params.topMargin = 0;
                            mLinearLayout.setLayoutParams(params);
                            catalogTextView.setText(mList.get(getPositionForSection(section)).getLetters());
                        }
                        if (nextSecPosition == firstVisibleItem + 1) {
                            View childView = view.getChildAt(0);
                            if (childView != null) {
                                int titleHeight = mLinearLayout.getHeight();
                                int bottom = childView.getBottom();
                                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLinearLayout.getLayoutParams();
                                if (bottom < titleHeight) {
                                    float pushedDistance = bottom - titleHeight;
                                    params.topMargin = (int) pushedDistance;
                                    mLinearLayout.setLayoutParams(params);
                                } else {
                                    if (params.topMargin != 0) {
                                        params.topMargin = 0;
                                        mLinearLayout.setLayoutParams(params);
                                    }
                                }
                            }
                        }
                        lastFirstInt = firstVisibleItem;
                    }
                }
            }

        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJson();
                    }
                }, 1000);
            }
        });

    }

    private void getJson() {

        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "userFollow", "followMine");
        ajaxParams.put("user_id", user_id);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=userFollow&a=followMine", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                String data = mApplication.getJsonData(o.toString());
                if (TextUtil.isEmpty(data) || data.equals("[]")) {
                    tipsTextView.setText("暂时没有关注的人");
                    tipsTextView.setVisibility(View.VISIBLE);
                } else {
                    try {
                        //解析数据
                        mList.clear();
                        JSONArray jsonArray = new JSONArray(data);
                        CharacterParser characterParser = CharacterParser.getInstance();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            jsonObject = new JSONObject(jsonObject.getString("user_info"));
                            FriendBean friendBean = new FriendBean();
                            friendBean.setId(jsonObject.getString("user_id"));
                            friendBean.setAvatar(jsonObject.getString("user_avatar"));
                            friendBean.setNickname(jsonObject.getString("nick_name"));
                            friendBean.setGender(jsonObject.getString("user_gender"));
                            friendBean.setSign(jsonObject.getString("user_sign"));
                            String pinyin = characterParser.getSelling(friendBean.getNickname());
                            String sortString = pinyin.substring(0, 1).toUpperCase();
                            if (sortString.matches("[A-Z]")) {
                                friendBean.setLetters(sortString.toUpperCase());
                            } else {
                                friendBean.setLetters("#");
                            }
                            mList.add(friendBean);
                        }
                        Collections.sort(mList, new PinyinComparator());
                        catalogTextView.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        tipsTextView.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        tipsTextView.setText("数据加载失败\n\n点击重试");
                        tipsTextView.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                tipsTextView.setText("数据加载失败\n\n点击重试");
                tipsTextView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}