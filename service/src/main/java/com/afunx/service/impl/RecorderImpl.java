package com.afunx.service.impl;

import android.util.Log;

import com.afunx.data.bean.MotionBean;
import com.afunx.service.interfaces.Recorder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by afunx on 27/12/2017.
 */

public class RecorderImpl implements Recorder {

    private static final String TAG = "RecorderImpl";

    private volatile int mFrameIndex = -1;

    private volatile int mMotionIndex = -1;

    private volatile boolean mIsCancelled = false;

     Map<String, MotionBean> mMotionBeanMap = new HashMap<>();

    @Override
    public int getFrameIndex() {
        return mFrameIndex;
    }

    @Override
    public void setFrameIndex(int frameIndex) {
        Log.i(TAG, "setFrameIndex() frameIndex: " + frameIndex);
        mFrameIndex = frameIndex;
    }

    @Override
    public int getMotionIndex() {
        return mMotionIndex;
    }

    @Override
    public void setMotionIndex(int motionIndex) {
        Log.i(TAG, "setMotionIndex() motionIndex: " + motionIndex);
        mMotionIndex = motionIndex;
    }

    @Override
    public void putMotionBean(MotionBean motionBean) {
        mMotionBeanMap.put(motionBean.getName(), motionBean);
    }

    @Override
    public MotionBean getMotionBean(String motionName) {
        return mMotionBeanMap.get(motionName);
    }

    @Override
    public void setIsCancelled(boolean isCancelled) {
        mIsCancelled = isCancelled;
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }
}
