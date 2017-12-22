package com.afunx.server;

/**
 * Created by afunx on 21/12/2017.
 */

public class UdpDiscoverServer {

    private UdpDiscoverServer() {
    }

    public static UdpDiscoverServer get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final UdpDiscoverServer INSTANCE = new UdpDiscoverServer();
    }
}