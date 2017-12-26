package com.afunx.server.impl;

import com.afunx.server.interfaces.Robot;

/**
 * Created by afunx on 25/12/2017.
 */

public class RobotImpl implements Robot {

    private STATE state = STATE.IDLE;

    private RobotImpl() {
    }

    public static RobotImpl get() {
        return SingletonHolder.INSTANCE;
    }


    @Override
    public void setState(STATE newState) {
        this.state = newState;
    }

    @Override
    public STATE getState() {
        return state;
    }

    @Override
    public boolean isIdle() {
        return state == STATE.IDLE;
    }

    private static class SingletonHolder {
        private static final RobotImpl INSTANCE = new RobotImpl();
    }
}
