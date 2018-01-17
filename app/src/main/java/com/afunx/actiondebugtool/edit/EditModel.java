package com.afunx.actiondebugtool.edit;

import android.content.Context;

import com.afunx.actiondebugtool.common.Constants;
import com.afunx.actiondebugtool.data.FrameData;

import java.util.List;

/**
 * Created by afunx on 12/01/2018.
 */

class EditModel {

    private static final int sMotorsCount = Constants.MOTORS_COUNT;

    private static final int[] sDegMin = Constants.MOTORS_DEG_MIN;

    private static final int[] sDegMax = Constants.MOTORS_DEG_MAX;

    private static final int sRuntimeMin = Constants.RUNTIME_MIN;

    private static final int sRuntimeMax = Constants.RUNTIME_MAX;

    private final Context appContext;
    private final List<FrameData> mFrameDataList;

    EditModel(Context context, List<FrameData> frameDataList) {
        appContext = context.getApplicationContext();
        mFrameDataList = frameDataList;
    }

    int getsMotorsCount() {
        return sMotorsCount;
    }

    int getMotorDegMin(int motorId) {
        return sDegMin[motorId - 1];
    }

    int getMotorDegMax(int motorId) {
        return sDegMax[motorId - 1];
    }

    int getRuntimeMin() {
        return sRuntimeMin;
    }

    int getRuntimeMax() {
        return sRuntimeMax;
    }

    List<FrameData> getFrameDataList() {
        return mFrameDataList;
    }

}
