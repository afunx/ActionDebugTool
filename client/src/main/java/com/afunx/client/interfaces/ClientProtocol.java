package com.afunx.client.interfaces;

import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.ActionBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.bean.RequestBean;
import com.afunx.data.bean.ResponseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by afunx on 26/12/2017.
 */

public interface ClientProtocol {

    @POST("/query/busy")
    Call<ResponseBean> queryBusy(@Body RequestBean body);

    @POST("/query/motors")
    Call<ResponseBean<List<MotorBean>>> queryMotors(@Body RequestBean body);

    @POST("/exec/motors")
    Call<ResponseBean> execMotors(@Body RequestBean<FrameBean> body);

    @POST("/cancel/motors")
    Call<ResponseBean> cancelAllMotors(@Body RequestBean body);

    @POST("/exec/readmode/enter")
    Call<ResponseBean> execEnterReadmode(@Body RequestBean body);

    @POST("/exec/readmode/exit")
    Call<ResponseBean> execExitReadmode(@Body RequestBean body);

    @POST("/query/action/index")
    Call<ResponseBean<Integer>> queryAction(@Body RequestBean<Integer> body);

    @POST("/prepare/action")
    Call<ResponseBean> prepareAction(@Body RequestBean<ActionBean> body);

    @POST("/exec/action")
    Call<ResponseBean> execAction(@Body RequestBean<String> body);

    @POST("/query/actions/index")
    Call<ResponseBean<Integer>> queryActions(@Body RequestBean<Integer> body);

    @POST("/prepare/actions")
    Call<ResponseBean> prepareActions(@Body RequestBean<List<ActionBean>> body);

    @POST("/exec/actions")
    Call<ResponseBean> execActions(@Body RequestBean<List<String>> body);

    @POST("/cancel/actions")
    Call<ResponseBean> cancelAllActions(@Body RequestBean body);

}
