package com.afunx.service.interfaces;

import com.afunx.data.bean.MotionBean;

/**
 * Created by afunx on 27/12/2017.
 */

public interface Recorder {

    /**
     * get running frame index(-1 means not started yet)
     *
     * @return running frame index(-1 means not started yet)
     */
    int getFrameIndex();

    /**
     * set running frame index(-1 means complete)
     *
     * @param frameIndex running frame index
     */
    void setFrameIndex(int frameIndex);

    /**
     * get running motion index(-1 means not started yet)
     *
     * @return running motion index(-1 means not started yet)
     */
    int getMotionIndex();

    /**
     * set running motion index(-1 means complete)
     *
     * @param motionIndex motion index(-1 means complete)
     */
    void setMotionIndex(int motionIndex);

    /**
     * put motion bean into motionBeanMap
     *
     * @param motionBean motion bean
     */
    void putMotionBean(MotionBean motionBean);

    /**
     * get motion bean from motionBeanMap
     *
     * @param motionName motion bean name
     * @return
     */
    MotionBean getMotionBean(String motionName);

    /**
     * set whether current motion is cancelled
     *
     * @param isCancelled whether current motion is cancelled
     */
    void setIsCancelled(boolean isCancelled);

    /**
     * check whether current motion is cancelled
     *
     * @return whether current motion is cancelled
     */
    boolean isCancelled();
}