package com.afunx.server.impl;

import com.afunx.server.interfaces.UdpDiscoverServer;
import com.afunx.server.log.LogUtils;
import com.afunx.server.util.HexUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by afunx on 21/12/2017.
 */

public class UdpDiscoverServerImpl implements UdpDiscoverServer {

    private static final String TAG = "UdpDiscoverServerImpl";
    private volatile boolean isStarted = false;
    private volatile boolean isStopped = false;
    private volatile int port = -1;
    private volatile DatagramSocket udpSocket;
    private final byte[] bufRec = new byte[4];
    private final DatagramPacket packetRec = new DatagramPacket(bufRec, bufRec.length);
    private final byte[] bufSend = new byte[4];
    private final DatagramPacket packetSend = new DatagramPacket(bufSend, bufSend.length);

    @Override
    public synchronized boolean start(int port, byte[] secret, byte[] reply) {
        boolean isSuc = _start(port, secret, reply);
        LogUtils.log(TAG, "start() port: " + port + ", isSuc: " + isSuc);
        return isSuc;
    }

    @Override
    public synchronized void stop() {
        LogUtils.log(TAG, "stop()");
        _stop();
    }

    private synchronized void reopen() {
        boolean isSuc = _reopen();
        LogUtils.log(TAG, "reopen() isSuc: " + isSuc);
    }

    private synchronized void close() {
        LogUtils.log(TAG, "close()");
        _close();
    }

    private InetAddress getLoopbackAddress() throws UnknownHostException {
        return InetAddress.getLoopbackAddress();
    }

    private boolean _start(int port, byte[] secret, byte[] reply) {
        if (this.isStarted) {
            LogUtils.log(TAG, "_start() port: " + port + " ERROR!!!!!! only could be called onces");
            return false;
        }
        this.isStarted = true;
        this.port = port;
        try {
            udpSocket = new DatagramSocket(port, getLoopbackAddress());
            _listen(udpSocket, packetRec, secret, packetSend, reply);
            return true;
        } catch (IOException e) {
            LogUtils.log(TAG, "_start() port: " + port + " IOException");
        }
        return false;
    }

    private boolean _reopen() {
        if (isStopped) {
            LogUtils.log(TAG, "_reopen() isStopped already, ignore _reopen()");
            return false;
        }
        try {
            udpSocket = new DatagramSocket(port, getLoopbackAddress());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void _listen(final DatagramSocket socket, final DatagramPacket packet, final byte[] secret, final DatagramPacket packetReply, final byte[] reply) {
        new Thread() {
            @Override
            public void run() {
                while (!isStopped) {
                    try {
                        LogUtils.log(TAG, "_listen() receive...");
                        // receive client data
                        socket.receive(packet);
                        LogUtils.log(TAG, "_listen() receive: " + HexUtils.bytes2HexString(packet.getData(), packet.getOffset(), packet.getLength()));
                        // check client data valid
                        boolean isValid = _isValid(bufRec, packet.getLength(), secret);
                        if (isValid) {
                            // reply ip address to client
                            _reply(socket, packet, packetReply, reply);
                        }
                    } catch (IOException e) {
                        LogUtils.log(TAG, "_listen() IOException");
                        close();
                        if (!isStopped) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            reopen();
                        }
                    }
                }
                LogUtils.log(TAG, "_listen() exit");
            }
        }.start();
    }

    private boolean _isValid(byte[] buffer, int length, byte[] secret) {
        // request
        for (int i = 0; i <= length - secret.length; i++) {
            boolean isValid = true;
            for (int j = 0; j < secret.length && isValid; j++) {
                if (buffer[i + j] != secret[j]) {
                    isValid = false;
                }
            }
            if (isValid) {
                return true;
            }
        }
        return false;
    }

    private void _reply(DatagramSocket socket, DatagramPacket packet, DatagramPacket packetReply, byte[] reply) {
        try {
            LogUtils.log(TAG, "_reply() send: " + HexUtils.bytes2HexString(reply));
            // send reply to client
            packetReply.setAddress(packet.getAddress());
            packetReply.setPort(packet.getPort());
            packetReply.setData(reply);
            socket.send(packetReply);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _stop() {
        this.isStopped = true;
        close();
    }

    private void _close() {
        if (this.udpSocket != null) {
            LogUtils.log(TAG, "_close() port: " + port);
            this.udpSocket.close();
            this.udpSocket = null;
        }
    }

    public static void main(String args[]) {
        final UdpDiscoverServerImpl obj = new UdpDiscoverServerImpl();
        final byte[] secret = new byte[]{0x75, 0x62, 0x74};
        final byte[] reply = new byte[]{0x01, 0x02, 0x03, 0x04};
        boolean isSuc = obj.start(12000, secret, reply);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                obj.stop();
            }
        }.start();

        for (int i = 0; i < 10000; i++) {
            // client
            final byte[] request = secret;
            DatagramPacket datagramPacket = new DatagramPacket(request, request.length, InetAddress.getLoopbackAddress(), 12000);
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.setSoTimeout(1000);
                LogUtils.log(TAG, "send...");
                socket.send(datagramPacket);
                LogUtils.log(TAG, "sent");
                byte[] rec = new byte[4];
                datagramPacket.setData(rec);
                socket.receive(datagramPacket);
                LogUtils.log(TAG, "rec: " + Arrays.toString(datagramPacket.getData()));

                datagramPacket.setData(secret);
                LogUtils.log(TAG, "send...");
                socket.send(datagramPacket);
                LogUtils.log(TAG, "sent");
                datagramPacket.setData(rec);
                socket.receive(datagramPacket);
                LogUtils.log(TAG, "rec: " + Arrays.toString(datagramPacket.getData()));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            System.out.println("count: " + i);
        }
        System.out.println("FINISH!!!!!!");
    }
}