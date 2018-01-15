package com.afunx.actiondebugtool.data;

import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotorBean;

/**
 * Created by afunx on 13/01/2018.
 */

public class FrameData implements Cloneable {

    private static final int MOTORS_COUNT = 14;
    private static final int DEFAULT_FRAME_TIME = 1000;

    private FrameBean frameBean;

    private boolean isSelected;

    private boolean isCopied;

    /**
     * this constructor is used by add new frame
     */
    public FrameData() {
        FrameBean frameBean = new FrameBean();
        for (int i = 0; i < MOTORS_COUNT; i++) {
            MotorBean motorBean = new MotorBean();
            motorBean.setId(i + 1);
            motorBean.setDeg(0);
            frameBean.getMotorBeans().add(motorBean);
        }
        frameBean.setTime(DEFAULT_FRAME_TIME);
        this.frameBean = frameBean;
    }

    public FrameData(FrameBean frameBean) {
        this.frameBean = frameBean;
    }

    public FrameBean getFrameBean() {
        return this.frameBean;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCopied() {
        return isCopied;
    }

    public void setCopied(boolean copied) {
        isCopied = copied;
    }

    public FrameData clone() {
        try {
            FrameData clone = (FrameData) super.clone();
            clone.frameBean = this.frameBean.clone();
            clone.isCopied = false;
            clone.isSelected = false;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException();
        }
    }

}