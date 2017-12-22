package com.afunx.server.log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by afunx on 21/12/2017.
 */

public class LogUtils {

    private static final boolean DEBUG = true;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public static void log(String tag, String msg) {
        Date date = new Date();
        System.out.println( sdf.format(date)+"|" + tag + ": " + msg);
    }

}