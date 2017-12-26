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

    private static final int PORT = 18266;
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
                .baseUrl(baseUrl + ":" + PORT)
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
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.cancelAllMotors(requestBean);
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
    public int execEnterReadmode() {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.execEnterReadmode(requestBean);
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
    public int execExitReadmode() {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.execExitReadmode(requestBean);
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
    public int queryMotion(int[] frameIndex) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<Integer> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean<Integer>> responseBeanCall = clientProtocol.queryMotion(requestBean);
        try {
            Response<ResponseBean<Integer>> response = responseBeanCall.execute();
            frameIndex[0] = response.body().getBody();
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
    public int prepareMotion(MotionBean motionBean) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<MotionBean> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(motionBean);
        Call<ResponseBean> responseBeanCall = clientProtocol.prepareMotion(requestBean);
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
    public int execMotion(String motionName) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<String> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(motionName);
        Call<ResponseBean> responseBeanCall = clientProtocol.execMotion(requestBean);
        try {
            Response<ResponseBean> response = responseBeanCall.execute();
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
    public int queryMotions(int[] motionIndex) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<Integer> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean<Integer>> responseBeanCall = clientProtocol.queryMotions(requestBean);
        try {
            Response<ResponseBean<Integer>> response = responseBeanCall.execute();
            motionIndex[0] = response.body().getBody();
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
    public int prepareMotions(List<MotionBean> motionBeanList) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<List<MotionBean>> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(motionBeanList);
        Call<ResponseBean> responseBeanCall = clientProtocol.prepareMotions(requestBean);
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
    public int execMotions(List<String> motionNameList) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<List<String>> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(motionNameList);
        Call<ResponseBean> responseBeanCall = clientProtocol.execMotions(requestBean);
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
    public int cancelAllMotions() {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.cancelAllMotions(requestBean);
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
}
