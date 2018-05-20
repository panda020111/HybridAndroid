package com.example.core;

/**
 * Created by yunchang on 2018/5/18.
 * 负责Activity和CordovaWebViewImpl的通信；
 * 实现plugin 和 Activity之间的沟通；
 */

public interface ContainerInterface {

    void performAction(String action, ActionCallback callback, String... args);

    interface ActionCallback {
        void onSuccess();
        void onFailed();
    }
}
