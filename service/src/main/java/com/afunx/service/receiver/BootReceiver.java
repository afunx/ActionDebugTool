package com.afunx.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.afunx.service.util.UdpDiscoverServerUtils;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive() receive boot broadcast");
        startUdpDiscoverServer(context);
    }

    private void startUdpDiscoverServer(Context context) {
        UdpDiscoverServerUtils.startService(context);
    }
}