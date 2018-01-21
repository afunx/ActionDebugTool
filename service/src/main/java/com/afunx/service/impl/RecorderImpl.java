package com.afunx.service.impl;

import android.util.Log;

import com.afunx.data.bean.ActionBean;
import com.afunx.service.interfaces.Recorder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by afunx on 27/12/2017.
 */

public class RecorderImpl implements Recorder {

    private static final String TAG = "RecorderImpl";

    private volatile int mFrameIndex = -1;

    private volatile int mActionIndex = -1;

    private volatile boolean mIsCancelled = false;

     Map<String, ActionBean> mActionBeanMap = new HashMap<>();

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
    public int getActionIndex() {
        return mActionIndex;
    }

    @Override
    public void setActionIndex(int actionIndex) {
        Log.i(TAG, "setActionIndex() actionIndex: " + actionIndex);
        mActionIndex = actionIndex;
    }

    @Override
    public void putActionBean(ActionBean actionBean) {
        mActionBeanMap.put(actionBean.getName(), actionBean);
    }

    @Override
    public ActionBean getActionBean(String actionName) {
        return mActionBeanMap.get(actionName);
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
