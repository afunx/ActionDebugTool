package com.afunx.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.afunx.service.util.UdpDiscoverServerUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive() receive network change");
        reStartUdpDiscoverServer(context);
    }

    private void reStartUdpDiscoverServer(Context context) {
        UdpDiscoverServerUtils.stopService(context);
        UdpDiscoverServerUtils.startService(context);
    }
}
