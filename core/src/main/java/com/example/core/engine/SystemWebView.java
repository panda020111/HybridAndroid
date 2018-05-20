package com.example.core.engine;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by yunchang on 2018/5/14.
 */

public class SystemWebView extends WebView {

    private SystemWebViewClient mViewClient;
    private SystemWebChromeClient mChromeClient;
    private HybridWebViewEngine mParentEngine;

    public SystemWebView(Context context) {
        super(context);
    }

    public void init(HybridWebViewEngine engine) {
        this.mParentEngine = engine;
        if (this.mViewClient == null) {
            setWebViewClient(new SystemWebViewClient(engine));
        }

        if (this.mChromeClient == null) {
            setWebChromeClient(new SystemWebChromeClient(engine));
        }
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        mViewClient = (SystemWebViewClient) client;
        super.setWebViewClient(client);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        mChromeClient = (SystemWebChromeClient) client;
        super.setWebChromeClient(client);
    }
}
