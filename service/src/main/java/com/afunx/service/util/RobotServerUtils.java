package com.afunx.service.util;

import android.content.Context;
import android.content.Intent;

import com.afunx.service.service.RobotService;

/**
 * Created by afunx on 27/12/2017.
 */

public class RobotServerUtils {

    public static void startService(Context context) {
        Intent intent = new Intent(context, RobotService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, RobotService.class);
        context.stopService(intent);
    }

}
