package com.example.core;

/**
 * Created by yunchang on 2018/5/16.
 */

public class PluginEntry {

    public final String service;

    public final String pluginClass;

    public final HybridPlugin plugin;

    public final boolean onLoad;

    public PluginEntry(String service, HybridPlugin plugin) {
        this(service, plugin.getClass().getName(), plugin, true);
    }

    public PluginEntry(String service, String pluginClass, boolean onLoad) {
        this(service, pluginClass, null, onLoad);
    }

    public PluginEntry(String service, String pluginClass, HybridPlugin plugin, boolean onLoad) {
        this.service = service;
        this.pluginClass = pluginClass;
        this.plugin = plugin;
        this.onLoad = onLoad;
    }
}
