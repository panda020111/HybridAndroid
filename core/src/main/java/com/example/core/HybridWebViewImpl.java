package com.example.core;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.example.core.engine.HybridWebViewEngine;
import com.example.core.plugins.ClientPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by yunchang on 2018/5/14.
 */

public class HybridWebViewImpl implements HybridWebViewInterface {

    private static final String TAG = "HybridWebViewImpl";

    private PluginManager mPluginManager;
    private HybridWebViewEngine mEngine;
    private NativeToJsMessageQueue mNativeToJsMessageQueue;
    private LinkedHashMap<String, PluginEntry> entryMap = new LinkedHashMap<>();

    private List<PluginEntry> mPluginEntries = new ArrayList<>();

    // act with activity;
    public ContainerInterface mContainerInterface;

    public HybridWebViewImpl(HybridWebViewEngine engine, ContainerInterface containerInterface) {
        this.mEngine = engine;
        mContainerInterface = containerInterface;
        init();
    }

    /**
     * init plugin manager and nativeToJsMessageQueue;
     */
    public void init() {

        loadPluginsConfig();

        Log.d(TAG, "init: Hybrid WebView impl");
        mPluginManager = new PluginManager(this, mPluginEntries);
        mNativeToJsMessageQueue = new NativeToJsMessageQueue();
        mEngine.init(this, mPluginManager, mNativeToJsMessageQueue);
    }

    @Override
    public View getView() {
        return mEngine.getView();
    }

    @Override
    public void loadUrlIntoView(String url) {
        Log.d(TAG, "loadUrlIntoView: url ==" + url);

        // todo request timeout处理error的相关逻辑;
        mEngine.loadUrl(url);
    }

    @Override
    public void sendPluginResult(PluginResult pluginResult, String callbackId) {
        mNativeToJsMessageQueue.addPluginResult(pluginResult, callbackId);
    }

    @Override
    public void showWebPage(String url, boolean openExternal, boolean cleanHistory, Map<String, Object> params) {
        Log.d(TAG, "showWebPage: url " + url);

        // todo url拦截的相关操作
        loadUrlIntoView(url);
    }

    @Override
    public void reload() {

    }

    @Override
    public Object postMessage(String id, Object data) {
        return null;
    }

    private void loadPluginsConfig() {
        this.mPluginEntries.add(new PluginEntry("Client Plugin", "com.example.core.plugins.ClientPlugin", true));
    }
}
