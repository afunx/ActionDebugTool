package com.afunx.server.impl;

import com.afunx.data.bean.FrameBean;
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
        LogUtils.log(TAG, "queryMotors() motorBeanList: " + motorBeanList);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execMotors(FrameBean frameBean, Robot robot) {
        LogUtils.log(TAG, "execMotors() frameBean:\n" + frameBean);
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
        LogUtils.log(TAG, "queryMotion() frameIndex[0] = 1");
        setRobotIdle(robot);
        frameIndex[0] = 1;
        return 0;
    }

    @Override
    public int prepareMotion(MotionBean motionBean, Robot robot) {
        LogUtils.log(TAG, "prepareMotion() motionBean: " + motionBean);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execMotion(String motionName, Robot robot) {
        LogUtils.log(TAG, "execMotion() motionName: " + motionName);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int queryMotions(int[] motionIndex, Robot robot) {
        LogUtils.log(TAG, "queryMotions() motionIndex[0] = 2");
        setRobotIdle(robot);
        motionIndex[0] = 2;
        return 0;
    }

    @Override
    public int prepareMotions(List<MotionBean> motionBeanList, Robot robot) {
        LogUtils.log(TAG, "prepareMotionList()");
        setRobotIdle(robot);
        LogUtils.log(TAG, "motionBeanList:\n" + motionBeanList);
        return 0;
    }

    @Override
    public int execMotions(List<String> motionNameList, Robot robot) {
        LogUtils.log(TAG, "execMotionsList() motionNameList: " + motionNameList);
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
