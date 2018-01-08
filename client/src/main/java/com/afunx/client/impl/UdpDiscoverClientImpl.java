package com.afunx.client.impl;

import com.afunx.client.interfaces.UdpDiscoverClient;
import com.afunx.client.log.LogUtils;
import com.afunx.client.util.HexUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 22/12/2017.
 */

public class UdpDiscoverClientImpl implements UdpDiscoverClient {

    private static final String TAG = "UdpDiscoverClientImpl";
    private final byte[] bufRec = new byte[4];
    private final DatagramPacket packetRec = new DatagramPacket(bufRec, bufRec.length);

    @Override
    public synchronized List<InetAddress> discover(byte[] secret, int port, int soTimeout) {
        LogUtils.log(TAG, "discover() secret: " + HexUtils.bytes2HexString(secret) + ", port: " + port + ", soTimeout: " + soTimeout);
        return _discover(secret, port, soTimeout, this.packetRec);
    }

    private DatagramSocket send(byte[] secret, int port) {
        LogUtils.log(TAG, "send() secret: " + HexUtils.bytes2HexString(secret) + ", port: " + port);
        return _send(secret, port);
    }

    private InetAddress receive(DatagramSocket socket, DatagramPacket packet, int soTimeout) {
        LogUtils.log(TAG, "receive() soTimeout: " + soTimeout);
        return _receive(socket, packet, soTimeout);
    }

    private InetAddress getBroadcastInetAddress() throws UnknownHostException {
        return InetAddress.getByName("255.255.255.255");
    }

    private List<InetAddress> _discover(byte[] secret, int port, int soTimeout, DatagramPacket packet) {
        List<InetAddress> list = new ArrayList<>();
        DatagramSocket socket = send(secret, port);
        if (socket == null) {
            LogUtils.log(TAG, "_discover() secret: " + HexUtils.bytes2HexString(secret) + ", port: " + port + ", soTimeout: " + soTimeout + " send fail");
            return list;
        }
        InetAddress inetAddr = null;
        do {
            inetAddr = receive(socket, packet, soTimeout);
            if (inetAddr != null) {
                list.add(inetAddr);
            }
        } while (inetAddr != null);
        return list;
    }

    private DatagramSocket _send(byte[] secret, int port) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(secret, secret.length, getBroadcastInetAddress(), port);
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.send(datagramPacket);
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InetAddress _receive(DatagramSocket socket, DatagramPacket packet, int soTimeout) {
        try {
            socket.setSoTimeout(soTimeout);
            socket.receive(packet);
            LogUtils.log(TAG, "_receive() receive: " + HexUtils.bytes2HexString(packet.getData(), packet.getOffset(), packet.getLength()));
            return InetAddress.getByAddress(packet.getData());
        } catch (IOException ignore) {
            // it will always timeout, make it ignore just to keep developer from unnecessary panic
        }
        return null;
    }

    public static void main(String args[]) {
        // when test it, please update getBroadcastInetAddress() "127.0.0.1"
        UdpDiscoverClientImpl client = new UdpDiscoverClientImpl();
        byte[] secret = new byte[]{0x75, 0x62, 0x74};
        List<InetAddress> inetAddressList = client.discover(secret, 32866, 2000);
        LogUtils.log(TAG, "inetAddressList: " + inetAddressList);
    }
}
