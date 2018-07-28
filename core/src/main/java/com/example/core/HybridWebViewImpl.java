package com.example.core;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import com.example.core.engine.HybridWebViewEngine;
import com.example.core.utils.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yunchang on 2018/5/14.
 */

public class HybridWebViewImpl implements HybridWebViewInterface {

    private static final String TAG = "HybridWebViewImpl";
    private PluginManager mPluginManager;
    protected final HybridWebViewEngine mEngine;
    private String userAgent;
    private NativeToJsMessageQueue mNativeToJsMessageQueue;

    private List<PluginEntry> mPluginEntries = new ArrayList<>();

    // act with activity;
    public ContainerInterface mContainerInterface;
    public Activity mActivity;

    private boolean isLoading = true;
    private ExecutorService threadPool;

    public HybridWebViewImpl(HybridWebViewEngine engine, ContainerInterface containerInterface, String userAgent, Activity activity) {
        this.mEngine = engine;
        mContainerInterface = containerInterface;
        this.mActivity = activity;
        this.userAgent = userAgent;
        this.threadPool = Executors.newCachedThreadPool();
        init();
    }

    /**
     * init plugin manager and nativeToJsMessageQueue;
     */
    private void init() {
        loadPluginsConfig();
        Log.d(TAG, "init: Hybrid WebView impl");
        mPluginManager = new PluginManager(this, mPluginEntries);
        mNativeToJsMessageQueue = new NativeToJsMessageQueue(mEngine, this);
        mEngine.init(this, mPluginManager, mNativeToJsMessageQueue, this.userAgent);

        // 注入main.js
        injectMainJS();
    }


    public ExecutorService getThreadPool() {
        return threadPool;
    }

    @Override
    public View getView() {
        return mEngine.getView();
    }

    @Override
    public void loadUrlIntoView(final String url) {
        Log.d(TAG, "loadUrlIntoView: url " + url);

        if (url.equals("about:black") || url.startsWith("javascript:")) {
            mEngine.loadUrl(url);

            return ;
        }

        // 设置timeout时间;
        final int loadUrlTimeoutValue = 20000;

        // todo request timeout处理error的相关逻辑;
        final Runnable loadError = new Runnable() {
            @Override
            public void run() {
                stopLoading();
                Log.d(TAG, "run: HybridWebview: Timeout error");
                JSONObject data = new JSONObject();
                try {
                    data.put("errorCode", -6);
                    data.put("description", "The connection was unsuccessful");
                    data.put("url", url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mPluginManager.postMessage("onReceivedError", data);
            }
        };

        final Runnable timeoutCheck = new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // 休息8 seconds;
                        wait(loadUrlTimeoutValue);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isLoading) {
                    mActivity.runOnUiThread(loadError);
                }
            }
        };

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadUrlTimeoutValue > 0) {
                    threadPool.execute(timeoutCheck);
                }

                mEngine.loadUrl(url);
            }
        });
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
    public void loadUrl(String url) {
        loadUrlIntoView(url);
    }

    @Override
    public void stopLoading () {
        isLoading = false;
    }

    @Override
    public Object postMessage(String id, Object data) {
        return null;
    }

    private void loadPluginsConfig() {
        this.mPluginEntries.add(new PluginEntry("ClientPlugin", "com.example.core.plugins.ClientPlugin", true));
        this.mPluginEntries.add(new PluginEntry("hybrid.device", "com.example.core.plugins.DevicePlugin", false));
        this.mPluginEntries.add(new PluginEntry("hybrid.notification", "com.example.core.plugins.NotificationPlugin", false));
    }

    private void injectMainJS() {
        String jsContent = FileUtils.getScript(mActivity, "main.js");
        String buildString = String.format("javascript:%s", jsContent);
        mEngine.loadUrl(buildString);
    }
}
