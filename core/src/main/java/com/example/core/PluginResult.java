package com.example.core;

/**
 * Created by yunchang on 2018/5/16.
 */

public class PluginResult {

    private final int status;
    private final int messageType;
    private String message;

    public static final int MESSAGE_TYPE_STRING = 1;
    public static final int MESSAGE_TYPE_JSON = 2;
    public static final int MESSAGE_TYPE_NUMBER = 3;
    public static final int MESSAGE_TYPE_BOOLEAN = 4;
    public static final int MESSAGE_TYPE_NULL = 5;
    public static final int MESSAGE_TYPE_ARRAYBUFFER = 6;

    public PluginResult(Status status) {
        this(status, PluginResult.StatusMessages[status.ordinal()]);
    }

    public PluginResult(Status status, String message) {
        this.status = status.ordinal();
        this.messageType = message == null ? MESSAGE_TYPE_NULL : MESSAGE_TYPE_STRING;
        this.message = message;
    }

    public static String[] StatusMessages = new String[] {
            "No result",
            "OK",
            "Class not found",
            "Illegal access",
            "Instantiation error",
            "Malformed url",
            "IO error",
            "Invalid action",
            "JSON error",
            "Error"
    };

    public enum Status {
        NO_RESULT,
        OK,
        CLASS_NOT_FOUND_EXCEPTION,
        ILLEGAL_ACCESS_EXCEPTION,
        INSTANTIATION_EXCEPTION,
        MALFORMED_URL_EXCEPTION,
        IO_EXCEPTION,
        INVALID_ACTION,
        JSON_EXCEPTION,
        ERROR
    }
}
