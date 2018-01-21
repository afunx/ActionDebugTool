package com.afunx.service.impl;

import android.util.Log;

import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotionBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.constants.Constants;
import com.afunx.server.interfaces.Robot;
import com.afunx.server.interfaces.RobotAdapter;
import com.afunx.server.log.LogUtils;
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
    public int queryMotion(int[] frameIndex, Robot robot) {
        Log.d(TAG, "queryMotion() frameIndex: " + Arrays.toString(frameIndex));
        final Recorder recorder = mRecorder;
        final int result = _queryMotion(recorder, frameIndex, robot);
        Log.i(TAG, "queryMotion() result: " + result + ", frameIndex: " + Arrays.toString(frameIndex));
        return result;
    }

    @Override
    public int prepareMotion(MotionBean motionBean, Robot robot) {
        Log.d(TAG, "prepareMotion() motionBean:\n" + motionBean);
        final Recorder recorder = mRecorder;
        final int result = _prepareMotion(recorder, motionBean, robot);
        Log.i(TAG, "prepareMotion() result: " + result + ", motionBean: " + motionBean);
        return result;
    }

    @Override
    public int execMotion(String motionName, Robot robot) {
        Log.d(TAG, "execMotion() motionName: " + motionName);
        final Recorder recorder = mRecorder;
        final int result = _execMotion(recorder, motionName, robot);
        Log.i(TAG, "execMotion() result: " + result + ", motionName: " + motionName);
        return result;
    }

    @Override
    public int queryMotions(int[] motionIndex, Robot robot) {
        Log.d(TAG, "queryMotions() motionIndex: " + Arrays.toString(motionIndex));
        final Recorder recorder = mRecorder;
        final int result = _queryMotions(recorder, motionIndex, robot);
        Log.d(TAG, "queryMotions() result: " + result + ", motionIndex: " + Arrays.toString(motionIndex));
        return result;
    }

    @Override
    public int prepareMotions(List<MotionBean> motionBeanList, Robot robot) {
        Log.d(TAG, "prepareMotions() motionBeanList:\n" + motionBeanList);
        final Recorder recorder = mRecorder;
        final int result = _prepareMotions(recorder, motionBeanList, robot);
        Log.i(TAG, "prepareMotion() result: " + result + ", motionBeanList: " + motionBeanList);
        return result;
    }

    @Override
    public int execMotions(List<String> motionNameList, Robot robot) {
        Log.d(TAG, "execMotions() motionNameList: " + motionNameList);
        final Recorder recorder = mRecorder;
        final int result = _execMotions(recorder, motionNameList, robot);
        Log.i(TAG, "execMotion() result: " + result + ", motionNameList: " + motionNameList);
        return result;
    }

    @Override
    public int cancelAllMotions(Robot robot) {
        Log.d(TAG, "cancelAllMotions()");
        final Recorder recorder = mRecorder;
        final int result = _cancelAllMotions(recorder, robot);
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
        for (int i = 0; i < frameBean.getMotorBeans().size(); i++) {
            MotorBean motorBean = frameBean.getMotorBeans().get(i);
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

    private int _queryMotion(final Recorder recorder, final int[] frameIndex, final Robot robot) {
        frameIndex[0] = recorder.getFrameIndex();
        return Constants.RESULT.SUC;
    }

    private int _prepareMotion(final Recorder recorder, final MotionBean motionBean, final Robot robot) {
        final int result;
        if (motionBean.getName() == null) {
            result = Constants.RESULT.MOTION_NAME_NULL;
        } else {
            recorder.putMotionBean(motionBean);
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
        for (int i = 0; i < frameBean.getMotorBeans().size(); i++) {
            MotorBean motorBean = frameBean.getMotorBeans().get(i);
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

    private void __execMotion(final Recorder recorder, final MotionBean motionBean, final Robot robot, final boolean isLast) {
        final List<FrameBean> frameBeans = motionBean.getFrameBeans();
        final int size = frameBeans.size();
        Log.d(TAG, "__execMotion() size: " + size + ", isCancelled: " + recorder.isCancelled());
        final Semaphore semaphore = new Semaphore(0);
        for (int index = 0; index < size && !recorder.isCancelled(); ++index) {
            // set frame index
            recorder.setFrameIndex(index);
            __execFrame(semaphore, frameBeans.get(index), robot, isLast && index == size - 1, index);
            __waitFrameStop(semaphore);
        }
    }

    private int _execMotion(final Recorder recorder, final String motionName, final Robot robot) {
        final int result;
        final MotionBean motionBean = recorder.getMotionBean(motionName);
        if (motionBean == null) {
            result = Constants.RESULT.MOTION_ABSENT;
            setRobotIdle(robot);
        } else {
            recorder.setIsCancelled(false);
            new Thread() {
                @Override
                public void run() {
                    // set motion index 0
                    recorder.setMotionIndex(0);
                    __execMotion(recorder, motionBean, robot, true);
                    // set frame index -1
                    recorder.setFrameIndex(-1);
                    // set motion index -1
                    recorder.setMotionIndex(-1);
                }
            }.start();
            result = Constants.RESULT.SUC;
        }
        return result;
    }

    private int _queryMotions(Recorder recorder, int[] motionIndex, Robot robot) {
        motionIndex[0] = recorder.getMotionIndex();
        return Constants.RESULT.SUC;
    }


    private int _prepareMotions(Recorder recorder, List<MotionBean> motionBeanList, Robot robot) {
        int result = Constants.RESULT.SUC;
        for (MotionBean motionBean : motionBeanList) {
            if (motionBean.getName() == null) {
                result = Constants.RESULT.MOTION_NAME_NULL;
                break;
            }
        }
        if (result == Constants.RESULT.SUC) {
            for (MotionBean motionBean : motionBeanList) {
                recorder.putMotionBean(motionBean);
            }
        }
        setRobotIdle(robot);
        return result;
    }

    private int _execMotions(final Recorder recorder, final List<String> motionNameList,final Robot robot) {
        int result = Constants.RESULT.SUC;
        for (String motionName : motionNameList) {
            if (recorder.getMotionBean(motionName) == null) {
                result = Constants.RESULT.MOTION_ABSENT;
                break;
            }
        }
        if (result == Constants.RESULT.SUC) {
            recorder.setIsCancelled(false);
            new Thread() {
                @Override
                public void run() {
                    for (int motionIndex = 0; motionIndex < motionNameList.size(); motionIndex++) {
                        final String motionName = motionNameList.get(motionIndex);
                        final MotionBean motionBean = recorder.getMotionBean(motionName);
                        // set motion index
                        recorder.setMotionIndex(motionIndex);
                        __execMotion(recorder, motionBean, robot, motionIndex == motionNameList.size() - 1);
                    }
                    // set frame index -1
                    recorder.setFrameIndex(-1);
                    // set motion index -1
                    recorder.setMotionIndex(-1);
                }
            }.start();
            result = Constants.RESULT.SUC;
        } else {
            setRobotIdle(robot);
        }

        return result;
    }

    private int _cancelAllMotions(final Recorder recorder, final Robot robot) {
        recorder.setIsCancelled(true);
        return _cancelAllMotors(robot);
    }

}
