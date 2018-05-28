package com.example.core.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by yunchang on 2018/5/27.
 */

public class FileUtils {

    public static String getScript(Context context, String fileName) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            String content = new String(buffer, Charset.forName("UTF-8"));
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
