package com.afunx.data.bean;

/**
 * Created by afunx on 23/12/2017.
 */

public class RequestBean {

    private long id;

    private Object body;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" + "\"id\"" + ":" + id +
                ", " + "\"body\"" + ":" + body
                + "}";
    }

    public static void main(String args[]) {
        // test RequestBean toString() method
        RequestBean requestBean = new RequestBean();
        requestBean.setId(1);
        System.out.println(requestBean);
        MotorBean motorBean = new MotorBean();
        motorBean.setId(1);
        motorBean.setDeg(100);
        requestBean.setBody(motorBean);
        System.out.println(requestBean);
    }
}