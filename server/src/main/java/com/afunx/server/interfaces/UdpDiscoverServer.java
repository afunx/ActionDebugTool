package com.afunx.server.interfaces;

/**
 * Created by afunx on 21/12/2017.
 */

public interface UdpDiscoverServer {

    /**
     * start udp discover server
     *
     * @param port   udp port
     * @param secret secret request sent from by client
     * @param reply  response reply to client
     * @return isSuc or not
     */
    boolean start(int port, byte[] secret, byte[] reply);

    /**
     * stop udp discover server
     */
    void stop();
}
