package com.example.hybridwebview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.core.ContainerInterface;
import com.example.core.HybridController;
import com.example.core.HybridWebViewImpl;
import com.example.core.engine.HybridWebViewEngine;

/**
 * Created by yunchang on 2018/5/17.
 */

public class WebViewActivity extends Activity implements ContainerInterface {

    private static final String TAG = "WebViewActivity";
    
    private RelativeLayout mBaseLayout;
    private FrameLayout mBodyLayout;
    private RelativeLayout mTitleLayout;
    private TextView mTitleTv;
    private ImageView mLeftBtn;
    private HybridController mHybridController;

    private HybridWebViewImpl mWebViewImpl;
    private ProgressBar mProgressBar;
    

    private View mErrorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_act);

        // 初始化title
        mBaseLayout = findViewById(R.id.base_ly_act_layout);
        mTitleLayout = findViewById(R.id.layout_title);
        LayoutInflater.from(this).inflate(R.layout.webview_title, mTitleLayout, true);

        mTitleTv = findViewById(R.id.middle_text);
        mLeftBtn = findViewById(R.id.left_btn);
        mBodyLayout = findViewById(R.id.body);
        mProgressBar = findViewById(R.id.progress);

        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 退出activity；
                WebViewActivity.this.finish();
            }
        });

        //todo add searchBar
        String url = "http://act.mogujie.com/519/h5";
        doCreate(url);
    }

    /**
     * init HybridController
     */
    private void doCreate(String url) {

        mWebViewImpl = new HybridWebViewImpl(new HybridWebViewEngine(getApplicationContext()), this);
        mBodyLayout.addView(mWebViewImpl.getView());

        // show web page;
        mWebViewImpl.showWebPage(url, false, false, null);
    }

    /**
     *
     * @param action
     * @param callback
     * @param args
     */
    @Override
    public void performAction(String action, ActionCallback callback, String... args) {

        if ("onReceiveTitle".equals(action)) {
            setTitle(args[0]);
        } else if ("onShowProgress".equals(action)) {
            showProgressing();
        } else if ("onHideProgress".equals(action)) {
            hideProgressing();
        } else if ("onReceivedError".equals(action)) {
            showErrorPage();
        } else if ("onHideErrorPage".equals(action)) {
            hideErrorPage();
        }

    }

    private void setTitle(String title) {
        if (mTitleTv == null) {
            Log.e(TAG, "setTitle: title view is null;");
        }

        mTitleTv.setText(title);
    }

    private void showProgressing() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressing() {
        if (mTitleTv != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showErrorPage() {

    }

    private void hideErrorPage() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
