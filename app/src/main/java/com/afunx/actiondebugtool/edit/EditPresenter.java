package com.afunx.actiondebugtool.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by afunx on 12/01/2018.
 */

public class EditPresenter implements EditContract.Presenter {

    private static final boolean DEBUG = true;

    private static final String TAG = "EditPresenter";

    private final EditModel mEditModel;

    private final EditContract.View mEditView;

    EditPresenter(@NonNull Context appContext, @NonNull EditContract.View editView) {
        mEditModel = EditModelFactory.provideEditModel(appContext);
        mEditView = editView;

        editView.setPresenter(this);
    }

    @Override
    public void setSelectedMotorDegree(int degree) {
        if (DEBUG) {
            Log.d(TAG, "setSelectedMotorDegree() degree: " + degree);
        }
    }

    @Override
    public void setSelectedFrameRuntime(int runtime) {
        if (DEBUG) {
            Log.d(TAG, "setSelectedFrameRuntime() runtime: " + runtime);
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
            // TODO
            Log.i(TAG, "TODO delete frame in model");
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
                // TODO
                Log.i(TAG, "TODO paste frame in model");
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