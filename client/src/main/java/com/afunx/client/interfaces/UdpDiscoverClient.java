package com.afunx.client.interfaces;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by afunx on 22/12/2017.
 */

public interface UdpDiscoverClient {

    /**
     * discover udp discover server
     *
     * @param port      udp port
     * @param soTimeout socket timeout in milliseconds(when receive new response, it will reset)
     * @return udp discover server ip address list
     */
    List<InetAddress> discover(byte[] secret, int port, int soTimeout);

}
