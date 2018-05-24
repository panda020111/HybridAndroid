package com.example.core.plugins;

import com.example.core.CallbackContext;
import com.example.core.HybridPlugin;
import com.example.core.PluginResult;
import com.google.gson.Gson;

/**
 * Created by yunchang on 2018/5/23.
 */

public class DevicePlugin extends HybridPlugin {

    private static final String TAG = "DevicePlugin";

    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) {
        if (action.equals("getDeviceInfo")) {
            DeviceInfo deviceInfo = new DeviceInfo();

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, new Gson().toJson(deviceInfo, DeviceInfo.class));
            if (pluginResult == null) {
                return false;
            }

            sendPluginResult(pluginResult, callbackContext);
            return true;
        }
        return false;
    }

    public static class DeviceInfo {
        public String platform = "Android";
        public String version = "1.0.0";
        public String screenWidth = "1140";
        public String screenHeight = "1890";
    }
}
