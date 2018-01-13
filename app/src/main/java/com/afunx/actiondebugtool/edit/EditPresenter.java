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
    }

    @Override
    public void copySelectedFrame() {
        if (DEBUG) {
            Log.d(TAG, "copySelectedFrame()");
        }
    }

    @Override
    public void insertFrameAfterSelected() {
        if (DEBUG) {
            Log.d(TAG, "insertFrameAfterSelected()");
        }
    }
}