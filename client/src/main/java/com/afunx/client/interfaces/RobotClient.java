package com.afunx.client.interfaces;

import com.afunx.data.bean.ActionBean;
import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.constants.Constants;

import java.util.List;

/**
 * Created by afunx on 26/12/2017.
 */

public interface RobotClient {

    /**
     * set robot server's ip address 4
     *
     * @param ipAddr4 ip address 4
     */
    void setIp4(byte[] ipAddr4);

    /**
     * check whether robot is idle
     *
     * @return whether robot is idle
     */
    boolean isRobotIdle();

    /**
     * GET /query/motors
     * <p>
     * query motors
     *
     * @param motorBeanList motor bean list(store result)
     * @return {@link Constants.RESULT}
     */
    int queryMotors(List<MotorBean> motorBeanList);

    /**
     * POST /exec/motors
     * <p>
     * execute motors
     *
     * @param frameBean frame bean(store request)
     * @return {@link Constants.RESULT}
     */
    int execMotors(FrameBean frameBean);

    /**
     * POST /cancel/motors
     * <p>
     * cancel all motors
     *
     * @return {@link Constants.RESULT}
     */
    int cancelAllMotors();

    /**
     * POST /exec/readmode/enter
     * <p>
     * execute enter read mode
     *
     * @return {@link Constants.RESULT}
     */
    int execEnterReadmode();

    /**
     * POST /exec/readmode/exit
     * <p>
     * execute exit read mode
     *
     * @return {@link Constants.RESULT}
     */
    int execExitReadmode();

    /**
     * GET /query/action/index
     * <p>
     * query action's frame index running
     *
     * @param frameIndex action's frame index (when index < 0 means no action is running)
     * @return {@link Constants.RESULT}
     */
    int queryAction(int[] frameIndex);

    /**
     * POST /prepare/action
     * <p>
     * prepare action for play
     *
     * @param action action bean(store request)
     * @return {@link Constants.RESULT}
     */
    int prepareAction(ActionBean action);

    /**
     * POST /exec/action
     * <p>
     * play action have been prepared
     *
     * @param actionName action name
     * @return {@link Constants.RESULT}
     */
    int execAction(String actionName);

    /**
     * GET /query/actions/index
     * <p>
     * query action index running
     *
     * @param actionIndex action index (when index < 0 means no action is running)
     * @return {@link Constants.RESULT}
     */
    int queryActions(int[] actionIndex);

    /**
     * POST /prepare/actions
     * <p>
     * prepare action list for play
     *
     * @param actionBeanList action bean list(store request)
     * @return {@link Constants.RESULT}
     */
    int prepareActions(List<ActionBean> actionBeanList);

    /**
     * POST /exec/actions
     * <p>
     * play action have been prepared
     *
     * @param actionBeanList action name list
     * @return {@link Constants.RESULT}
     */
    int execActions(List<String> actionBeanList);

    /**
     * POST /cancel/actions
     * <p>
     * cancel all actions
     *
     * @return {@link Constants.RESULT}
     */
    int cancelAllActions();
}
