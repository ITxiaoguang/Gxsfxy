package top.yokey.gxsfxy.activity.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.adapter.EduCJCXListAdapter;
import top.yokey.gxsfxy.system.UserAjaxParams;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.DisplayUtil;
import top.yokey.gxsfxy.utility.TextUtil;

public class EduCJCXActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String kksj;
    private String kcxz;
    private String kcmc;
    private String xsfs;

    private Vector<String> xhVector;
    private Vector<String> kkxqVector;
    private Vector<String> kcbhVector;
    private Vector<String> kcmcVector;
    private Vector<String> cjVector;
    private Vector<String> xfVector;
    private Vector<String> zxsVector;
    private Vector<String> khfsVector;
    private Vector<String> kcsxVector;
    private Vector<String> kcxzVector;

    private ImageView backImageView;
    private TextView titleTextView;
    private ImageView moreImageView;

    private RelativeLayout mRelativeLayout;
    private RecyclerView mListView;

    private Spinner timeSpinner;
    private EditText nameEditText;
    private TextView queryTextView;
    private Vector<String> timeVector;

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
        setContentView(R.layout.activity_edu_cjcx);
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
        moreImageView = (ImageView) findViewById(R.id.moreImageView);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        mListView = (RecyclerView) findViewById(R.id.mainListView);

        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        queryTextView = (TextView) findViewById(R.id.queryTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        kksj = "";
        kcxz = "";
        kcmc = "";
        xsfs = "all";

        timeVector = new Vector<>();
        titleTextView.setText("成绩查询");
        moreImageView.setImageResource(R.mipmap.ic_action_nav);

        timeVector.add("全部");
        timeVector.add("2016-2017-2");
        timeVector.add("2016-2017-1");
        timeVector.add("2015-2016-2");
        timeVector.add("2015-2016-1");
        timeVector.add("2014-2015-2");
        timeVector.add("2014-2015-1");
        timeVector.add("2013-2014-2");
        timeVector.add("2013-2014-1");
        timeVector.add("2012-2013-2");
        timeVector.add("2012-2013-1");
        timeVector.add("2011-2012-2");
        timeVector.add("2011-2012-1");
        timeVector.add("2010-2011-2");
        timeVector.add("2010-2011-1");
        timeVector.add("2009-2010-2");
        timeVector.add("2009-2010-1");
        timeVector.add("2008-2009-2");
        timeVector.add("2008-2009-1");
        timeVector.add("2007-2008-2");
        timeVector.add("2007-2008-1");
        timeVector.add("2006-2007-2");
        timeVector.add("2006-2007-1");
        timeVector.add("2005-2006-2");
        timeVector.add("2005-2006-1");
        timeVector.add("2004-2005-2");
        timeVector.add("2004-2005-1");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeVector);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        queryGrade();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRelativeLayout.getVisibility() == View.VISIBLE) {
                    goneQuery();
                } else {
                    showQuery();
                }
            }
        });

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kksj = timeVector.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        queryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryGrade();
            }
        });

    }

    private void queryGrade() {

        DialogUtil.progress(mActivity);

        kcmc = nameEditText.getText().toString();

        if (kksj.equals("全部")) {
            kksj = "";
        }

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("kksj", kksj);
        ajaxParams.put("kcxz", kcxz);
        ajaxParams.put("kcmc", kcmc);
        ajaxParams.put("xsfs", xsfs);

        mApplication.eduFinalHttp.post(mApplication.eduGradeUrlString, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (o.toString().contains("学生个人考试成绩")) {
                    //截取数据
                    String content = o.toString();
                    content = content.substring(content.indexOf("<table id=\"dataList\""), content.length());
                    content = content.substring(0, content.indexOf("</table>") + 8);
                    content = TextUtil.encodeHtml(content);
                    //数据解析
                    xhVector = new Vector<>();
                    kkxqVector = new Vector<>();
                    kcbhVector = new Vector<>();
                    kcmcVector = new Vector<>();
                    cjVector = new Vector<>();
                    xfVector = new Vector<>();
                    zxsVector = new Vector<>();
                    khfsVector = new Vector<>();
                    kcsxVector = new Vector<>();
                    kcxzVector = new Vector<>();
                    Document document = Jsoup.parse(content);
                    Elements elements = document.getElementsByTag("td");
                    for (int i = 0; i < elements.size(); i++) {
                        switch (i % 10) {
                            case 0:
                                xhVector.add(elements.get(i).text());
                                break;
                            case 1:
                                kkxqVector.add(elements.get(i).text());
                                break;
                            case 2:
                                kcbhVector.add(elements.get(i).text());
                                break;
                            case 3:
                                kcmcVector.add(elements.get(i).text());
                                break;
                            case 4:
                                cjVector.add(elements.get(i).text());
                                break;
                            case 5:
                                xfVector.add(elements.get(i).text());
                                break;
                            case 6:
                                zxsVector.add(elements.get(i).text());
                                break;
                            case 7:
                                khfsVector.add(elements.get(i).text());
                                break;
                            case 8:
                                kcsxVector.add(elements.get(i).text());
                                break;
                            case 9:
                                kcxzVector.add(elements.get(i).text());
                                break;
                            default:
                                break;
                        }
                    }
                    //填充到列表
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                    for (int i = 0; i < xhVector.size(); i++) {
                        UserAjaxParams ajaxParams = new UserAjaxParams(mApplication, "user", "updateGrade");
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("xh", xhVector.get(i));
                        hashMap.put("kkxq", kkxqVector.get(i));
                        hashMap.put("kcbh", kcbhVector.get(i));
                        hashMap.put("kcmc", kcmcVector.get(i));
                        hashMap.put("cj", cjVector.get(i));
                        hashMap.put("xf", xfVector.get(i));
                        hashMap.put("zxs", zxsVector.get(i));
                        hashMap.put("khfs", khfsVector.get(i));
                        hashMap.put("kcsx", kcsxVector.get(i));
                        hashMap.put("kcxz", kcxzVector.get(i));
                        ajaxParams.put("xh", xhVector.get(i));
                        ajaxParams.put("kkxq", kkxqVector.get(i));
                        ajaxParams.put("kcbh", kcbhVector.get(i));
                        ajaxParams.put("kcmc", kcmcVector.get(i));
                        ajaxParams.put("cj", cjVector.get(i));
                        ajaxParams.put("xf", xfVector.get(i));
                        ajaxParams.put("zxs", zxsVector.get(i));
                        ajaxParams.put("khfs", khfsVector.get(i));
                        ajaxParams.put("kcsx", kcsxVector.get(i));
                        ajaxParams.put("kcxz", kcxzVector.get(i));
                        mApplication.mFinalHttp.post(mApplication.apiUrlString + "c=user&a=updateGrade", ajaxParams, null);
                        arrayList.add(hashMap);
                    }
                    mListView.setLayoutManager(new LinearLayoutManager(mActivity));
                    mListView.setAdapter(new EduCJCXListAdapter(mActivity, arrayList));
                    //显示UI
                    if (mRelativeLayout.getVisibility() == View.VISIBLE) {
                        goneQuery();
                    }
                } else {
                    queryFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                queryFailure();
            }
        });

    }

    private void queryFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity).setTitle("是否重试?")
                    .setMessage("读取数据失败")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            queryGrade();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

    }

    private void goneQuery() {

        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, DisplayUtil.getHeight(mActivity));
        animation.setDuration(1000);
        mRelativeLayout.startAnimation(animation);
        mRelativeLayout.setVisibility(View.GONE);
        AndroidUtil.hideKeyboard(queryTextView);

    }

    private void showQuery() {

        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, DisplayUtil.getHeight(mActivity), 0.0f);
        animation.setDuration(1000);
        mRelativeLayout.startAnimation(animation);
        mRelativeLayout.setVisibility(View.VISIBLE);

    }

    private void returnActivity() {

        if (mRelativeLayout.getVisibility() == View.VISIBLE) {
            goneQuery();
        } else {
            mApplication.finishActivity(mActivity);
        }

    }

}