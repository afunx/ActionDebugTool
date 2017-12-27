package com.afunx.service;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afunx.service.util.RobotServerUtils;
import com.afunx.service.util.UdpDiscoverServerUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startUdpDiscoverServer(this);
        startRobotServer(this);
    }


    private void startUdpDiscoverServer(Context context) {
        UdpDiscoverServerUtils.startService(context);
    }

    private void startRobotServer(Context context) {
        RobotServerUtils.startService(context);
    }
}
