package com.afunx.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.afunx.server.impl.RobotServerImpl;
import com.afunx.server.log.LogUtils;
import com.afunx.service.impl.RobotAdapterImpl;
import com.ubt.ip.mini_adapter.sdk.MiniRobot;

import java.io.IOException;

public class RobotService extends Service {

    private static final String TAG = "RobotService";
    private static final int PORT = 18266;
    private RobotServerImpl mRobotServer = null;

    private void open() {

        MiniRobot.get().setDegreeOffsets(new int[]{8, 2, -12, -1, -25, -16, 1, 20, 33, -2, -1, 2, -41, -34});
        try {
            MiniRobot.get().open();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        MiniRobot.get().setRobotMock(true);

        LogUtils.log(TAG, "open() mRobotServer is null: " + (mRobotServer == null));
        if (mRobotServer == null) {
            mRobotServer = new RobotServerImpl(PORT);
            mRobotServer.setAdapter(new RobotAdapterImpl());
            try {
                mRobotServer.start();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.log(TAG, "open() fail and close it");
                close();
            }
        }
    }

    private void close() {
        LogUtils.log(TAG, "close() mRobotServer is null: " + (mRobotServer == null));
        if (mRobotServer != null) {
            mRobotServer.stop();
            mRobotServer = null;
        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate() starting port: " + PORT);
        open();
    }

    @Override
    public void onDestroy() {
        close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
