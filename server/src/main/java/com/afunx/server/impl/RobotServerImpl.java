package com.afunx.server.impl;

import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.ActionBean;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
    private static final JsonParser jsonParser;

    private static final Response RESPONSE_ROBOT_BUSY;
    private static final Response RESPONSE_ROBOT_TIMEOUT;

    static {
        gson = new GsonBuilder().create();
        jsonParser = new JsonParser();

        ResponseBean responseBean = new ResponseBean();

        responseBean.setResult(Constants.RESULT.ROBOT_BUSY);
        RESPONSE_ROBOT_BUSY = newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));

        responseBean.setResult(Constants.RESULT.ROBOT_TIMEOUT);
        RESPONSE_ROBOT_TIMEOUT = newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));
    }

    private final Robot robot;
    private RobotAdapter robotAdapter;


    public RobotServerImpl(int port) {
        super(port);
        robot = RobotImpl.get();
    }

    @Override
    protected void finalize() throws Throwable {
        this.stop();
        super.finalize();
    }

    @Override
    public void setAdapter(RobotAdapter robotAdapter) {
        this.robotAdapter = robotAdapter;
    }

    @Override
    public Response serve(IHTTPSession session) {
        LogUtils.log(TAG, "serve() uri: " + session.getUri());

        // check robot is idle, only cancel could break in
        if (!robot.isIdle() && !session.getUri().contains("cancel") && !session.getUri().contains("query")) {
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
            response = serveQueryMotors(session);
        } else if (session.getUri().equals("/exec/motors") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.EXECUTE);
            response = serveExecMotors(session);
        } else if (session.getUri().equals("/cancel/motors") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.CANCEL);
            response = serveCancelMotors(session);
        } else if (session.getUri().equals("/exec/readmode/enter") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.EXECUTE);
            response = serveExecEnterReadmode(session);
        } else if (session.getUri().equals("/exec/readmode/exit") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.EXECUTE);
            response = serveExecExitReadmode(session);
        } else if (session.getUri().equals("/query/action/index") && session.getMethod().equals(Method.POST)) {
            response = serveQueryAction(session);
        } else if (session.getUri().equals("/prepare/action") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.PREPARE);
            response = servePrepareAction(session);
        } else if (session.getUri().equals("/exec/action") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.EXECUTE);
            response = serveExecAction(session);
        } else if (session.getUri().equals("/query/actions/index") && session.getMethod().equals(Method.POST)) {
            response = serveQueryActions(session);
        } else if (session.getUri().equals("/prepare/actions") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.PREPARE);
            response = servePrepareActions(session);
        } else if (session.getUri().equals("/exec/actions") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.EXECUTE);
            response = serveExecActions(session);
        } else if (session.getUri().equals("/cancel/actions") && session.getMethod().equals(Method.POST)) {
            robot.setState(Robot.STATE.CANCEL);
            response = serveCancelActions(session);
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
    @SuppressWarnings("unchecked")
    private <T> RequestBean<T> parseRequestBean(IHTTPSession session, Class<T>... classOfT) {
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            RequestBean<T> result = gson.fromJson(files.get("postData"), new TypeToken<RequestBean<T>>() {
            }.getType());
            if (classOfT.length > 0) {
                T body = result.getBody();
                result.setBody(gson.fromJson(body.toString(), classOfT[0]));
            }
            return result;
        } catch (IOException | ResponseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // parse request(body is bean list) from client generically
    private <T> RequestBean<List<T>> parseRequestBeanList(IHTTPSession session, Class<T> classOfT) {
        Map<String, String> files = new HashMap<>();

        try {
            session.parseBody(files);
            RequestBean<List<T>> result = gson.fromJson(files.get("postData"), new TypeToken<RequestBean<List<T>>>() {
            }.getType());
            List<T> list = new ArrayList<>();
            JsonArray jsonArray = jsonParser.parse(result.getBody().toString()).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                list.add(gson.fromJson(jsonElement, classOfT));
            }
            result.setBody(list);
            return result;
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
        LogUtils.log(TAG, "assembleResponse() result: " + result + ", id: " + id);
        return newFixedLengthResponse(Response.Status.OK, "text/json", gson.toJson(responseBean));
    }

    // assemble response by result, id, body
    private <T> Response assembleResponse(int result, long id, T body) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setResult(result);
        responseBean.setId(id);
        responseBean.setBody(body);
        LogUtils.log(TAG, "assembleResponse() result: " + result + ", id: " + id + ", body: " + body);
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
        final List<MotorBean> motorBeanList = new ArrayList<>();
        queryMotorsAsync(motorBeanList, result, semaphore, robot);
        // wait execute finished or timeout
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id, motorBeanList);
    }

    private Response serveExecMotors(IHTTPSession session) {
        LogUtils.log(TAG, "serveExecMotors()");
        final RequestBean<FrameBean> requestBean = parseRequestBean(session, FrameBean.class);
        if (requestBean == null || requestBean.getBody() == null) {
            LogUtils.log(TAG, "serveExecMotors() requestBeanList is null or requestBeanList.getBody() is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // execute motors async
        execMotorsAsync(requestBean.getBody(), result, semaphore, robot);
        // wait execute finished or timeout
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveCancelMotors(IHTTPSession session) {
        LogUtils.log(TAG, "serveCancelMotors()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveCancelMotors() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // cancel motors async
        cancelMotorsAsync(result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveExecEnterReadmode(IHTTPSession session) {
        LogUtils.log(TAG, "serveExecEnterReadmode()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveExecEnterReadmode() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // enter readmode async
        execEnterReadmodeAsync(result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveExecExitReadmode(IHTTPSession session) {
        LogUtils.log(TAG, "serveExecExitReadmode()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveExecExitReadmode() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // exit readmode async
        execExitReadmodeAsync(result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveQueryAction(IHTTPSession session) {
        LogUtils.log(TAG, "serveQueryAction()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveQueryAction() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // query action frame index async
        final int[] frameIndex = new int[1];
        queryActionAsync(frameIndex, result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id, frameIndex[0]);
    }

    private Response servePrepareAction(IHTTPSession session) {
        LogUtils.log(TAG, "servePrepareAction()");
        final RequestBean<ActionBean> requestBean = parseRequestBean(session, ActionBean.class);
        if (requestBean == null) {
            LogUtils.log(TAG, "servePrepareAction() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // prepare action async
        prepareActionAsync(requestBean.getBody(), result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveExecAction(IHTTPSession session) {
        LogUtils.log(TAG, "serveExecAction()");
        final RequestBean<String> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveExecAction() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // execute action async
        execActionAsync(requestBean.getBody(), result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveQueryActions(IHTTPSession session) {
        LogUtils.log(TAG, "serveQueryActions()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveQueryActions() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // query motors async
        final int[] actionIndex = new int[1];
        queryActionsAsync(actionIndex, result, semaphore, robot);
        // wait execute finished or timeout
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id, actionIndex[0]);
    }

    private Response servePrepareActions(IHTTPSession session) {
        LogUtils.log(TAG, "servePrepareActions()");
        final RequestBean<List<ActionBean>> requestBean = parseRequestBeanList(session, ActionBean.class);
        if (requestBean == null) {
            LogUtils.log(TAG, "servePrepareActions() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // prepare actions async
        prepareActionsAsync(requestBean.getBody(), result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private Response serveExecActions(IHTTPSession session) {
        LogUtils.log(TAG, "serveExecActions()");
        final RequestBean<List<String>> requestBean = parseRequestBeanList(session, String.class);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveExecActions() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // execute actions async
        execActionsAsync(requestBean.getBody(), result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }
    private Response serveCancelActions(IHTTPSession session) {
        LogUtils.log(TAG, "serveCancelActions()");
        final RequestBean<?> requestBean = parseRequestBean(session);
        if (requestBean == null) {
            LogUtils.log(TAG, "serveCancelActions() requestBean is null");
            return null;
        }
        final long id = requestBean.getId();
        final Robot robot = this.robot;
        final Semaphore semaphore = new Semaphore(0);
        final int[] result = new int[]{Constants.RESULT.FAIL};
        // execute actions async
        cancelActionsAsync(result, semaphore, robot);
        final int timeout = requestBean.getTimeout();
        final boolean timely = waitTimeout(timeout, semaphore);
        if (!timely) {
            return RESPONSE_ROBOT_TIMEOUT;
        }
        // assemble response
        return assembleResponse(result[0], id);
    }

    private void execMotorsAsync(final FrameBean frameBean, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.execMotors(frameBean, robot);
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

    private void cancelMotorsAsync(final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.cancelAllMotors(robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void execEnterReadmodeAsync(final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.execEnterReadmode(robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void execExitReadmodeAsync(final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.execExitReadmode(robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void queryActionAsync(final int[] frameIndex, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.queryAction(frameIndex, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void prepareActionAsync(final ActionBean actionBean, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.prepareAction(actionBean, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void execActionAsync(final String actionName, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.execAction(actionName, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void queryActionsAsync(final int[] actionIndex, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.queryActions(actionIndex, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void prepareActionsAsync(final List<ActionBean> actionBeanList, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.prepareActions(actionBeanList, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void execActionsAsync(final List<String> actionNameList, final int[] result, final Semaphore semaphore, final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.execActions(actionNameList, robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }

    private void cancelActionsAsync(final int[] result,final Semaphore semaphore,final Robot robot) {
        new Thread() {
            @Override
            public void run() {
                if (robotAdapter != null) {
                    // catch Exception here, let semaphore will be released forever
                    try {
                        result[0] = robotAdapter.cancelAllActions(robot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                semaphore.release();
            }
        }.start();
    }
}
