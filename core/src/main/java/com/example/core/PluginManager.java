package com.example.core;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by yunchang on 2018/5/14.
 */

public class PluginManager {

    private static final String TAG = "PluginManager";

    // 存放已经初始化完成的Plugin
    private final LinkedHashMap<String, HybridPlugin> pluginMap = new LinkedHashMap<>();

    // 未初始化的plugin
    private final LinkedHashMap<String, PluginEntry> entryMap = new LinkedHashMap<>();
    private final HybridWebViewImpl webView;
    private boolean isInitialized;


    public PluginManager(HybridWebViewImpl webView, List<PluginEntry> pluginEntries) {
        this.webView = webView;
        setPluginEntries(pluginEntries);
        init();
    }

    public void setPluginEntries(List<PluginEntry> entries) {
        for (PluginEntry entry : entries) {
            addService(entry);
        }
    }
    
    public void init() {
        Log.d(TAG, "init: plugin manager init");
        pluginMap.clear();
        this.startPlugins();
    }

    private void startPlugins () {
        for (PluginEntry entry : entryMap.values()) {
            if (entry.onLoad) {
                getPlugin(entry.service);
            } else {
                pluginMap.put(entry.service, null);
            }
        }
    }

    //
    public HybridPlugin getPlugin(String service) {
        HybridPlugin plugin = pluginMap.get(service);
        if (plugin == null) {
            PluginEntry entry = entryMap.get(service);

            if (entry == null) {
                return null;
            }

            if (entry.plugin != null) {
                plugin = entry.plugin;
            } else {
                plugin = instantiatePlugin(entry.pluginClass);
            }

            // 初始化plugin；
            plugin.initialize(webView, service);
            pluginMap.put(service, plugin);
        }

        return plugin;
    }

    // add plugin entry, but not create plugin instance;
    public void addService(String service, String className) {
        PluginEntry entry = new PluginEntry(service, className, false);
        this.addService(entry);
    }

    public void addService(String service, String className, boolean onload) {
        PluginEntry entry = new PluginEntry(service, className, true);
        this.addService(entry);
    }

    // add plugin to pluginEntry;
    public void addPlugin(String service, HybridPlugin plugin) {

        this.pluginMap.put(service, plugin);
    }


    public void addService(PluginEntry entry) {
        this.entryMap.put(entry.service, entry);
    }


    private HybridPlugin instantiatePlugin(String className) {
        HybridPlugin ret = null;

        try {
            Class<?> c = null;
            if ((className != null) && !("".equals(className))) {
                c = Class.forName(className);
            }

            if (c != null & HybridPlugin.class.isAssignableFrom(c)) {
                ret = (HybridPlugin) c.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "instantiatePlugin: Error adding Plugin" + className);
        }

        return ret;
    }

    // 执行插件
    public void exec(final String service, final String action, final String callbackId, final String rawArgs) {
        HybridPlugin plugin = getPlugin(service);

        if (plugin == null) {
            Log.d(TAG, "exec() call to unknow plugin:" + service);
            return ;
        }

        // 每次exec时就会有一个CallbackContext;
        CallbackContext callbackContext = new CallbackContext(webView, callbackId);

        // todo 记录下超时；
        try {
            boolean wasValidAction = plugin.execute(action, rawArgs, callbackContext);
            if (!wasValidAction) {
                PluginResult pluginResult = new PluginResult(PluginResult.Status.INVALID_ACTION);
                callbackContext.sendPluginResult(pluginResult);
            }
        } catch (Exception e) {

        }

    }


    // 响应的事件
    public void onPause() {
        for (HybridPlugin plugin: this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onPause();
            }
        }
    }

    public void onResume() {
        for (HybridPlugin plugin: this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onResume();
            }
        }
    }

    public void onStart() {
        for (HybridPlugin plugin: this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onStart();
            }
        }
    }

    public void onStop() {
        for (HybridPlugin plugin: this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onStop();
            }
        }
    }

    // 这个地方好像存在bug; 如果有一个plugin的post message有返回
    // 就不会给其他的Plugin postMessage了；
    public Object postMessage(String id, Object data) {
        for (HybridPlugin plugin: this.pluginMap.values()) {
            if (plugin != null) {
                Object obj = plugin.onMessage(id, data);
                if (obj != null) {
                    return obj;
                }
            }
        }
        return null;
    }
}
