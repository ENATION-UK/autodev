package com.enation.autodev;

import com.google.gson.Gson;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class GsonHelper {
    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
