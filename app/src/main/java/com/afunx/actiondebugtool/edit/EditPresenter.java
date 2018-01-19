package com.afunx.actiondebugtool.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afunx.actiondebugtool.R;
import com.afunx.actiondebugtool.common.Constants;
import com.afunx.actiondebugtool.data.FrameData;
import com.afunx.client.impl.RobotClientImpl;
import com.afunx.client.interfaces.RobotClient;
import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotionBean;
import com.afunx.data.bean.MotorBean;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 12/01/2018.
 */

public class EditPresenter implements EditContract.Presenter {

    private static final boolean DEBUG = true;

    private static final String TAG = "EditPresenter";

    private static final int sMotorsCount = Constants.MOTORS_COUNT;

    private static final int RESULT_SUC = com.afunx.data.constants.Constants.RESULT.SUC;

    private static final int RESULT_FAIL = com.afunx.data.constants.Constants.RESULT.FAIL;

    private static final int RESULT_NETWORK_TIMEOUT = com.afunx.data.constants.Constants.RESULT.NETWORK_TIMEOUT;

    private static final int RESULT_ROBOT_BUSY = com.afunx.data.constants.Constants.RESULT.ROBOT_BUSY;

    private static final int RESULT_ROBOT_TIMEOUT = com.afunx.data.constants.Constants.RESULT.ROBOT_TIMEOUT;

    private static final int RESULT_MOTION_ABSENT = com.afunx.data.constants.Constants.RESULT.MOTION_ABSENT;

    private static final int RESULT_MOTION_NAME_NULL = com.afunx.data.constants.Constants.RESULT.MOTION_NAME_NULL;

    private static final int RESULT_CLIENT_IOEXCEPTION = com.afunx.data.constants.Constants.RESULT.CLIENT_IOEXCEPTION;

    private static final int RESULT_CLIENT_EXCEPTION = com.afunx.data.constants.Constants.RESULT.CLIENT_EXCEPTION;

    private final EditModel mEditModel;

    private final EditContract.View mEditView;

    private final RobotClient mRobotClient = new RobotClientImpl();

    EditPresenter(@NonNull Context appContext, @NonNull EditContract.View editView, @NonNull List<FrameData> frameDataList) {
        mEditModel = EditModelFactory.provideEditModel(appContext, frameDataList);
        mEditView = editView;

        editView.setPresenter(this);
    }

    private List<FrameData> getFrameDataList() {
        return mEditModel.getFrameDataList();
    }

