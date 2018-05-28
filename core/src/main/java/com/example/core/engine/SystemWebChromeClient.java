package com.example.core.engine;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by yunchang on 2018/5/14.
 */

public class SystemWebChromeClient extends WebChromeClient {

    private static final String TAG = "SystemWebChromeClient";
    private final HybridWebViewEngine mParentEngine;


    public SystemWebChromeClient(HybridWebViewEngine parentEngine) {
        mParentEngine = parentEngine;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mParentEngine.mPluginManager.postMessage("onReceivedTitle", title);
    }

    // todo 添加其他的代理

}
