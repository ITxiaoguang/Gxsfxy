package top.yokey.gxsfxy.activity.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.http.AjaxCallBack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.utility.AndroidUtil;
import top.yokey.gxsfxy.utility.DialogUtil;
import top.yokey.gxsfxy.utility.TextUtil;

@SuppressWarnings("all")
public class EduWDKBActivity extends AppCompatActivity {

    private Activity mActivity;
    private MyApplication mApplication;

    private ImageView backImageView;
    private TextView titleTextView;

    private TextView[][] mTextView;
    private Drawable[] mDrawable;

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
        setContentView(R.layout.activity_edu_wdkb);
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
        mTextView = new TextView[6][7];
        mTextView[0][0] = (TextView) findViewById(R.id.oneOneTextView);
        mTextView[0][1] = (TextView) findViewById(R.id.oneTwoTextView);
        mTextView[0][2] = (TextView) findViewById(R.id.oneThrTextView);
        mTextView[0][3] = (TextView) findViewById(R.id.oneFouTextView);
        mTextView[0][4] = (TextView) findViewById(R.id.oneFivTextView);
        mTextView[0][5] = (TextView) findViewById(R.id.oneSixTextView);
        mTextView[0][6] = (TextView) findViewById(R.id.oneSevTextView);
        mTextView[1][0] = (TextView) findViewById(R.id.twoOneTextView);
        mTextView[1][1] = (TextView) findViewById(R.id.twoTwoTextView);
        mTextView[1][2] = (TextView) findViewById(R.id.twoThrTextView);
        mTextView[1][3] = (TextView) findViewById(R.id.twoFouTextView);
        mTextView[1][4] = (TextView) findViewById(R.id.twoFivTextView);
        mTextView[1][5] = (TextView) findViewById(R.id.twoSixTextView);
        mTextView[1][6] = (TextView) findViewById(R.id.twoSevTextView);
        mTextView[2][0] = (TextView) findViewById(R.id.thrOneTextView);
        mTextView[2][1] = (TextView) findViewById(R.id.thrTwoTextView);
        mTextView[2][2] = (TextView) findViewById(R.id.thrThrTextView);
        mTextView[2][3] = (TextView) findViewById(R.id.thrFouTextView);
        mTextView[2][4] = (TextView) findViewById(R.id.thrFivTextView);
        mTextView[2][5] = (TextView) findViewById(R.id.thrSixTextView);
        mTextView[2][6] = (TextView) findViewById(R.id.thrSevTextView);
        mTextView[3][0] = (TextView) findViewById(R.id.fouOneTextView);
        mTextView[3][1] = (TextView) findViewById(R.id.fouTwoTextView);
        mTextView[3][2] = (TextView) findViewById(R.id.fouThrTextView);
        mTextView[3][3] = (TextView) findViewById(R.id.fouFouTextView);
        mTextView[3][4] = (TextView) findViewById(R.id.fouFivTextView);
        mTextView[3][5] = (TextView) findViewById(R.id.fouSixTextView);
        mTextView[3][6] = (TextView) findViewById(R.id.fouSevTextView);
        mTextView[4][0] = (TextView) findViewById(R.id.fivOneTextView);
        mTextView[4][1] = (TextView) findViewById(R.id.fivTwoTextView);
        mTextView[4][2] = (TextView) findViewById(R.id.fivThrTextView);
        mTextView[4][3] = (TextView) findViewById(R.id.fivFouTextView);
        mTextView[4][4] = (TextView) findViewById(R.id.fivFivTextView);
        mTextView[4][5] = (TextView) findViewById(R.id.fivSixTextView);
        mTextView[4][6] = (TextView) findViewById(R.id.fivSevTextView);
        mTextView[5][0] = (TextView) findViewById(R.id.sixOneTextView);
        mTextView[5][1] = (TextView) findViewById(R.id.sixTwoTextView);
        mTextView[5][2] = (TextView) findViewById(R.id.sixThrTextView);
        mTextView[5][3] = (TextView) findViewById(R.id.sixFouTextView);
        mTextView[5][4] = (TextView) findViewById(R.id.sixFivTextView);
        mTextView[5][5] = (TextView) findViewById(R.id.sixSixTextView);
        mTextView[5][6] = (TextView) findViewById(R.id.sixSevTextView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        mDrawable = new Drawable[28];
        mDrawable[0] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_bohelv);
        mDrawable[1] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_boy);
        mDrawable[2] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_cheng);
        mDrawable[3] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_cyan);
        mDrawable[4] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_fen);
        mDrawable[5] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_girl);
        mDrawable[6] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_huang);
        mDrawable[7] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_kafei);
        mDrawable[8] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_lan);
        mDrawable[9] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_lv);
        mDrawable[10] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_molan);
        mDrawable[11] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_tao);
        mDrawable[12] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_tuhuang);
        mDrawable[13] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_zi);
        mDrawable[14] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_bohelv);
        mDrawable[15] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_boy);
        mDrawable[16] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_cheng);
        mDrawable[17] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_cyan);
        mDrawable[18] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_fen);
        mDrawable[19] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_girl);
        mDrawable[20] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_huang);
        mDrawable[21] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_kafei);
        mDrawable[22] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_lan);
        mDrawable[23] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_lv);
        mDrawable[24] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_molan);
        mDrawable[25] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_tao);
        mDrawable[26] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_tuhuang);
        mDrawable[27] = ContextCompat.getDrawable(mActivity, R.drawable.border_color_zi);

        titleTextView.setText("我的课表");
        getJson();

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

    }

    private void getJson() {

        DialogUtil.progress(mActivity);

        mApplication.eduFinalHttp.get(mApplication.eduScheduleUrlString, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (o.toString().contains("<table id=\"kbtable\"")) {
                    try {
                        //拼接HTML
                        String content = o.toString();
                        content = content.substring(content.indexOf("<table id=\"kbtable\""), content.length());
                        content = content.substring(0, content.indexOf("</table>") + 8);
                        content = TextUtil.encodeHtml(content);
                        //初始化数据
                        Vector<String> info;
                        Vector<String>[] day;
                        info = new Vector<>();
                        day = new Vector[7];
                        for (int i = 0; i < 7; i++) {
                            day[i] = new Vector<>();
                        }
                        //解析HTML
                        Document document = Jsoup.parse(content);
                        Elements elements = document.getElementsByTag("td");
                        for (int i = 0; i < elements.size() - 1; i++) {
                            info.add(elements.get(i).html());
                        }
                        for (int i = 0; i < info.size(); i++) {
                            day[i % 7].add(info.get(i));
                        }
                        //赋值处理
                        for (int i = 0; i < day.length; i++) {
                            for (int j = 0; j < day[i].size(); j++) {
                                String temp = day[i].get(j);
                                if (temp.contains("&nbsp;")) {
                                    mTextView[j][i].setText("");
                                } else {
                                    temp = temp.substring(temp.lastIndexOf("<div"), temp.length());
                                    document = Jsoup.parse(temp);
                                    elements = document.getElementsByTag("div");
                                    temp = elements.get(0).text().replace(" ", "\n");
                                    if (temp.contains("--")) {
                                        temp = temp.substring(0, temp.indexOf("--"));
                                    }
                                    mTextView[j][i].setText(temp);
                                }
                            }
                        }
                        //上色
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 7; i++) {
                            for (int j = 0; j < 6; j++) {
                                if (!mTextView[j][i].getText().toString().equals("")) {
                                    list.add(mTextView[j][i].getText().toString());
                                }
                            }
                        }
                        list = AndroidUtil.removeDuplicateWithOrder(list);
                        for (int i = 0; i < 7; i++) {
                            for (int j = 0; j < 6; j++) {
                                for (int a = 0; a < list.size(); a++) {
                                    mTextView[j][i].setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));
                                    if (mTextView[j][i].getText().toString().equals(list.get(a))) {
                                        mTextView[j][i].setBackgroundDrawable(mDrawable[a]);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
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
                DialogUtil.cancel();
                getJsonFailure();
            }
        });

    }

    private void getJsonFailure() {

        if (!mActivity.isFinishing()) {
            new AlertDialog.Builder(mActivity).setTitle("是否重试?")
                    .setMessage("读取数据失败")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getJson();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}