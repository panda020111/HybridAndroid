package com.example.core;

import android.view.View;

import java.util.List;
import java.util.Map;

/**
 * Created by yunchang on 2018/5/14.
 * 这是WebView包裹的一层
 */

public interface HybridWebViewInterface {

    View getView();

    void loadUrlIntoView(String url);

    void showWebPage(String url, boolean openExternal, boolean cleanHistory, Map<String, Object> params);

    void reload();

    void loadUrl(String url);

    void stopLoading();

    Object postMessage(String id, Object data);

    void sendPluginResult(PluginResult pluginResult, String callbackId);
}
