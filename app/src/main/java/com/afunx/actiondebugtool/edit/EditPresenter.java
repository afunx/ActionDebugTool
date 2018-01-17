package com.afunx.actiondebugtool.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afunx.actiondebugtool.data.FrameData;
import com.afunx.data.bean.MotorBean;

import java.util.List;

/**
 * Created by afunx on 12/01/2018.
 */

public class EditPresenter implements EditContract.Presenter {

    private static final boolean DEBUG = true;

    private static final String TAG = "EditPresenter";

    private final EditModel mEditModel;

    private final EditContract.View mEditView;

    EditPresenter(@NonNull Context appContext, @NonNull EditContract.View editView, @NonNull List<FrameData> frameDataList) {
        mEditModel = EditModelFactory.provideEditModel(appContext, frameDataList);
        mEditView = editView;

        editView.setPresenter(this);
    }

    private List<FrameData> getFrameDataList() {
        return mEditModel.getFrameDataList();
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

    @Override
    public void enterReadMode() {
        if (DEBUG) {
            Log.d(TAG, "enterReadMode()");
        }
    }

    @Override
    public void readMotors() {
        if (DEBUG) {
            Log.d(TAG, "readMotors()");
        }
    }

    @Override
    public void playSelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "playSelectedFrame()");
        }
    }

    @Override
    public void playMotionFromSelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "playMotionFromSelectedFrame()");
        }
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