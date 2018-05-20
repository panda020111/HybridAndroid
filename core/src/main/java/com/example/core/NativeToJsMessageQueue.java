package com.example.core;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by yunchang on 2018/5/14.
 */

public class NativeToJsMessageQueue {

    private static final String TAG = "NativeToJsMessageQueue";

    private final LinkedList<JsMessage> queue = new LinkedList<>();

    private boolean paused;

    public void reset() {
        synchronized (this) {
            queue.clear();
        }
    }

    public void addPluginResult(PluginResult result, String callbackId) {
        if (callbackId == null) {
            Log.e(TAG, "addPluginResult:  Got plugin result with no callbackId");
            return ;
        }

        JsMessage message = new JsMessage(callbackId, result);

    }

    // 将结果返回到；
    public String popAndEncode() {
        synchronized (this) {
            String ret = null;
            return ret;
        }
    }

    private void enqueueMessage(JsMessage message) {
        synchronized (this) {
            queue.add(message);

        }
    }


    private static class JsMessage {
        final String jsPayloadOrCallBackId;
        final PluginResult mPluginResult;

        public JsMessage(String jsPayloadOrCallBackId, PluginResult pluginResult) {

            if (jsPayloadOrCallBackId == null || pluginResult == null) {
                throw new NullPointerException();
            }

            this.jsPayloadOrCallBackId = jsPayloadOrCallBackId;
            mPluginResult = pluginResult;
        }



    }
}
