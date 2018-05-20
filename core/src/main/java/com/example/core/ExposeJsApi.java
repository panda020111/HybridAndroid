package com.example.core;

import android.webkit.JavascriptInterface;

import org.json.JSONException;

/**
 * Created by yunchang on 2018/5/17.
 */

public class ExposeJsApi {

    private final HybridBridge bridge;

    public ExposeJsApi(HybridBridge bridge) {
        this.bridge = bridge;
    }

    @JavascriptInterface
    public String exec(int bridgeSecret, String service, String action, String callbackId, String arguments) throws JSONException, IllegalAccessException {
        return bridge.jsExec(bridgeSecret, service, action, callbackId, arguments);
    }
}
