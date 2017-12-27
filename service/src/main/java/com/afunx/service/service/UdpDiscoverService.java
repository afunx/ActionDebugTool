package com.afunx.service.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.afunx.server.impl.UdpDiscoverServerImpl;
import com.afunx.server.interfaces.UdpDiscoverServer;
import com.afunx.server.util.HexUtils;

public class UdpDiscoverService extends Service {

    private static final String TAG = "UdpDiscoverService";
    private static final int PORT = 32866;
    private static final byte[] secret = new byte[]{0x75, 0x62, 0x74};
    private UdpDiscoverServer mUdpDiscoverServer = null;

    private byte[] getInetAddress() {
        final Context context = getApplicationContext();
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                int ip = wifiInfo.getIpAddress();
                if (ip != 0) {
                    return new byte[]{(byte) (ip & 0xff), (byte) ((ip >> 8) & 0xff)
                            , (byte) ((ip >> 16) & 0xff), (byte) ((ip >> 24) & 0xff)};
                }
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        byte[] inetAddr = getInetAddress();
        Log.e(TAG, "onCreate() starting inetAddr: " + HexUtils.bytes2HexString(inetAddr) + ", port: " + PORT);
        if (inetAddr != null) {
            mUdpDiscoverServer = new UdpDiscoverServerImpl();
            mUdpDiscoverServer.start(PORT, secret, inetAddr);
        }
    }

    @Override
    public void onDestroy() {
        if (mUdpDiscoverServer != null) {
            mUdpDiscoverServer.stop();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
