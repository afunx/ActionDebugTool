package com.afunx.data.bean;

/**
 * Created by afunx on 23/12/2017.
 */

public class ResponseBean {

    private long id;

    private Object body;

    private int result;

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
