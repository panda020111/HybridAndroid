package com.example.core.plugins;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.core.CallbackContext;
import com.example.core.HybridPlugin;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by yunchang on 2018/5/24.
 */

public class NotificationPlugin extends HybridPlugin {

    private static final String TAG = "NotificationPlugin";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("alert")) {
            if (args.length() > 1) {
                this.alert(args.getString(0), args.getString(1), "确认", callbackContext);
                return true;
            }
        } else if (action.equals("confirm")) {
            return true;
        }

        return false;
    }

    public void alert(final String message, final String title, final String buttonLabel, final CallbackContext callbackContext) {
        // ui相关需要context;
        AlertDialog.Builder builder = new AlertDialog.Builder(webview.mActivity);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }


        builder.setPositiveButton(buttonLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(webview.mActivity, "Yes", Toast.LENGTH_SHORT).show();
                callbackContext.success("success");
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
