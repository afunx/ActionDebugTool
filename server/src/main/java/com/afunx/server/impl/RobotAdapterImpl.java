package com.afunx.server.impl;

import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.ActionBean;
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
    public int execEnterReadmodeOne(Robot robot, int motorId) {
        LogUtils.log(TAG, "execEnterReadmodeOne() motorId: " + motorId);
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
    public int queryAction(int[] frameIndex, Robot robot) {
        LogUtils.log(TAG, "queryAction() frameIndex[0] = 1");
        setRobotIdle(robot);
        frameIndex[0] = 1;
        return 0;
    }

    @Override
    public int prepareAction(ActionBean actionBean, Robot robot) {
        LogUtils.log(TAG, "prepareAction() actionBean: " + actionBean);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execAction(String actionName, Robot robot) {
        LogUtils.log(TAG, "execAction() actionName: " + actionName);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int queryActions(int[] actionIndex, Robot robot) {
        LogUtils.log(TAG, "queryActions() actionIndex[0] = 2");
        setRobotIdle(robot);
        actionIndex[0] = 2;
        return 0;
    }

    @Override
    public int prepareActions(List<ActionBean> actionBeanList, Robot robot) {
        LogUtils.log(TAG, "prepareMotionList()");
        setRobotIdle(robot);
        LogUtils.log(TAG, "actionBeanList:\n" + actionBeanList);
        return 0;
    }

    @Override
    public int execActions(List<String> actionNameList, Robot robot) {
        LogUtils.log(TAG, "execMotionsList() actionNameList: " + actionNameList);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int cancelAllActions(Robot robot) {
        LogUtils.log(TAG, "cancelAllActions()");
        setRobotIdle(robot);
        return 0;
    }
}
