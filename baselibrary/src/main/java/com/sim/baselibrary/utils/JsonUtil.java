package com.sim.baselibrary.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * @author Sim --- Json字符串与对象集合的转换工具类
 */
public class JsonUtil {

    /**
     * 将对象集合转化为json字符串
     */
    public static <T> T jsonToObject(String json, Class<T> beanClass) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, beanClass);
    }

    /**
     * 将json字符串对象集合
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        Gson gson = new Gson();
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }

}
