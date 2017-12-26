package com.afunx.server.impl;

import com.afunx.data.bean.MotionBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.server.interfaces.Robot;
import com.afunx.server.interfaces.RobotAdapter;
import com.afunx.server.log.LogUtils;

import java.util.List;

/**
 * RobotAdapterImpl is used only for debug without real robot
 * <p>
 * Created by afunx on 26/12/2017.
 */

public class RobotAdapterImpl implements RobotAdapter {

    private static final String TAG = "RobotAdapterImpl";

    private void setRobotIdle(Robot robot) {
        robot.setState(Robot.STATE.IDLE);
    }

    @Override
    public int queryMotors(List<MotorBean> motorBeanList, Robot robot) {
        LogUtils.log(TAG, "queryMotors()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execMotors(List<MotorBean> motorBeanList, Robot robot) {
        LogUtils.log(TAG, "execMotors()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int cancelAllMotors(Robot robot) {
        LogUtils.log(TAG, "cancelAllMotors()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execEnterReadmode(Robot robot) {
        LogUtils.log(TAG, "execEnterReadmode()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execExitReadmode(Robot robot) {
        LogUtils.log(TAG, "execExitReadmode()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int queryMotion(int[] frameIndex, Robot robot) {
        LogUtils.log(TAG, "queryMotion()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int prepareMotion(MotionBean motionBean, Robot robot) {
        LogUtils.log(TAG, "prepareMotion()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execMotion(String motionName, Robot robot) {
        LogUtils.log(TAG, "execMotion()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int queryMotions(int[] motionIndex, Robot robot) {
        LogUtils.log(TAG, "queryMotions()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int prepareMotionList(List<MotionBean> motionBeanList, Robot robot) {
        LogUtils.log(TAG, "prepareMotionList()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int cancelAllMotions(Robot robot) {
        LogUtils.log(TAG, "cancelAllMotions()");
        setRobotIdle(robot);
        return 0;
    }
}
