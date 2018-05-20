package com.example.core;

/**
 * Created by yunchang on 2018/5/17.
 */

public class HybridBridge {

    private PluginManager mPluginManager;
    private NativeToJsMessageQueue messageQueue;

    public HybridBridge(PluginManager pluginManager, NativeToJsMessageQueue messageQueue) {
        mPluginManager = pluginManager;
        this.messageQueue = messageQueue;
    }

    public String jsExec(int bridgeSecret, String service, String action, String callbackId, String arguments) {
        // 执行插件
        mPluginManager.exec(service, action, callbackId, arguments);
        String ret = messageQueue.popAndEncode();
        return ret;
    }
}

