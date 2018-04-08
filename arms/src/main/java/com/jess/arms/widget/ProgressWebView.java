package com.jess.arms.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jess.arms.R;


/**
 *
 */
public class ProgressWebView extends WebView {
    public static String APP_CACAHE_DIRNAME = "/processapp_cache";

    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progressbar);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        // setWebViewClient(new WebViewClient(){});
        initAttrs();
    }

    private void initAttrs() {
        //是否可以缩放
        getSettings().setSupportZoom(false);
        getSettings().setJavaScriptEnabled(true);
        //设置允许获取地理位置
        getSettings().setGeolocationEnabled(true);
        getSettings().setUserAgentString("android_process" + getSettings().getUserAgentString());
//      //禁止硬件加速、AYER_TYPE_SOFTWARE, null);    //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSavePassword(true);
        getSettings().setSaveFormData(true);
        getSettings().setGeolocationEnabled(true);
        requestFocus();
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getContext().getCacheDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        getSettings().setAppCachePath(cacheDirPath);
        getSettings().setDatabasePath(cacheDirPath);

        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);


    }

    public void updateProgress(int newProgress) {
        if (newProgress == 100) {
            progressbar.setVisibility(GONE);
        } else {
            if (progressbar.getVisibility() == GONE)
                progressbar.setVisibility(VISIBLE);
            progressbar.setProgress(newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface WebChromeClientCallback {
        void showTitle(String title);

        void loadSuccess(boolean result);
    }
}