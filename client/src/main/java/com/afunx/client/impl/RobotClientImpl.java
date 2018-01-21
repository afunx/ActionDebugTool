package com.afunx.client.impl;

import com.afunx.client.interfaces.ClientProtocol;
import com.afunx.client.interfaces.RobotClient;
import com.afunx.data.bean.ActionBean;
import com.afunx.data.bean.FrameBean;
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
    public synchronized boolean isRobotIdle() {
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
    public synchronized int queryMotors(List<MotorBean> motorBeanList) {
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
    public synchronized int execMotors(FrameBean frameBean) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<FrameBean> requestBean = new RequestBean<>();
        requestBean.setBody(frameBean);
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
    public synchronized int cancelAllMotors() {
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
    public synchronized int execEnterReadmode() {
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
    public synchronized int execExitReadmode() {
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
    public synchronized int queryAction(int[] frameIndex) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<Integer> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean<Integer>> responseBeanCall = clientProtocol.queryAction(requestBean);
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
    public synchronized int prepareAction(ActionBean action) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<ActionBean> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(action);
        Call<ResponseBean> responseBeanCall = clientProtocol.prepareAction(requestBean);
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
    public synchronized int execAction(String actionName) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<String> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(actionName);
        Call<ResponseBean> responseBeanCall = clientProtocol.execAction(requestBean);
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
    public synchronized int queryActions(int[] actionIndex) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<Integer> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean<Integer>> responseBeanCall = clientProtocol.queryActions(requestBean);
        try {
            Response<ResponseBean<Integer>> response = responseBeanCall.execute();
            actionIndex[0] = response.body().getBody();
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
    public synchronized int prepareActions(List<ActionBean> actionBeanList) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<List<ActionBean>> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(actionBeanList);
        Call<ResponseBean> responseBeanCall = clientProtocol.prepareActions(requestBean);
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
    public synchronized int execActions(List<String> actionNameList) {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<List<String>> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        requestBean.setBody(actionNameList);
        Call<ResponseBean> responseBeanCall = clientProtocol.execActions(requestBean);
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
    public synchronized int cancelAllActions() {
        ClientProtocol clientProtocol = createClientProtocol();
        RequestBean<?> requestBean = new RequestBean<>();
        final long id = genRequestId();
        requestBean.setId(id);
        Call<ResponseBean> responseBeanCall = clientProtocol.cancelAllActions(requestBean);
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
