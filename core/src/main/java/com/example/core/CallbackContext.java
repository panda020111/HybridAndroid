package com.example.core;

/**
 * Created by yunchang on 2018/5/17.
 * CallbackContext将pluginResult通过发送到NativeToJsMessageQueue;
 */

public class CallbackContext {

    private static final String TAG = "CallbackContext";

    private HybridWebViewInterface webview;
    private String callbackId;

    public CallbackContext(HybridWebViewInterface webview, String callbackId) {
        this.webview = webview;
        this.callbackId = callbackId;
    }

    public void sendPluginResult(PluginResult pluginResult) {
        webview.sendPluginResult(pluginResult, callbackId);
    }

    public void success(String message) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
    }

    public void error(String message) {
        sendPluginResult(new PluginResult(PluginResult.Status.ERROR, message));
    }
}
