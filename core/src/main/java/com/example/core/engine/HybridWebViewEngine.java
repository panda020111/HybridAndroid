package com.example.core.engine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.core.ExposeJsApi;
import com.example.core.HybridBridge;
import com.example.core.HybridWebViewInterface;
import com.example.core.NativeToJsMessageQueue;
import com.example.core.PluginManager;

import java.lang.annotation.Native;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yunchang on 2018/5/14.
 */

public class HybridWebViewEngine {

    private static final String TAG = "HybridWebViewEngine";

    private final SystemWebView mWebView;
    public HybridWebViewInterface parentWebView;
    private NativeToJsMessageQueue mNativeToJsMessageQueue;
    private HybridBridge bridge;
    public PluginManager mPluginManager;

    public HybridWebViewEngine(Context context) {
        this(new SystemWebView(context));
    }

    public HybridWebViewEngine(SystemWebView webView) {
        this.mWebView = webView;
    }

    public void init(HybridWebViewInterface parentWebView, PluginManager pluginManager, NativeToJsMessageQueue messageQueue, String userAgent) {
        this.parentWebView = parentWebView;
        this.mPluginManager = pluginManager;
        this.mNativeToJsMessageQueue = messageQueue;

        // 初始化webview
        mWebView.init(this);
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.setInitialScale(0);
        mWebView.setVerticalScrollBarEnabled(false);
        initWebViewSettings();
        setUserAgent(userAgent);

        // 初始化Bridge
        bridge = new HybridBridge(pluginManager, mNativeToJsMessageQueue);
        exposeJsInterface(mWebView, bridge);
    }

    private void initWebViewSettings() {

        // Enable JavaScript
        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        try {
            // 字体设置为默认的
            settings.setTextZoom(100);
            settings.setUseWideViewPort(true);
        }catch (Throwable e){
            e.printStackTrace();
        }

        try {
            Method gingerbread_getMethod =  WebSettings.class.getMethod("setNavDump", new Class[] { boolean.class });

            String manufacturer = android.os.Build.MANUFACTURER;
            Log.d(TAG, "CordovaWebView is running on device made by: " + manufacturer);
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB &&
                    android.os.Build.MANUFACTURER.contains("HTC"))
            {
                gingerbread_getMethod.invoke(settings, true);
            }
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "We are on a modern version of Android, we will deprecate HTC 2.3 devices in 2.8");
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Doing the NavDump failed with bad arguments");
        } catch (IllegalAccessException e) {
            Log.d(TAG, "This should never happen: IllegalAccessException means this isn't Android anymore");
        } catch (InvocationTargetException e) {
            Log.d(TAG, "This should never happen: InvocationTargetException means this isn't Android anymore.");
        }

        //We don't save any form data in the application
        settings.setSaveFormData(false);
        settings.setSavePassword(false);

        // while we do this
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        // Enable database
        settings.setDatabaseEnabled(true);

        // Enable DOM storage
        settings.setDomStorageEnabled(true);

        // Enable built-in geolocation
        settings.setGeolocationEnabled(true);

        // Enable AppCache
        settings.setAppCacheMaxSize(5 * 1048576);
        settings.setAppCacheEnabled(true);
    }

    public View getView() {
        return mWebView;
    }


    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void syncCookies() {

    }

    public void setUserAgent (String userAgent) {
        WebSettings webSettings = mWebView.getSettings();
        String ua = webSettings.getUserAgentString();

        String new_Ua = ua + " " + userAgent;
        webSettings.setUserAgentString(new_Ua);

        Log.d(TAG, "setUserAgent: user agent ===> " + webSettings.getUserAgentString());
    }

    // 将bridge定义的接口暴露给webview；
    private static void exposeJsInterface(WebView webView, HybridBridge bridge) {
        ExposeJsApi exposeJsApi = new ExposeJsApi(bridge);
        webView.addJavascriptInterface(exposeJsApi, "_HybridNative");
    }

}

