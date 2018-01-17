package com.afunx.actiondebugtool.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by afunx on 17/01/2018.
 */

public class FileUtils {

    private static boolean DEBUG = true;
    private static String TAG = "FileUtils";

    /**
     * write String to sd card
     *
     * @param path    file path
     * @param content content
     * @return write suc or not
     */
    public static boolean writeString(String path, String content) {
        if (DEBUG) {
            Log.d(TAG, "writeString() path: " + path + ", content: " + content);
        }
        try {
            File file = new File(path);
            // create directory if necessary
            String parent = file.getParent();
            File dir = new File(parent);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // create file if necessary
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * read String from sd card
     *
     * @param path file path
     * @return content
     */
    public static String readString(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String line = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ignore) {
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignore) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * list files under the path
     *
     * @param path file path
     * @return files under the path
     */
    public static List<String> list(String path) {
        File file = new File(path);
        String[] files = file.list();
        if (files == null) {
            return Collections.emptyList();
        }
        List<String> list = Arrays.asList(files);
        return list;
    }
}