    @Override
    public void setRobotIpAddr(String ipAddr) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddr);
            mRobotClient.setIp4(inetAddress.getAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            mEditView.showToast(R.string.ip_addr_invalid);
        }
    }

    @Override
    public int getFrameRuntimeMin() {
        return mEditModel.getRuntimeMin();
    }

    @Override
    public int getFrameRuntimeMax() {
        return mEditModel.getRuntimeMax();
    }

    @Override
    public void insertFrameAfterSelected() {
        if (DEBUG) {
            Log.d(TAG, "insertFrameAfterSelected()");
        }
        int frameIndex = mEditView.getSelectedFrameIndex();
        // if frameIndex, it will insert first frame
        mEditView.insertFrame(frameIndex + 1);
        // set inserted frame as the selected frame index
        mEditView.setSelectedFrameIndex(frameIndex + 1);
    }

    @Override
    public void setSelectedFrameIndex(int frameIndex) {
        if (DEBUG) {
            Log.d(TAG, "setSelectedFrameIndex() frameIndex: " + frameIndex);
        }
        // set runtime min max
        int runtimeMax = mEditModel.getRuntimeMax();
        mEditView.setFrameRuntimeMax(runtimeMax);
        int runtimeMin = mEditModel.getRuntimeMin();
        mEditView.setFrameRuntimeMin(runtimeMin);

        // set runtime value
        List<FrameData> frameDataList = getFrameDataList();
        FrameData frameData = frameDataList.get(frameIndex);
        int time = frameData.getFrameBean().getTime();
        mEditView.setFrameRuntime(time);

        // update selected motor's deg
        int motorId = mEditView.getSelectedMotorId();
        if (motorId != -1) {
            setSelectedMotor(motorId);
        }
    }

    @Override
    public void setSelectedMotor(int motorId) {
        if (DEBUG) {
            Log.d(TAG, "setSelectedMotor() motorId: " + motorId);
        }

        int selectedFrameIndex = mEditView.getSelectedFrameIndex();
        if (selectedFrameIndex != -1) {
            // set motor degree min max
            mEditView.setSelectedMotorId(motorId);
            int motorDegMax = mEditModel.getMotorDegMax(motorId);
            mEditView.setMotorDegMax(motorDegMax);
            int motorDegMin = mEditModel.getMotorDegMin(motorId);
            mEditView.setMotorDegMin(motorDegMin);

            // set motor degree
            List<FrameData> frameDataList = getFrameDataList();
            List<MotorBean> motorBeanList = frameDataList.get(selectedFrameIndex).getFrameBean().getMotorBeans();
            MotorBean motorBean = getMotorBean(motorBeanList, motorId);

            // update motor degree
            mEditView.setMotorDeg(motorBean.getDeg());
            if (DEBUG) {
                Log.d(TAG, "setSelectedMotor() motorId: " + motorId + ", selectedFrameIndex: " + selectedFrameIndex + ", motorDeg: " + motorBean.getDeg());
            }
        } else {
            if (DEBUG) {
                Log.d(TAG, "setSelectedMotor() selectedFrameIndex = -1");
            }
        }
    }

    private MotorBean getMotorBean(List<MotorBean> motorBeanList, int motorId) {
        for (MotorBean motorBean : motorBeanList) {
            if (motorBean.getId() == motorId) {
                return motorBean;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public void setSelectedMotorDegree(int degree) {
        int selectedFrameIndex = mEditView.getSelectedFrameIndex();
        if (selectedFrameIndex != -1) {
            int selectedMotorId = mEditView.getSelectedMotorId();
            if (selectedMotorId == -1) {
                if (DEBUG) {
                    Log.d(TAG, "setSelectedMotorDegree() selectedMotorId = -1");
                }
                return;
            }
            if (DEBUG) {
                Log.d(TAG, "setSelectedMotorDegree() degree: " + degree);
            }
            List<FrameData> frameDataList = getFrameDataList();
            List<MotorBean> motorBeanList = frameDataList.get(selectedFrameIndex).getFrameBean().getMotorBeans();
            MotorBean motorBean = getMotorBean(motorBeanList, selectedMotorId);
            motorBean.setDeg(degree);
        } else {
            if (DEBUG) {
                Log.d(TAG, "setSelectedMotorDegree() selectedFrameIndex = -1");
            }
        }
    }

    @Override
    public void setSelectedFrameRuntime(int runtime) {
        int selectedFrameIndex = mEditView.getSelectedFrameIndex();
        if (selectedFrameIndex != -1) {
            if (DEBUG) {
                Log.d(TAG, "setSelectedFrameRuntime() runtime: " + runtime);
            }
            List<FrameData> frameDataList = getFrameDataList();
            frameDataList.get(selectedFrameIndex).getFrameBean().setTime(runtime);
            mEditView.updateFrame(selectedFrameIndex);
        } else {
            if (DEBUG) {
                Log.d(TAG, "setSelectedFrameRuntime() selectedFrameIndex = -1");
            }
        }
    }

    private void showResult(int result,int sucResId) {
        switch (result) {
            case RESULT_SUC:
                mEditView.showToast(sucResId);
                break;
            case RESULT_FAIL:
                mEditView.showToast(R.string.robot_execute_err_1_fail);
                break;
            case RESULT_NETWORK_TIMEOUT:
                mEditView.showToast(R.string.robot_execute_err_2_fail);
                break;
            case RESULT_ROBOT_BUSY:
                mEditView.showToast(R.string.robot_execute_err_3_fail);
                break;
            case RESULT_ROBOT_TIMEOUT:
                mEditView.showToast(R.string.robot_execute_err_4_fail);
                break;
            case RESULT_MOTION_ABSENT:
                mEditView.showToast(R.string.robot_execute_err_5_fail);
                break;
            case RESULT_MOTION_NAME_NULL:
                mEditView.showToast(R.string.robot_execute_err_6_fail);
                break;
            case RESULT_CLIENT_IOEXCEPTION:
                mEditView.showToast(R.string.robot_execute_err_7_fail);
                break;
            case RESULT_CLIENT_EXCEPTION:
                mEditView.showToast(R.string.robot_execute_err_8_fail);
                break;
        }
    }

    @Override
    public void enterReadMode() {
        if (DEBUG) {
            Log.d(TAG, "enterReadMode()");
        }
        new Thread() {
            @Override
            public void run() {
                int ret = mRobotClient.execEnterReadmode();
                if (DEBUG) {
                    Log.d(TAG, "enterReadMode() ret: " + ret);
                }
                showResult(ret, R.string.enter_read_mode_suc);
            }
        }.start();
    }

    private void updateMotorBeanList(List<MotorBean> destMotorBeanList, List<MotorBean> srcMotorBeanList) {
        for (MotorBean destMotorBean : destMotorBeanList) {
            for (MotorBean srcMotorBean : srcMotorBeanList) {
                if (srcMotorBean.getId() == destMotorBean.getId()) {
                    destMotorBean.setDeg(srcMotorBean.getDeg());
                    break;
                }
            }
        }
    }

    @Override
    public void readMotors() {
        if (DEBUG) {
            Log.d(TAG, "readMotors()");
        }

        final int selectedFrameIndex = mEditView.getSelectedFrameIndex();
        if (selectedFrameIndex == -1) {
            mEditView.showToast(R.string.please_select_frame_first);
        } else {
            new Thread() {
                @Override
                public void run() {
                    // query motor bean list
                    List<MotorBean> motorBeanList = new ArrayList<>();
                    int ret = mRobotClient.queryMotors(motorBeanList);
                    // show result
                    showResult(ret, R.string.read_motors_suc);
                    // update model UI if suc
                    if (ret == RESULT_SUC) {
                        // update model
                        FrameBean frameBean = mEditModel.getFrameDataList().get(selectedFrameIndex).getFrameBean();
                        updateMotorBeanList(frameBean.getMotorBeans(), motorBeanList);
                        if (DEBUG) {
                            Log.i(TAG, "readMotors() motorBeanList: " + motorBeanList);
                        }
                        // update UI
                        setSelectedFrameIndex(selectedFrameIndex);
                    }
                }
            }.start();
        }
    }

    @Override
    public void playSelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "playSelectedFrame()");
        }

        final int selectedFrameIndex = mEditView.getSelectedFrameIndex();
        if (selectedFrameIndex == -1) {
            throw new IllegalStateException("selectedFrameIndex is -1");
        }
        new Thread() {
            @Override
            public void run() {
                // play selected frame bean
                FrameBean frameBean = mEditModel.getFrameDataList().get(selectedFrameIndex).getFrameBean();
                int ret = mRobotClient.execMotors(frameBean);
                showResult(ret, R.string.play_frame_suc);
            }
        }.start();
    }

    @Override
    public void playMotionFromSelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "playMotionFromSelectedFrame()");
        }

        int _selectedFrameIndex = mEditView.getSelectedFrameIndex();
        // if no frame is selected, treat it as first frame is selected
        final int selectedFrameIndex = _selectedFrameIndex != -1 ? _selectedFrameIndex : 0;
        new Thread() {
            @Override
            public void run() {
                // create temp motion
                final String tempMotionName = "__temp__motion__";
                List<FrameData> frameDataList = mEditModel.getFrameDataList();
                MotionBean motionBean = new MotionBean();
                motionBean.setName(tempMotionName);
                for (int i = selectedFrameIndex; i < frameDataList.size(); i++) {
                    motionBean.getFrameBeans().add(frameDataList.get(i).getFrameBean());
                }
                // prepare motion
                int ret = mRobotClient.prepareMotion(motionBean);
                if (ret != RESULT_SUC) {
                    showResult(ret, -1);
                    return;
                }
                // execute motion
                ret = mRobotClient.execMotion(tempMotionName);
                if (ret != RESULT_SUC) {
                    showResult(ret, -1);
                    return;
                }
                // wait motion finished
                int lastFrameIndex;
                int currentFrameIndex = -1;
                int[] frameIndex = new int[1];
                while (ret == RESULT_SUC && frameIndex[0] >= 0) {
                    lastFrameIndex = currentFrameIndex;
                    ret = mRobotClient.queryMotion(frameIndex);
                    if (ret != 0) {
                        Log.e(TAG, "playMotionFromSelectedFrame() ret: " + ret);
                    }
                    currentFrameIndex = frameIndex[0];
                    if (currentFrameIndex != lastFrameIndex && currentFrameIndex >= 0) {
                        if (DEBUG) {
                            Log.i(TAG, "playMotionFromSelectedFrame() lastFrameIndex: " + lastFrameIndex);
                        }
                        mEditView.setSelectedFrameIndex(selectedFrameIndex + currentFrameIndex);
                    } else {
                        // just sleep 100ms to avoid too many connections
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (ret != RESULT_SUC) {
                    showResult(ret, -1);
                    return;
                }
                mEditView.setSelectedFrameIndex(selectedFrameIndex);
            }
        }.start();
    }

    @Override
    public void deleteSelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "deleteSelectedFrame()");
        }
        int frameIndex = mEditView.getSelectedFrameIndex();
        if (frameIndex != -1) {
            mEditView.deleteFrame(frameIndex);
            // set selected
            if (mEditView.getFrameCount() > frameIndex) {
                // first try to set selected after the deleted one
                mEditView.setSelectedFrameIndex(frameIndex);
            } else if (mEditView.getFrameCount() > 0) {
                // second try to set selected before the deleted one
                mEditView.setSelectedFrameIndex(frameIndex - 1);
            } else {
                // no frame available to be set selected
                if (DEBUG) {
                    Log.d(TAG, "deleteSelectedFrame() no more frame");
                }
            }
        }
    }

    @Override
    public void copySelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "copySelectedFrame()");
        }
        int frameIndex = mEditView.getSelectedFrameIndex();
        if (frameIndex != -1) {
            int copiedIndex = mEditView.getCopiedFrameIndex();
            if (copiedIndex == frameIndex) {
                // clear copy state if it is copied already
                mEditView.clearCopy();
            } else {
                // add copy state
                mEditView.copyFrame(frameIndex);
            }
        }
    }

    @Override
    public void pasteAfterSelected() {
        if (DEBUG) {
            Log.d(TAG, "pasteAfterSelected()");
        }
        int copiedIndex = mEditView.getCopiedFrameIndex();
        if (copiedIndex != -1) {
            int frameIndex = mEditView.getSelectedFrameIndex();
            if (frameIndex != -1) {
                mEditView.pasteAfterSelected();
                // set inserted frame as the selected frame index
                mEditView.setSelectedFrameIndex(frameIndex + 1);
            } else {
                if (DEBUG) {
                    Log.d(TAG, "pasteAfterSelected() frameIndex = -1");
                }
            }
        } else {
            if (DEBUG) {
                Log.d(TAG, "pasteAfterSelected() copiedIndex = -1");
            }
        }

    }
}