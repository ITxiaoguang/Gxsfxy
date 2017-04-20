package top.yokey.gxsfxy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import top.yokey.gxsfxy.R;
import top.yokey.gxsfxy.utility.ControlUtil;
import top.yokey.gxsfxy.utility.ToastUtil;

public class BrowserActivity extends Activity {

    private Activity mActivity;
    private MyApplication mApplication;

    private String linkString;

    private ImageView backImageView;
    private TextView titleTextView;

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private ImageView back1ImageView;
    private ImageView nextImageView;
    private ImageView refreshImageView;
    private ImageView topImageView;
    private ImageView shareImageView;

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
        setContentView(R.layout.activity_browser);
        initView();
        initData();
        initEven();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.loadUrl("");
        mWebView.clearHistory();
        mWebView.clearMatches();
        mWebView.clearFormData();
        mWebView.clearCache(true);
        mWebView.clearSslPreferences();
        mApplication.mCookieManager.removeAllCookie();
        mApplication.mCookieManager.removeSessionCookie();
        mApplication.mCookieManager.removeExpiredCookie();
        mWebView = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    private void initView() {

        backImageView = (ImageView) findViewById(R.id.backImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        mWebView = (WebView) findViewById(R.id.mainWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        back1ImageView = (ImageView) findViewById(R.id.back1ImageView);
        nextImageView = (ImageView) findViewById(R.id.nextImageView);
        refreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        topImageView = (ImageView) findViewById(R.id.topImageView);
        shareImageView = (ImageView) findViewById(R.id.shareImageView);

    }

    private void initData() {

        mActivity = this;
        mApplication = (MyApplication) getApplication();

        titleTextView.setText("加载中...");
        linkString = mActivity.getIntent().getStringExtra("link");

        ControlUtil.setWebView(mWebView);
        mWebView.loadUrl(linkString);

    }

    private void initEven() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        back1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goBack();
            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.goForward();
            }
        });

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl(linkString);
            }
        });

        topImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(19)
            public void onClick(View v) {
                try {
                    mWebView.setScrollY(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(mActivity, "暂不支持");
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.startsWith("http") || url.startsWith("https")) {
                    return super.shouldInterceptRequest(view, url);
                } else {
                    try {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(in);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleTextView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

    }

    private void returnActivity() {

        mApplication.finishActivity(mActivity);

    }

}