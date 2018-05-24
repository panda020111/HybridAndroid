package com.example.core;

import android.net.Uri;
import android.net.http.SslError;
import android.telecom.Call;
import android.webkit.SslErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by yunchang on 2018/5/16.
 * 所有插件的父类
 */

public class HybridPlugin {

    protected HybridWebViewImpl webview;
    private String serviceName;

    /**
     */
    public void initialize(HybridWebViewImpl webview, String serviceName) {
        this.serviceName = serviceName;
        this.webview = webview;
        pluginInitialize();
    }

    /**
     * for child plugin to initialize
     */
    public void pluginInitialize() {

    }

    public void sendPluginResult(PluginResult result, CallbackContext callbackContext) {
        callbackContext.sendPluginResult(result);
    }

    /**
     * @param action
     * @param rawArgs
     * @param callbackContext
     * @return
     */
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        JSONArray args = new JSONArray(rawArgs);
        return execute(action, args, callbackContext);
    }


    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)throws JSONException {
        return false;
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     *
     */
    public void onPause() {
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     */
    public void onResume() {

    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    public void onStart() {
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    public void onStop() {
    }


    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy() {
    }

    /**
     * Called when a message is sent to plugin.
     *
     * @param id            The message id
     * @param data          The message data
     * @return              Object to stop propagation or null
     */
    public Object onMessage(String id, Object data) {
        return null;
    }

    public Boolean shouldAllowRequest(String url) {
        return null;
    }


    public Boolean shouldAllowNavigation(String url) {
        return null;
    }


    public boolean onOverrideUrlLoading(String url) {
        return false;
    }


    public Uri remapUri(Uri uri) {
        return null;
    }

    /**
     * 当https证书校验错误时调用
     * @param handler
     * @param error
     */
    public void onReceivedSslError(SslErrorHandler handler, SslError error){

    }

}
