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
        queue.add(message);
    }

    // 将结果返回到；
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
    }
}
