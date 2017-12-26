package com.afunx.client.interfaces;

import com.afunx.data.bean.MotionBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.bean.RequestBean;
import com.afunx.data.bean.ResponseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Call<ResponseBean> execMotors(@Body RequestBean body);

    @POST("/cancel/motors")
    Call<ResponseBean> cancelAllMotors(@Body RequestBean body);

    @POST("/exec/readmode/enter")
    Call<ResponseBean> execEnterReadmode(@Body RequestBean body);

    @POST("/exec/readmode/exit")
    Call<ResponseBean> execExitReadmode(@Body RequestBean body);

    @POST("/query/motion/index")
    Call<ResponseBean<Integer>> queryMotion(@Body RequestBean<Integer> body);

    @POST("/prepare/motion")
    Call<ResponseBean> prepareMotion(@Body RequestBean<MotionBean> body);

    @POST("/exec/motion")
    Call<ResponseBean> execMotion(@Body RequestBean<String> body);

    @POST("/query/motions/index")
    Call<ResponseBean> queryMotions(@Body RequestBean<Integer> body);

    @POST("/prepare/motions")
    Call<ResponseBean> prepareMotionList(@Body RequestBean<List<MotionBean>> body);

    @POST("/exec/motions")
    Call<ResponseBean> execMotionList(@Body RequestBean<List<String>> body);

    @POST("/cancel/motions")
    Call<ResponseBean> cancelAllMotions(@Body RequestBean body);

}
