package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import top.yokey.gxsfxy.adapter.AreaListAdapter;
import top.yokey.gxsfxy.system.BaseAjaxParams;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class AreaActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String province_id;
    private String province;
    private String city;
    private String city_id;
    private String area;

    private ImageView backImageView;
    private TextView titleTextView;

    private RecyclerView provinceListView;
    private AreaListAdapter provinceAdapter;
    private ArrayList<HashMap<String, String>> provinceArrayList;

    private RecyclerView cityListView;
    private AreaListAdapter cityAdapter;
    private ArrayList<HashMap<String, String>> cityArrayList;

    private RecyclerView areaListView;
    private AreaListAdapter areaAdapter;
    private ArrayList<HashMap<String, String>> areaArrayList;

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
        setContentView(R.layout.activity_area);
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

        provinceListView = (RecyclerView) findViewById(R.id.provinceListView);
        cityListView = (RecyclerView) findViewById(R.id.cityListView);
        areaListView = (RecyclerView) findViewById(R.id.areaListView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        province_id = "";
        province = "";
        city_id = "";
        city = "";
        area = "";
        titleTextView.setText("地区选择");

        provinceArrayList = new ArrayList<>();
        provinceAdapter = new AreaListAdapter(provinceArrayList);
        provinceListView.setLayoutManager(new LinearLayoutManager(mActivity));
        provinceListView.setAdapter(provinceAdapter);

        cityArrayList = new ArrayList<>();
        cityAdapter = new AreaListAdapter(cityArrayList);
        cityListView.setLayoutManager(new LinearLayoutManager(mActivity));
        cityListView.setAdapter(cityAdapter);

        areaArrayList = new ArrayList<>();
        areaAdapter = new AreaListAdapter(areaArrayList);
        areaListView.setLayoutManager(new LinearLayoutManager(mActivity));
        areaListView.setAdapter(areaAdapter);

        getProvince();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        provinceAdapter.setOnItemClickListener(new AreaListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String area_id, String area_name) {
                province_id = area_id;
                province = area_name;
                getCity();
            }
        });

        cityAdapter.setOnItemClickListener(new AreaListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String area_id, String area_name) {
                city_id = area_id;
                city = area_name;
                getArea();
            }
        });

        areaAdapter.setOnItemClickListener(new AreaListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String area_id, String area_name) {
                area = area_name;
                Intent intent = new Intent();
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                mActivity.setResult(RESULT_OK, intent);
                mApplication.finishActivity(mActivity);
            }
        });

    }

    private void getProvince() {

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "getProvince");

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=getProvince", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                try {
                    provinceArrayList.clear();
                    JSONArray jsonArray = new JSONArray(mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        provinceArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                    }
                    provinceAdapter.notifyDataSetChanged();
                    provinceListView.setVisibility(View.VISIBLE);
                    cityListView.setVisibility(View.GONE);
                    areaListView.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getProvinceFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                getProvinceFailure();
            }
        });

    }

    private void getProvinceFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("确认您的选择")
                    .setMessage("读取省份数据失败,是否重试?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getProvince();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mApplication.finishActivity(mActivity);
                        }
                    })
                    .show();
        }

    }

    private void getCity() {

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "getCityArea");
        ajaxParams.put("parent_id", province_id);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=getCityArea", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                try {
                    cityArrayList.clear();
                    JSONArray jsonArray = new JSONArray(mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        cityArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                    }
                    cityAdapter.notifyDataSetChanged();
                    provinceListView.setVisibility(View.GONE);
                    cityListView.setVisibility(View.VISIBLE);
                    areaListView.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getCityFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                getCityFailure();
            }
        });

    }

    private void getCityFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("确认您的选择")
                    .setMessage("读取城市数据失败,是否重试?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getCity();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

    }

    private void getArea() {

        DialogUtil.progress(mActivity);

        BaseAjaxParams ajaxParams = new BaseAjaxParams(mApplication, "base", "getCityArea");
        ajaxParams.put("parent_id", city_id);

        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=base&a=getCityArea", ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                try {
                    areaArrayList.clear();
                    JSONArray jsonArray = new JSONArray(mApplication.getJsonData(o.toString()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        areaArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                    }
                    areaAdapter.notifyDataSetChanged();
                    provinceListView.setVisibility(View.GONE);
                    cityListView.setVisibility(View.GONE);
                    areaListView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getAreaFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                getAreaFailure();
            }
        });

    }

    private void getAreaFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("确认您的选择")
                    .setMessage("读取地区数据失败,是否重试?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getArea();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

    }

    private void returnActivity() {

        if (areaListView.getVisibility() == View.VISIBLE) {
            provinceListView.setVisibility(View.GONE);
            cityListView.setVisibility(View.VISIBLE);
            areaListView.setVisibility(View.GONE);
            area = "";
        } else {
            if (cityListView.getVisibility() == View.VISIBLE) {
                provinceListView.setVisibility(View.VISIBLE);
                cityListView.setVisibility(View.GONE);
                areaListView.setVisibility(View.GONE);
                city_id = "";
                city = "";
            } else {
                mApplication.finishActivity(mActivity);
            }
        }

    }

}