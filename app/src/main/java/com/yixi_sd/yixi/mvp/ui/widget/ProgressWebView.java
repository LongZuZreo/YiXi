package com.yixi_sd.yixi.mvp.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.yixi_sd.yixi.R;


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

    public void setWebChromeClientCallback(final WebChromeClientCallback callback) {
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                updateProgress(newProgress);
                if (newProgress == 100) {
                    //加载完成
                    callback.loadSuccess(true);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                callback.showTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //自己处理
                Log.i("ddef", "onJsAlert: " + url + "--" + message + "--" + result.toString());
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton("确定", null);

                // 不需要绑定按键事件
                // 屏蔽keycode等于84之类的按键
                // 禁止响应按back键的事件
                builder.setCancelable(false);
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                        Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + keyEvent);
                        return true;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
            }

            /**
             * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message,
                                       final JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });

                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                // builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("提示").setMessage(message);

                final EditText et = new EditText(view.getContext());
                et.setSingleLine();
                et.setText(defaultValue);
                builder.setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }

                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });

                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
                        return true;
                    }
                });

                // 禁止响应按back键的事件
                // builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                Log.i("ddef", "onJsBeforeUnload: " + url + "::" + message + "::");
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });
    }

    public interface WebChromeClientCallback {
        void showTitle(String title);

        void loadSuccess(boolean result);
    }
}