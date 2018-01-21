package com.afunx.service.interfaces;

import com.afunx.data.bean.ActionBean;

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
     * get running action index(-1 means not started yet)
     *
     * @return running action index(-1 means not started yet)
     */
    int getActionIndex();

    /**
     * set running action index(-1 means complete)
     *
     * @param actionIndex action index(-1 means complete)
     */
    void setActionIndex(int actionIndex);

    /**
     * put action bean into actionBeanMap
     *
     * @param actionBean action bean
     */
    void putActionBean(ActionBean actionBean);

    /**
     * get action bean from actionBeanMap
     *
     * @param actionName action bean name
     * @return
     */
    ActionBean getActionBean(String actionName);

    /**
     * set whether current action is cancelled
     *
     * @param isCancelled whether current action is cancelled
     */
    void setIsCancelled(boolean isCancelled);

    /**
     * check whether current action is cancelled
     *
     * @return whether current action is cancelled
     */
    boolean isCancelled();
}