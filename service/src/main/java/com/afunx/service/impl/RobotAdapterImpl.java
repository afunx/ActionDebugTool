package com.afunx.service.impl;

import android.util.Log;

import com.afunx.data.bean.ActionBean;
import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.constants.Constants;
import com.afunx.server.interfaces.Robot;
import com.afunx.server.interfaces.RobotAdapter;
import com.afunx.service.interfaces.Recorder;
import com.ubt.ip.ctrl_motor.listener.OpListener;
import com.ubt.ip.ctrl_motor.util.MotorUtil;
import com.ubt.ip.sdk.api.LogApi;
import com.ubt.ip.sdk.constants.SdkConstants;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by afunx on 27/12/2017.
 */

public class RobotAdapterImpl implements RobotAdapter {

    private static final String TAG = "RobotAdapterImpl";

    private static final Recorder mRecorder = new RecorderImpl();

    static {
        LogApi.get().setEnabled(false);
    }

    private void setRobotIdle(Robot robot) {
        robot.setState(Robot.STATE.IDLE);
    }

    @Override
    public int queryMotors(List<MotorBean> motorBeanList, Robot robot) {
        Log.d(TAG, "queryMotors() motorBeanList: " + motorBeanList);
        final int result = _queryMotors(motorBeanList, robot);
        Log.i(TAG, "queryMotors() result: " + result + ", motorBeanList: " + motorBeanList);
        return result;
    }

    @Override
    public int execMotors(FrameBean frameBean, Robot robot) {
        Log.d(TAG, "execMotors() FrameBean:\n" + frameBean);
        final int result = _execMotors(frameBean, robot);
        Log.i(TAG, "execMotors() result: " + result + ", frameBean: " + frameBean);
        return result;
    }

    @Override
    public int cancelAllMotors(Robot robot) {
        Log.d(TAG, "cancelAllMotors()");
        final int result = _cancelAllMotors(robot);
        Log.i(TAG, "cancelAllMotors() result: " + result);
        return result;
    }

    @Override
    public int execEnterReadmode(Robot robot) {
        Log.d(TAG, "execEnterReadmode()");
        final int result = _execEnterReadmode(robot);
        Log.i(TAG, "execEnterReadmode() result: " + result);
        return result;
    }


    @Override
    public int execExitReadmode(Robot robot) {
        Log.d(TAG, "execExitReadmode()");
        final int result = _execExitReadmode(robot);
        Log.i(TAG, "execExitReadmode() result: " + result);
        return result;
    }

    @Override
    public int queryAction(int[] frameIndex, Robot robot) {
        Log.d(TAG, "queryAction() frameIndex: " + Arrays.toString(frameIndex));
        final Recorder recorder = mRecorder;
        final int result = _queryAction(recorder, frameIndex, robot);
        Log.i(TAG, "queryAction() result: " + result + ", frameIndex: " + Arrays.toString(frameIndex));
        return result;
    }

    @Override
    public int prepareAction(ActionBean actionBean, Robot robot) {
        Log.d(TAG, "prepareAction() actionBean:\n" + actionBean);
        final Recorder recorder = mRecorder;
        final int result = _prepareAction(recorder, actionBean, robot);
        Log.i(TAG, "prepareAction() result: " + result + ", actionBean: " + actionBean);
        return result;
    }

    @Override
    public int execAction(String actionName, Robot robot) {
        Log.d(TAG, "execAction() actionName: " + actionName);
        final Recorder recorder = mRecorder;
        final int result = _execAction(recorder, actionName, robot);
        Log.i(TAG, "execAction() result: " + result + ", actionName: " + actionName);
        return result;
    }

    @Override
    public int queryActions(int[] actionIndex, Robot robot) {
        Log.d(TAG, "queryActions() actionIndex: " + Arrays.toString(actionIndex));
        final Recorder recorder = mRecorder;
        final int result = _queryActions(recorder, actionIndex, robot);
        Log.d(TAG, "queryActions() result: " + result + ", actionIndex: " + Arrays.toString(actionIndex));
        return result;
    }

    @Override
    public int prepareActions(List<ActionBean> actionBeanList, Robot robot) {
        Log.d(TAG, "prepareActions() actionBeanList:\n" + actionBeanList);
        final Recorder recorder = mRecorder;
        final int result = _prepareActions(recorder, actionBeanList, robot);
        Log.i(TAG, "prepareAction() result: " + result + ", actionBeanList: " + actionBeanList);
        return result;
    }

