package com.example.core;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.core.engine.HybridWebViewEngine;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yunchang on 2018/5/14.
 */

public class HybridController {

    private static final String TAG = "HybridController";

    public HybridWebViewInterface appView;
    private Activity mActivity;
    private String launhcUrl;
    private ArrayList<PluginEntry> mPluginEntries;

    public HybridController(Activity activity) {
        mActivity = activity;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate: HybridController is oncreate");
        init();
    }

    public void showWebPage(String url, boolean openExternal, boolean clearHistory, Map<String, Object> params) {
        appView.showWebPage(url, openExternal, clearHistory, params);
    }

    private void init() {
        loadPluginEntries();
    }

    /**
     * init plugins config
     */
    private void loadPluginEntries() {
        // todo 插件的注册和添加；
        mPluginEntries = new ArrayList<>();
    }
}
