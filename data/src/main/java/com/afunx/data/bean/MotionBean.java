package com.afunx.data.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 21/12/2017.
 */

public class MotionBean implements Cloneable {
    /**
     * motion name
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

    public List<FrameBean> getFrameBeans() {
        return frameBeans;
    }

    public void setFrameBeans(List<FrameBean> frameBeans) {
        this.frameBeans = frameBeans;
    }

    @Override
    public String toString() {
        return "{" + "\"name\"" + ":" + "\"" + name + "\"" +
                ", " + "\"frameBeans\"" + ":" + frameBeans +
                "}";
    }

    @Override
    public MotionBean clone() {
        try {
            MotionBean clone = (MotionBean) super.clone();
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
        // test MotionBean toString() method
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
        frameBean1.getMotorBeans().add(motorBean1);
        frameBean1.getMotorBeans().add(motorBean2);
        FrameBean frameBean2 = new FrameBean();
        frameBean2.setIndex(2);
        frameBean2.setName("frame2");
        frameBean2.setTime(2000);
        frameBean2.getMotorBeans().add(motorBean2);
        frameBean2.getMotorBeans().add(motorBean1);
        MotionBean motionBean = new MotionBean();
        motionBean.setName("motion");
        motionBean.getFrameBeans().add(frameBean1);
        motionBean.getFrameBeans().add(frameBean2);
        System.out.println(motionBean);
        // test MotionBean clone()
        MotionBean clone = motionBean.clone();
        clone.getFrameBeans().get(0).setName("frame111111");
        clone.getFrameBeans().get(0).getMotorBeans().get(0).setId(11);
        System.out.println(motionBean);
        System.out.println(clone);
    }
}
