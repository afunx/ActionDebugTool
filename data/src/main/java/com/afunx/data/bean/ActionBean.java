package com.afunx.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 21/12/2017.
 */

public class ActionBean implements Cloneable, Serializable {
    /**
     * action name
     */
    private String name;
    /**
     * frame bean list
     */
    private List<FrameBean> frameBeans = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FrameBean> getFrameBeanList() {
        return frameBeans;
    }

    public void setFrameBeanList(List<FrameBean> frameBeans) {
        this.frameBeans = frameBeans;
    }

    @Override
    public String toString() {
        return "{" + "\"name\"" + ":" + "\"" + name + "\"" +
                ", " + "\"frameBeans\"" + ":" + frameBeans +
                "}";
    }

    @Override
    public ActionBean clone() {
        try {
            ActionBean clone = (ActionBean) super.clone();
            clone.frameBeans = new ArrayList<>();
            for (FrameBean frameBean : this.frameBeans) {
                clone.frameBeans.add(frameBean.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String args[]) {
        // test ActionBean toString() method
        MotorBean motorBean1 = new MotorBean();
        motorBean1.setId(1);
        motorBean1.setDeg(10);
        MotorBean motorBean2 = new MotorBean();
        motorBean2.setId(2);
        motorBean2.setDeg(20);
        FrameBean frameBean1 = new FrameBean();
        frameBean1.setIndex(1);
        frameBean1.setName("frame1");
        frameBean1.setTime(1000);
        frameBean1.getMotorBeanList().add(motorBean1);
        frameBean1.getMotorBeanList().add(motorBean2);
        FrameBean frameBean2 = new FrameBean();
        frameBean2.setIndex(2);
        frameBean2.setName("frame2");
        frameBean2.setTime(2000);
        frameBean2.getMotorBeanList().add(motorBean2);
        frameBean2.getMotorBeanList().add(motorBean1);
        ActionBean actionBean = new ActionBean();
        actionBean.setName("action");
        actionBean.getFrameBeanList().add(frameBean1);
        actionBean.getFrameBeanList().add(frameBean2);
        System.out.println(actionBean);
        // test ActionBean clone()
        ActionBean clone = actionBean.clone();
        clone.getFrameBeanList().get(0).setName("frame111111");
        clone.getFrameBeanList().get(0).getMotorBeanList().get(0).setId(11);
        System.out.println(actionBean);
        System.out.println(clone);
    }
}
