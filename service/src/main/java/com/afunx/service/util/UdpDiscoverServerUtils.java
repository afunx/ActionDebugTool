package com.afunx.service.util;

import android.content.Context;
import android.content.Intent;

import com.afunx.service.service.UdpDiscoverService;

/**
 * Created by afunx on 22/12/2017.
 */

public class UdpDiscoverServerUtils {

    public static void startService(Context context) {
        Intent intent = new Intent(context, UdpDiscoverService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, UdpDiscoverService.class);
        context.stopService(intent);
    }
}
