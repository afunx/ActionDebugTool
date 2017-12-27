package com.afunx.service.impl;

import android.util.Log;

import com.afunx.data.bean.MotionBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.constants.Constants;
import com.afunx.server.interfaces.Robot;
import com.afunx.server.interfaces.RobotAdapter;
import com.ubt.ip.ctrl_motor.util.MotorUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by afunx on 27/12/2017.
 */

public class RobotAdapterImpl implements RobotAdapter {

    private static final String TAG = "RobotAdapterImpl";

    private void setRobotIdle(Robot robot) {
        robot.setState(Robot.STATE.IDLE);
    }

    @Override
    public int queryMotors(List<MotorBean> motorBeanList, Robot robot) {
        Log.d(TAG, "queryMotors() motorBeanList: " + motorBeanList);
        final int result = _queryMotors(motorBeanList);
        Log.i(TAG, "queryMotors() result: " + result + ", motorBeanList: " + motorBeanList);
        setRobotIdle(robot);
        return result;
    }

    @Override
    public int execMotors(List<MotorBean> motorBeanList, Robot robot) {
        Log.d(TAG, "execMotors() motorBeanList: " + motorBeanList);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int cancelAllMotors(Robot robot) {
        Log.d(TAG, "cancelAllMotors()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execEnterReadmode(Robot robot) {
        Log.d(TAG, "execEnterReadmode()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execExitReadmode(Robot robot) {
        Log.d(TAG, "execExitReadmode()");
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int queryMotion(int[] frameIndex, Robot robot) {
        Log.d(TAG, "queryMotion() frameIndex: " + Arrays.toString(frameIndex));
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int prepareMotion(MotionBean motionBean, Robot robot) {
        Log.d(TAG, "prepareMotion() motionBean:\n" + motionBean);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execMotion(String motionName, Robot robot) {
        Log.d(TAG, "execMotion() motionName: " + motionName);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int queryMotions(int[] motionIndex, Robot robot) {
        Log.d(TAG, "queryMotions() motionIndex: " + Arrays.toString(motionIndex));
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int prepareMotions(List<MotionBean> motionBeanList, Robot robot) {
        Log.d(TAG, "prepareMotions() motionBeanList:\n" + motionBeanList);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int execMotions(List<String> motionNameList, Robot robot) {
        Log.d(TAG, "execMotions() motionNameList: " + motionNameList);
        setRobotIdle(robot);
        return 0;
    }

    @Override
    public int cancelAllMotions(Robot robot) {
        Log.d(TAG, "cancelAllMotions()");
        setRobotIdle(robot);
        return 0;
    }

    private int _queryMotors(List<MotorBean> motorBeanList) {
        final int[] motorsId = new int[]{1, 2, 3, 4, 5, 6, 7};
        final int size = motorsId.length;
        final int[] motorsDeg = new int[size];
        int result = MotorUtil.getCurrentMotors(motorsId, motorsDeg);
        if (result == Constants.RESULT.SUC) {
            for (int i = 0; i < size; i++) {
                MotorBean motorBean = new MotorBean();
                motorBean.setId(motorsId[i]);
                motorBean.setDeg(motorsDeg[i]);
                motorBeanList.add(motorBean);
            }
        }
        return result;
    }

}
