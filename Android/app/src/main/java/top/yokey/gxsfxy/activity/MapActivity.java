package top.yokey.gxsfxy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.adapter.MapListAdapter;
import top.yokey.gxsfxy.utility.TextUtil;

public class MapActivity extends AppCompatActivity implements LocationSource, PoiSearch.OnPoiSearchListener, AMapLocationListener {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView searchImageView;
    private AutoCompleteTextView keywordEditText;

    private AMap aMap;
    private MapView mMapView;
    private RecyclerView mListView;
    private MapListAdapter mAdapter;
    private AMapLocationClient mLocationClient;
    private OnLocationChangedListener mListener;
    private ArrayList<HashMap<String, String>> mArrayList;

    private String name;
    private String address;
    private String province;
    private String city;
    private String area;

    private String keyword;
    private String oldKeyword;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationClient = new AMapLocationClient(this);
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        Vector<String> vector = new Vector<>();
        for (int j = 0; j < poiResult.getPois().size(); j++) {
            vector.add(poiResult.getPois().get(j).toString());
        }
        ArrayAdapter adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, vector);
        keywordEditText.setAdapter(adapter);
        keywordEditText.setText(keyword);
        keywordEditText.setSelection(keyword.length());
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

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
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.mainMapView);
        mMapView.onCreate(bundle);
        initView();
        initData();
        initEven();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mMapView.onPause();
        deactivate();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.backImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        keywordEditText = (AutoCompleteTextView) findViewById(R.id.keywordEditText);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);
        mListView = (RecyclerView) findViewById(R.id.mainListView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("地图选点");

        aMap = mMapView.getMap();
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

        mArrayList = new ArrayList<>();
        mAdapter = new MapListAdapter(mArrayList);
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);

        name = "";
        keyword = "";
        oldKeyword = "";
        address = "";

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
            }
        });

        keywordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword = keywordEditText.getText().toString();
                search();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("key", "ff32570f75caaa6231db0ed8151656a1");
                ajaxParams.put("address", province + city + area + keywordEditText.getText().toString());

                mApplication.mFinalHttp.post("http://restapi.amap.com/v3/geocode/geo?parameters", ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("geocodes"));
                            jsonObject = new JSONObject(jsonArray.getString(0));
                            String location = jsonObject.getString("location");
                            Double latitude = Double.parseDouble(location.substring(0, location.indexOf(",")));
                            Double longitude = Double.parseDouble(location.substring(location.indexOf(",") + 1, location.length()));
                            LatLng latLng = new LatLng(longitude, latitude);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                            aMap.moveCamera(cameraUpdate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("key", "ff32570f75caaa6231db0ed8151656a1");
                ajaxParams.put("location", cameraPosition.target.longitude + "," + cameraPosition.target.latitude);
                ajaxParams.put("extensions", "all");
                ajaxParams.put("radius", "3000");
                ajaxParams.put("batch", "true");

                mApplication.mFinalHttp.post("http://restapi.amap.com/v3/geocode/regeo?", ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        try {
                            mArrayList.clear();
                            JSONObject jsonObject = new JSONObject(o.toString());
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("regeocodes"));
                            jsonObject = new JSONObject(jsonArray.getString(0));
                            jsonArray = new JSONArray(jsonObject.getString("pois"));
                            jsonObject = new JSONObject(jsonObject.getString("addressComponent"));
                            province = jsonObject.getString("province").replace("自治区", "").replace("族", "");
                            province = province.replace("壮", "").replace("回", "").replace("维吾尔", "");
                            province = province.replace("省", "");
                            city = jsonObject.getString("city");
                            area = jsonObject.getString("district");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mArrayList.add(new HashMap<>(TextUtil.jsonObjectToHashMap(jsonArray.getString(i))));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mAdapter.setOnItemClickListener(new MapListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String n, String add) {
                name = n;
                address = add;
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                intent.putExtra("address", address);
                mActivity.setResult(RESULT_OK, intent);
                mApplication.finishActivity(mActivity);
            }
        });

    }

    private void search() {

        if (oldKeyword.equals(keyword)) {
            return;
        }

        PoiSearch.Query query = new PoiSearch.Query(keyword, "", city);
        query.setPageSize(10);
        query.setPageNum(1);
        PoiSearch poiSearch = new PoiSearch(mActivity, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
        oldKeyword = keyword;

    }

    private void returnActivity() {

        new AlertDialog.Builder(mActivity)
                .setTitle("确认你的选择")
                .setMessage("取消选择地点")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mApplication.finishActivity(mActivity);
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

}
