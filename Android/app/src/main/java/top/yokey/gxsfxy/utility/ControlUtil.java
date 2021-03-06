package top.yokey.gxsfxy.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.adapter.BasePagerAdapter;

public class ControlUtil {

    //作用：设置 TabLayout
    public static void setTabLayout(Activity activity, TabLayout mTabLayout, BasePagerAdapter mAdapter, ViewPager mViewPager) {

        try {
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setTabsFromPagerAdapter(mAdapter);
            mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.main));
            mTabLayout.setTabTextColors(ContextCompat.getColor(activity, R.color.greyAdd), ContextCompat.getColor(activity, R.color.main));
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    //作用：设置 SwipeRefreshLayout
    public static void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {

        swipeRefreshLayout.setColorSchemeResources(R.color.main, R.color.red);

    }

    //作用：设置 WebView
    @SuppressLint("SetJavaScriptEnabled")
    public static void setWebView(WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

    }

    //作用：设置 控件焦点
    public static void setFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.findFocus();
    }

}