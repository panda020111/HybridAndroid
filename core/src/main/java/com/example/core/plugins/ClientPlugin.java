package com.example.core.plugins;

import android.util.Log;

import com.example.core.ContainerAction;
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
        } else if (id.equals("onReceivedTitle")) {
            onReceiveTitle((String) data);
        } else if (id.equals("onReceivedError")) {
            onReceiveError();
        }

        return null;
    }

    private void onPageStarted (final String url) {
        Log.d(TAG, "onPageStarted: " + url);
    }

    private void onPageFinished (final String url) {
        webview.mContainerInterface.performAction(ContainerAction.ACTION_HIDE_PROGRESS, null);
        Log.d(TAG, "onPageFinished: " + url);
    }

    private void onReceiveError() {
        webview.mContainerInterface.performAction(ContainerAction.ACTION_SHOW_ERROR_PAGE, null);
        Log.d(TAG, "onReceiveError: ");
    }

    private void onReceiveTitle (String title) {
        webview.mContainerInterface.performAction(ContainerAction.ACTION_SET_TITLE, null, title);
        Log.d(TAG, "onReceiveTitle: " + title);
    }
}
