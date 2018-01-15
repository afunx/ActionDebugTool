package com.afunx.actiondebugtool.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 15/01/2018.
 */

public class GsonUtils {

    private static final Gson gson;
    private static final JsonParser jsonParser;

    static {
        gson = new GsonBuilder().create();
        jsonParser = new JsonParser();
    }

    /**
     * parse object bo json String
     *
     * @param object object to be parsed
     * @return json String
     */
    public static String toGson(@NonNull Object object) {
        return gson.toJson(object);
    }

    /**
     * parse object from json String
     *
     * @param <T>      the type of the desired object
     * @param json     json String
     * @param classOfT class of type
     * @return         object
     */
    public static <T> T fromGson(@NonNull String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * parse object list from json list String
     *
     * @param <T>      the type of the desired object
     * @param jsonList json list String
     * @param classOfT class of list type
     * @return object list
     */
    public static <T> List<T> fromGsonList(@NonNull String jsonList, Class<T> classOfT) {
        List<T> list = new ArrayList<>();
        JsonArray jsonArray = jsonParser.parse(jsonList).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            list.add(gson.fromJson(jsonElement, classOfT));
        }
        return list;
    }
}
