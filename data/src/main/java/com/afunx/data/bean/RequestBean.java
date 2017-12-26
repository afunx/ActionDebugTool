package com.afunx.data.bean;

/**
 * Created by afunx on 23/12/2017.
 */

public class RequestBean<T> {

    private long id;

    private int timeout;

    private T body;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" + "\"id\"" + ":" + id +
                ", " + "\"timeout\"" + ":" + timeout +
                ", " + "\"body\"" + ":" + body +
                "}";
    }

    public static void main(String args[]) {
        // test RequestBean toString() method
        RequestBean<MotorBean> requestBean = new RequestBean<>();
        requestBean.setId(1);
        requestBean.setTimeout(1000);
        System.out.println(requestBean);
        MotorBean motorBean = new MotorBean();
        motorBean.setId(1);
        motorBean.setDeg(100);
        requestBean.setBody(motorBean);
        System.out.println(requestBean);
    }
}