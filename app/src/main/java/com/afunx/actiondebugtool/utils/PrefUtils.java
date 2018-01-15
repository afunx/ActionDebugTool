package com.afunx.actiondebugtool.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by afunx on 15/01/2018.
 */

public class PrefUtils {

    private static final String FILE_NAME = "ACTION_LIST_FILE";

    private static final String FILE_KEY = "ACTION_LIST";

    /**
     * save object to SharedPreferences
     *
     * @param appContext application context
     * @param object     object to be saved
     */
    public static void saveObject(Context appContext, Object object) {
        saveObject(appContext, FILE_NAME, FILE_KEY, object);
    }

    /**
     * read object from SharedPreferences
     *
     * @param appContext application context
     * @return object saved in SharedPreferences
     */
    public static Object readObject(Context appContext) {
        return readObject(appContext, FILE_NAME, FILE_KEY);
    }

    /**
     * save object to SharedPreferences
     *
     * @param appContext application context
     * @param name       sharedPreferences name
     * @param key        sharedPreferences key
     * @param object     object to be saved
     */
    private static void saveObject(Context appContext, String name, String key, Object object) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            String data = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, data);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read object from SharedPreferences
     *
     * @param appContext application context
     * @param name       sharedPreferences name
     * @param key        sharedPreferences key
     * @return object saved in SharedPreferences
     */
    private static Object readObject(Context appContext, String name, String key) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(key, null);
        if (data == null) {
            return null;
        }
        byte[] base64 = Base64.decode(data.getBytes(), Base64.DEFAULT);

        try {
            ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(base64));
            try {
                return bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
