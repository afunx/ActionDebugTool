package com.afunx.server.impl;

import com.afunx.data.bean.MotorBean;
import com.afunx.data.bean.RequestBean;
import com.afunx.data.bean.ResponseBean;
import com.afunx.data.constants.Constants;
import com.afunx.server.interfaces.Robot;
import com.afunx.server.interfaces.RobotAdapter;
import com.afunx.server.interfaces.RobotServer;
import com.afunx.server.log.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by afunx on 25/12/2017.
 */

public class RobotServerImpl extends NanoHTTPD implements RobotServer {

    private static final String TAG = "RobotServerImpl";

    private static final Gson gson;

    private static final Response RESPONSE_ROBOT_BUSY;
    private static final Response RESPONSE_ROBOT_TIMEOUT;
    private static final Response RESPONSE_MOTION_ABSENT;

    static {
        gson = new GsonBuilder().create();

        ResponseBean responseBean = new ResponseBean();

        responseBean.setResult(Constants.RESULT.ROBOT_BUSY);
        RESPONSE_ROBOT_BUSY = newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));

        responseBean.setResult(Constants.RESULT.ROBOT_TIMEOUT);
        RESPONSE_ROBOT_TIMEOUT = newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));

        responseBean.setResult(Constants.RESULT.MOTION_ABSENT);
        RESPONSE_MOTION_ABSENT = newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));
    }

    private final Robot robot;
    private RobotAdapter robotAdapter;


    public RobotServerImpl(int port) {
        super(port);
        robot = RobotImpl.get();
    }

    @Override
    public void setAdapter(RobotAdapter robotAdapter) {
        this.robotAdapter = robotAdapter;
    }

    @Override
    public Response serve(IHTTPSession session) {
        LogUtils.log(TAG, "serve() uri: " + session.getUri());

        if (!robot.isIdle()) {
            LogUtils.log(TAG, "serve() uri: " + session.getUri() + ", robot is busy");
            return RESPONSE_ROBOT_BUSY;
        }

        Response response = null;

        // query whether robot is busy
        if (session.getUri().equals("/query/busy") && session.getMethod().equals(Method.POST)) {
            response = serveQueryBusy(session, this.robot);
            return response != null ? response :
                    newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "URL: " + session.getUri() + " IS BAD REQUEST");
        }

        if (session.getUri().equals("/query/motors") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.QUERY);
            response = serveQueryMotors(session);
        } else if (session.getUri().equals("/exec/motors") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.EXECUTE);
            response = serveExecMotors(session);
        }

        return response != null ? response :
                newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_PLAINTEXT, "URL: " + session.getUri() + " IS BAD REQUEST");
    }

    private Response serveQueryBusy(IHTTPSession session, Robot robot) {
        RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            return null;
        }
        int result = robot.isIdle() ? Constants.ROBOT_BUSY_STATE.IDLE : Constants.ROBOT_BUSY_STATE.BUSY;
        long id = requestBean.getId();
        return assembleResponse(result, id);
    }

    // parse request(body is bean) from client generically
    private <T> RequestBean<T> parseRequestBean(IHTTPSession session) {
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            return gson.fromJson(files.get("postData"), new TypeToken<RequestBean<T>>() {
            }.getType());
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // parse request(body is bean list) from client generically
    private <T> RequestBean<List<T>> parseRequestBeanList(IHTTPSession session) {
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            return gson.fromJson(files.get("postData"), new TypeToken<RequestBean<List<T>>>() {
            }.getType());
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // assemble response by result, id
    private Response assembleResponse(int result, long id) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setResult(result);
        responseBean.setId(id);
        return newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));
    }

    // assemble response by result, id, body
    private <T> Response assembleResponse(int result, long id, T body) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setResult(result);
        responseBean.setId(id);
        responseBean.setBody(body);
        return newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));
    }

    /**
     * wait semaphore release in time
     *
     * @param timeout   timeout in milliseconds(if timeout <=0, wait semaphore forever)
     * @param semaphore semaphore
     * @return whether semaphore is released in time
     */
    private boolean waitTimeout(final int timeout, final Semaphore semaphore) {
        if (timeout > 0) {
            try {
                return semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                semaphore.acquire();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Response serveQueryMotors(IHTTPSession session) {
        LogUtils.log(TAG, "serveQueryMotors()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveQueryMotors() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // query motors async
        final int timeout = requestBean.getTimeout();
        final List<MotorBean> motorBeanList = new ArrayList<>();
        queryMotorsAsync(motorBeanList, result, semaphore, robot);
        // wait execute finished or timeout
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id, motorBeanList);
    }

    private Response serveExecMotors(IHTTPSession session) {
        LogUtils.log(TAG, "serveExecMotors()");
        final RequestBean<List<MotorBean>> requestBeanList = parseRequestBeanList(session);
        if (requestBeanList == null || requestBeanList.getBody() == null) {
            LogUtils.log(TAG, "serveExecMotors() requestBeanList is null or requestBeanList.getBody() is null");
            return null;
        }
        final long id = requestBeanList.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // execute motors async
        execMotorsAsync(requestBeanList.getBody(), result, semaphore, robot);
        // wait execute finished or timeout
        final int timeout = requestBeanList.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private void execMotorsAsync(final List<MotorBean> motorBeanList, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.execMotors(motorBeanList, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void queryMotorsAsync(final List<MotorBean> motorBeanList, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.queryMotors(motorBeanList, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }
}
