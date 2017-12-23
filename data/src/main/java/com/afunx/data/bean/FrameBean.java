package com.afunx.data.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 21/12/2017.
 */

public class FrameBean implements Cloneable {
    /**
     * frame index
     */
    private int index;
    /**
     * frame name
     */
    private String name;
    /**
     * run time
     */
    private int time;
    /**
     * motor bean list
     */
    private List<MotorBean> motorBeans = new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<MotorBean> getMotorBeans() {
        return motorBeans;
    }

    public void setMotorBeans(List<MotorBean> motorBeans) {
        this.motorBeans = motorBeans;
    }

    @Override
    public String toString() {
        return "{" + "\"index\"" + ":" + index +
                ", " + "\"time\"" + ":" + time +
                ", " + "\"name\"" + ":" + "\"" + name + "\"" +
                ", " + "\"motorBeans\"" + ":" + motorBeans
                + "}";
    }

    @Override
    public FrameBean clone() {
        try {
            FrameBean clone = (FrameBean) super.clone();
            clone.motorBeans = new ArrayList<>();
            for (MotorBean motorBean : this.motorBeans) {
                clone.motorBeans.add(motorBean.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String args[]) {
        // test FrameBean toString() method
        MotorBean motorBean1 = new MotorBean();
        motorBean1.setId(1);
        motorBean1.setDeg(10);
        MotorBean motorBean2 = new MotorBean();
        motorBean2.setId(2);
        motorBean2.setDeg(20);
        FrameBean frameBean = new FrameBean();
        frameBean.setIndex(1);
        frameBean.setName("frame");
        frameBean.setTime(1000);
        frameBean.getMotorBeans().add(motorBean1);
        frameBean.getMotorBeans().add(motorBean2);
        System.out.println(frameBean);
        // test FrameBean clone()
        FrameBean clone = frameBean.clone();
        clone.motorBeans.get(0).setId(3);
        System.out.println(frameBean);
        System.out.println(clone);
    }
}