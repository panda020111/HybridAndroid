package com.example.core.plugins;

import android.util.Log;

import com.example.core.HybridPlugin;

/**
 * Created by yunchang on 2018/5/16.
 */

public class ClientPlugin extends HybridPlugin {

    private static final String TAG = "ClientPlugin";

    @Override
    public Object onMessage(String id, Object data) {
        if (id.equals("onPageStarted")) {
            onPageStarted((String) data);
        } else if (id.equals("onPageFinished")) {
            onPageFinished((String) data);
        } else if (id.equals("onReceiveTitle")) {
            onReceiveTitle((String) data);
        } else if (id.equals("onReceiveError")) {
            onReceiveError();
        }

        return null;
    }

    // 
    private void onPageStarted (final String url) {
        webview.mContainerInterface.performAction("onPageStarted", null);
        Log.d(TAG, "onPageStarted: " + url);
    }

    private void onPageFinished (final String url) {
        webview.mContainerInterface.performAction("onPageFinished", null);
        Log.d(TAG, "onPageFinished: " + url);
    }

    private void onReceiveError() {
        webview.mContainerInterface.performAction("onReceiveError", null);
        Log.d(TAG, "onReceiveError: ");
    }

    private void onReceiveTitle (String title) {
        webview.mContainerInterface.performAction("onReceiveTitle", null, title);
        Log.d(TAG, "onReceiveTitle: " + title);
    }
}
