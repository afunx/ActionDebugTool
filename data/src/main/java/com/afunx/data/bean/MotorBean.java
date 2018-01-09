package com.afunx.data.bean;

import java.io.Serializable;

/**
 * Created by afunx on 20/12/2017.
 */

public class MotorBean implements Cloneable, Serializable {
    /**
     * motor id
     */
    private int id;
    /**
     * motor degree
     */
    private int deg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    @Override
    public String toString() {
        return "{" + "\"id\"" + ":" + id +
                ", " + "\"deg\"" + ":" + deg +
                "}";
    }

    @Override
    public MotorBean clone() {
        try {
            MotorBean clone = (MotorBean) super.clone();
            clone.id = this.id;
            clone.deg = this.deg;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String args[]) {
        // test MotorBean toString() method
        MotorBean motorBean = new MotorBean();
        motorBean.setId(1);
        motorBean.setDeg(10);
        System.out.println(motorBean);
        // test MotorBean clone()
        MotorBean clone = motorBean.clone();
        clone.setId(2);
        clone.setDeg(20);
        System.out.println(motorBean);
        System.out.println(clone);
    }
}