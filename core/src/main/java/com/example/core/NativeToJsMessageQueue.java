package com.example.core;

import android.util.Log;

import com.example.core.engine.HybridWebViewEngine;

import java.util.LinkedList;

/**
 * Created by yunchang on 2018/5/14.
 */

public class NativeToJsMessageQueue {

    private static final String TAG = "NativeToJsMessageQueue";

    private final LinkedList<JsMessage> queue = new LinkedList<>();

    public final HybridWebViewEngine mEngine;
    private HybridWebViewImpl mWebView;

    public NativeToJsMessageQueue(HybridWebViewEngine engine, HybridWebViewImpl hybridWebView) {
        mEngine = engine;
        mWebView = hybridWebView;
    }

    // 设置访问的状态
    private boolean paused;

    public void reset() {
        synchronized (this) {
            queue.clear();
        }
    }

    public void setPaused (boolean status) {
        paused = status;
    }

    public void addPluginResult(PluginResult result, String callbackId) {
        if (callbackId == null) {
            Log.e(TAG, "addPluginResult:  Got plugin result with no callbackId");
            return ;
        }

        JsMessage message = new JsMessage(callbackId, result);
        enqueueMessage(message);
    }

    // 同步调用会在队列中直接获取message
    public String popAndEncode() {
        synchronized (this) {
            if (queue.size() > 0) {
                StringBuilder sb = new StringBuilder();
                JsMessage jsMessage = queue.removeFirst();
                jsMessage.encodeMessage(sb);

                return sb.toString();
            }
            return "";
        }
    }


    public String popAndEncodeAsJs() {
        synchronized (this) {

            StringBuilder sb = new StringBuilder();
            JsMessage message = queue.removeFirst();
            message.encodeAsJsMessage(sb);

            String ret = sb.toString();
            return ret;
        }
    }

    private void enqueueMessage(JsMessage message) {
        synchronized (this) {
            queue.add(message);
            // async
            if (!this.paused) {
                this.onNativeToJsMessageAvailable();
            }
        }
    }

    private void onNativeToJsMessageAvailable() {
        if (queue.size() > 0) {
            this.mWebView.mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String js = popAndEncodeAsJs();
                    if (js != null) {
                        Log.d(TAG, "onNativeToJsMessageAvailable: " + js);
                        mEngine.evaluateJavascript(js);
                    }
                }
            });

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


        public static void encodeMessageHelper(StringBuilder sb, PluginResult result) {
            switch (result.getMessageType()) {
                case PluginResult.MESSAGE_TYPE_BOOLEAN:
                    sb.append(result.getMessage().charAt(0));  // t or f;
                    break;
                case PluginResult.MESSAGE_TYPE_NULL:
                    sb.append("N");
                    break;
                case PluginResult.MESSAGE_TYPE_STRING:
                    sb.append("s");
                    sb.append(result.getMessage());
                    break;
                default:
                    sb.append(result.getMessage());
            }
        }

        public void encodeMessage(StringBuilder sb) {
            int status = mPluginResult.getStatus();
            boolean resultOk = status == PluginResult.Status.OK.ordinal();
            sb.append(resultOk ? "S" : "F")
                    .append(status)
                    .append(" ")
                    .append(jsPayloadOrCallBackId)
                    .append(" ");

            encodeMessageHelper(sb, mPluginResult);

        }

        public void encodeAsJsMessage(StringBuilder sb) {
            if (mPluginResult == null) {
                sb.append(jsPayloadOrCallBackId);
            } else {
                int status = mPluginResult.getStatus();
                boolean success = (status == PluginResult.Status.OK.ordinal()) || (status == PluginResult.Status.NO_RESULT.ordinal());
                sb.append("cordova.callbackFromNative('")
                        .append(jsPayloadOrCallBackId)
                        .append("',")
                        .append(success)
                        .append(",")
                        .append(status)
                        .append(",[");

                sb.append(mPluginResult.getMessage());
                sb.append("]").append(");");
            }
        }
    }
}
