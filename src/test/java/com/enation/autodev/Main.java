package com.enation.autodev;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String json = "[{\"用户模块\":\"注册\"},{\"用户模块\":\"登录\"},{\"文章模块\":\"发布\"},{\"文章模块\":\"评论\"}]";

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
        List<Map<String, String>> list = gson.fromJson(json, listType);

        for (Map<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }
        }
    }
}
