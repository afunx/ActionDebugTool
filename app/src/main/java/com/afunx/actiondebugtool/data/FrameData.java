package com.afunx.actiondebugtool.data;

import com.afunx.data.bean.FrameBean;

/**
 * Created by afunx on 13/01/2018.
 */

public class FrameData implements Cloneable {

    private FrameBean frameBean;

    private boolean isSelected;

    private boolean isCopied;

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