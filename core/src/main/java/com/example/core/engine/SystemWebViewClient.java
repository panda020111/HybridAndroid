package com.example.core.engine;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yunchang on 2018/5/14.
 */

public class SystemWebViewClient extends WebViewClient  {

    private static final String TAG = "SystemWebViewClient";

    private final HybridWebViewEngine mParentEngine;

    public SystemWebViewClient(HybridWebViewEngine parentEngine) {
        mParentEngine = parentEngine;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // pluginManager send pageStarted message;
        mParentEngine.mPluginManager.postMessage("onPageStarted", url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mParentEngine.mPluginManager.postMessage("onPageFinished", url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        mParentEngine.parentWebView.stopLoading();

        // 封装下data
        JSONObject data = new JSONObject();
        try {
            data.put("errorCode", errorCode);
            data.put("description", description);
            data.put("url", failingUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mParentEngine.mPluginManager.postMessage("onReceivedError", data);
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        // 消除下timeout error；
        mParentEngine.parentWebView.stopLoading();
        super.onReceivedClientCertRequest(view, request);
    }


    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        mParentEngine.parentWebView.stopLoading();
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }
}
