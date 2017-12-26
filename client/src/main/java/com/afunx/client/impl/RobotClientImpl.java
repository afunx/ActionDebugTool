package com.afunx.client.impl;

import com.afunx.client.interfaces.ClientProtocol;
import com.afunx.client.interfaces.RobotClient;
import com.afunx.data.bean.MotionBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.bean.RequestBean;
import com.afunx.data.bean.ResponseBean;
import com.afunx.data.constants.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afunx on 26/12/2017.
 */

public class RobotClientImpl implements RobotClient {

    private String baseUrl = "http://127.0.0.1";

    private final int basePort = 18266;
    private static final AtomicLong sIdGenerator = new AtomicLong(0);

    @Override
    public void setIp4(byte[] ipAddr4) {
        if (ipAddr4 == null || ipAddr4.length != 4) {
            return;
        }
        baseUrl = String.format(Locale.US, "http://%d.%d.%d.%d", ipAddr4[0] & 0xff, ipAddr4[1] & 0xff, ipAddr4[2] & 0xff, ipAddr4[3] & 0xff);
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl + ":" + basePort)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ClientProtocol createClientProtocol() {
        Retrofit retrofit = createRetrofit();
        return retrofit.create(ClientProtocol.class);
    }

    private static long genRequestId() {
        return sIdGenerator.getAndAdd(1);
    }

    @Override
    public boolean isRobotIdle() {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.queryBusy(requestBean);
        try {
            Response<ResponseBean> response = responseBeanCall.execute();
            return response.body().getResult() == Constants.ROBOT_BUSY_STATE.IDLE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int queryMotors(List<MotorBean> motorBeanList) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean<List<MotorBean>>> responseBeanCall = clientProtocol.queryMotors(requestBean);
        try {
            Response<ResponseBean<List<MotorBean>>> response = responseBeanCall.execute();
            motorBeanList.addAll(response.body().getBody());
            return  response.body().getResult();
        } catch (IOException e) {
            e.printStackTrace();
            return Constants.RESULT.CLIENT_IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.RESULT.CLIENT_EXCEPTION;
        }
    }

    @Override
    public int execMotors(List<MotorBean> motorBeanList) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<List<MotorBean>> requestBean = new RequestBean<>();
        requestBean.setBody(motorBeanList);
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.execMotors(requestBean);
        try {
            Response<ResponseBean> response = responseBeanCall.execute();
            return response.body().getResult();
        } catch (IOException e) {
            e.printStackTrace();
            return Constants.RESULT.CLIENT_IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.RESULT.CLIENT_EXCEPTION;
        }
    }

    @Override
    public int cancelAllMotors() {
        return 0;
    }

    @Override
    public int execEnterReadmode() {
        return 0;
    }

    @Override
    public int execExitReadmode() {
        return 0;
    }

    @Override
    public int queryMotion(int[] frameIndex) {
        return 0;
    }

    @Override
    public int prepareMotion(MotionBean motionBean) {
        return 0;
    }

    @Override
    public int execMotion(String motionName) {
        return 0;
    }

    @Override
    public int queryMotions(int[] motionIndex) {
        return 0;
    }

    @Override
    public int prepareMotionList(List<MotionBean> motionBeanList) {
        return 0;
    }

    @Override
    public int cancelAllMotions() {
        return 0;
    }
}