    @Override
    public int execActions(List<String> actionNameList, Robot robot) {
        Log.d(TAG, "execActions() actionNameList: " + actionNameList);
        final Recorder recorder = mRecorder;
        final int result = _execActions(recorder, actionNameList, robot);
        Log.i(TAG, "execAction() result: " + result + ", actionNameList: " + actionNameList);
        return result;
    }

    @Override
    public int cancelAllActions(Robot robot) {
        Log.d(TAG, "cancelAllActions()");
        final Recorder recorder = mRecorder;
        final int result = _cancelAllActions(recorder, robot);
        Log.i(TAG, "cancelAllMotors() result: " + result);
        return result;
    }

    private int _queryMotors(List<MotorBean> motorBeanList, final Robot robot) {
        final int[] motorsId = new int[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12, 13, 14};
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

    private int _execMotors(final FrameBean frameBean, final Robot robot) {
        final int[] motorsId = new int[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12, 13, 14};
        final int size = motorsId.length;
        final int[] motorsDeg = new int[size];
        Arrays.fill(motorsDeg, SdkConstants.MotorDegree.KEEP_DEGREE);
        for (int i = 0; i < frameBean.getMotorBeanList().size(); i++) {
            MotorBean motorBean = frameBean.getMotorBeanList().get(i);
            motorsDeg[motorBean.getId() - 1] = motorBean.getDeg();
        }
        final int time = frameBean.getTime();
        final int[] taskId = new int[1];
        final int result = MotorUtil.setMotors(motorsId, motorsDeg, time, 0, taskId, new OpListener() {
            @Override
            public void onStart() {
                Log.d(TAG, "_execMotors() onStart()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "_execMotors() onComplete()");
                setRobotIdle(robot);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "_execMotors() onCancel()");
                setRobotIdle(robot);
            }

            @Override
            public void onError(int errorCode) {
                Log.d(TAG, "_execMotors() onError() errorCode: " + errorCode);
                setRobotIdle(robot);
            }
        });
        return result;
    }

    private int _cancelAllMotors(final Robot robot) {
        boolean[] isSuc = new boolean[1];
        final int result = MotorUtil.cancelAllOperation(isSuc);
        setRobotIdle(robot);
        if (result == Constants.RESULT.SUC && !isSuc[0]) {
            return Constants.RESULT.FAIL;
        }
        return result;
    }

    private int _execEnterReadmode(final Robot robot) {
        final int[] motorsId = new int[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12, 13, 14};
        final boolean[] isSuc = new boolean[1];
        final int result = MotorUtil.enterReadMode(isSuc, motorsId);
        setRobotIdle(robot);
        if (result == Constants.RESULT.SUC && !isSuc[0]) {
            return Constants.RESULT.FAIL;
        }
        return result;
    }

    private int _execExitReadmode(final Robot robot) {
        final int[] motorsId = new int[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12, 13, 14};
        final boolean[] isSuc = new boolean[1];
        final int result = MotorUtil.exitReadMode(isSuc, motorsId);
        setRobotIdle(robot);
        if (result == Constants.RESULT.SUC && !isSuc[0]) {
            return Constants.RESULT.FAIL;
        }
        return result;
    }

    private int _queryAction(final Recorder recorder, final int[] frameIndex, final Robot robot) {
        frameIndex[0] = recorder.getFrameIndex();
        return Constants.RESULT.SUC;
    }

    private int _prepareAction(final Recorder recorder, final ActionBean actionBean, final Robot robot) {
        final int result;
        if (actionBean.getName() == null) {
            result = Constants.RESULT.ACTION_NAME_NULL;
        } else {
            recorder.putActionBean(actionBean);
            result = Constants.RESULT.SUC;
        }
        setRobotIdle(robot);
        return result;
    }

    private boolean __execFrame(final Semaphore semaphore, final FrameBean frameBean, final Robot robot, final boolean isLast, final int frameIndex) {
        Log.d(TAG, "__execFrame() frameIndex: " + frameIndex);
        final int[] motorsId = new int[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10, 11, 12, 13, 14};
        final int size = motorsId.length;
        final int[] motorsDeg = new int[size];
        Arrays.fill(motorsDeg, SdkConstants.MotorDegree.KEEP_DEGREE);
        for (int i = 0; i < frameBean.getMotorBeanList().size(); i++) {
            MotorBean motorBean = frameBean.getMotorBeanList().get(i);
            motorsDeg[motorBean.getId() - 1] = motorBean.getDeg();
        }
        final int time = frameBean.getTime();
        final int[] taskId = new int[1];
        final int result = MotorUtil.setMotors(motorsId, motorsDeg, time, 0, taskId, new OpListener() {
            @Override
            public void onStart() {
                Log.i(TAG, "__execFrame() frameIndex: " + frameIndex + " onStart()");

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "__execFrame() frameIndex: " + frameIndex + " onComplete()");
                if (isLast) {
                    setRobotIdle(robot);
                }
                semaphore.release();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "__execFrame() frameIndex: " + frameIndex + " onCancel()");
                setRobotIdle(robot);
                semaphore.release();
            }

            @Override
            public void onError(int errorCode) {
                setRobotIdle(robot);
                Log.e(TAG, "__execFrame() frameIndex: " + frameIndex + " onError() errorCode: " + errorCode);
                semaphore.release();
            }
        });
        return result == Constants.RESULT.SUC;
    }

    private void __waitFrameStop(final Semaphore semaphore) {
        Log.d(TAG, "__waitFrameStop() enter");
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "__waitFrameStop() exit");
    }

    private void __execAction(final Recorder recorder, final ActionBean actionBean, final Robot robot, final boolean isLast) {
        final List<FrameBean> frameBeans = actionBean.getFrameBeanList();
        final int size = frameBeans.size();
        Log.d(TAG, "__execAction() size: " + size + ", isCancelled: " + recorder.isCancelled());
        final Semaphore semaphore = new Semaphore(0);
        for (int index = 0; index < size && !recorder.isCancelled(); ++index) {
            // set frame index
            recorder.setFrameIndex(index);
            __execFrame(semaphore, frameBeans.get(index), robot, isLast && index == size - 1, index);
            __waitFrameStop(semaphore);
        }
    }

    private int _execAction(final Recorder recorder, final String actionName, final Robot robot) {
        final int result;
        final ActionBean actionBean = recorder.getActionBean(actionName);
        if (actionBean == null) {
            result = Constants.RESULT.ACTION_ABSENT;
            setRobotIdle(robot);
        } else {
            recorder.setIsCancelled(false);
            new Thread() {
                @Override
                public void run() {
                    // set action index 0
                    recorder.setActionIndex(0);
                    __execAction(recorder, actionBean, robot, true);
                    // set frame index -1
                    recorder.setFrameIndex(-1);
                    // set action index -1
                    recorder.setActionIndex(-1);
                }
            }.start();
            result = Constants.RESULT.SUC;
        }
        return result;
    }

    private int _queryActions(Recorder recorder, int[] actionIndex, Robot robot) {
        actionIndex[0] = recorder.getActionIndex();
        return Constants.RESULT.SUC;
    }


    private int _prepareActions(Recorder recorder, List<ActionBean> actionBeanList, Robot robot) {
        int result = Constants.RESULT.SUC;
        for (ActionBean actionBean : actionBeanList) {
            if (actionBean.getName() == null) {
                result = Constants.RESULT.ACTION_NAME_NULL;
                break;
            }
        }
        if (result == Constants.RESULT.SUC) {
            for (ActionBean actionBean : actionBeanList) {
                recorder.putActionBean(actionBean);
            }
        }
        setRobotIdle(robot);
        return result;
    }

    private int _execActions(final Recorder recorder, final List<String> actionNameList,final Robot robot) {
        int result = Constants.RESULT.SUC;
        for (String actionName : actionNameList) {
            if (recorder.getActionBean(actionName) == null) {
                result = Constants.RESULT.ACTION_ABSENT;
                break;
            }
        }
        if (result == Constants.RESULT.SUC) {
            recorder.setIsCancelled(false);
            new Thread() {
                @Override
                public void run() {
                    for (int actionIndex = 0; actionIndex < actionNameList.size(); actionIndex++) {
                        final String actionName = actionNameList.get(actionIndex);
                        final ActionBean actionBean = recorder.getActionBean(actionName);
                        // set action index
                        recorder.setActionIndex(actionIndex);
                        __execAction(recorder, actionBean, robot, actionIndex == actionNameList.size() - 1);
                    }
                    // set frame index -1
                    recorder.setFrameIndex(-1);
                    // set action index -1
                    recorder.setActionIndex(-1);
                }
            }.start();
            result = Constants.RESULT.SUC;
        } else {
            setRobotIdle(robot);
        }

        return result;
    }

    private int _cancelAllActions(final Recorder recorder, final Robot robot) {
        recorder.setIsCancelled(true);
        return _cancelAllMotors(robot);
    }

}
