package com.afunx.server.interfaces;

import com.afunx.data.bean.ActionBean;
import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotorBean;
import com.afunx.data.constants.Constants;

import java.util.List;

/**
 * Created by afunx on 23/12/2017.
 */

public interface RobotAdapter {

    /**
     * POST /query/motors
     * <p>
     * query motors
     *
     * @param motorBeanList motor bean list(store result)
     * @param robot         robot
     * @return {@link Constants.RESULT}
     */
    int queryMotors(List<MotorBean> motorBeanList, Robot robot);

    /**
     * POST /exec/motors
     * <p>
     * execute motors
     *
     * @param frameBean frameBean(store request)
     * @param robot     robot
     * @return {@link Constants.RESULT}
     */
    int execMotors(FrameBean frameBean, Robot robot);

    /**
     * POST /cancel/motors
     * <p>
     * cancel all motors
     *
     * @param robot robot
     * @return {@link Constants.RESULT}
     */
    int cancelAllMotors(Robot robot);

    /**
     * POST /exec/readmode_one/enter
     * <p>
     * execute enter read mode for one motor
     *
     * @param robot   robot
     * @param motorId motor id
     * @return {@link Constants.RESULT}
     */
    int execEnterReadmodeOne(Robot robot, int motorId);

    /**
     * POST /exec/readmode/enter
     * <p>
     * execute enter read mode
     *
     * @param robot robot
     * @return {@link Constants.RESULT}
     */
    int execEnterReadmode(Robot robot);

    /**
     * POST /exec/readmode/exit
     * <p>
     * execute exit read mode
     *
     * @param robot robot
     * @return {@link Constants.RESULT}
     */
    int execExitReadmode(Robot robot);

    /**
     * POST /query/action/index
     * <p>
     * query action's frame index running
     *
     * @param frameIndex action's frame index (when index < 0 means no action is running)
     * @param robot      robot
     * @return {@link Constants.RESULT}
     */
    int queryAction(int[] frameIndex, Robot robot);

    /**
     * POST /prepare/action
     * <p>
     * prepare action for play
     *
     * @param actionBean action bean(store request)
     * @return {@link Constants.RESULT}
     */
    int prepareAction(ActionBean actionBean, Robot robot);

    /**
     * POST /exec/action
     * <p>
     * play action have been prepared
     *
     * @param actionName action name
     * @param robot      robot
     * @return {@link Constants.RESULT}
     */
    int execAction(String actionName, Robot robot);

    /**
     * POST /query/actions/index
     * <p>
     * query action index running
     *
     * @param actionIndex action index (when index < 0 means no action is running)
     * @return {@link Constants.RESULT}
     */
    int queryActions(int[] actionIndex, Robot robot);

    /**
     * POST /prepare/actions
     * <p>
     * prepare action list for play
     *
     * @param actionBeanList action bean list(store request)
     * @param robot          robot
     * @return {@link Constants.RESULT}
     */
    int prepareActions(List<ActionBean> actionBeanList, Robot robot);

    /**
     * POST /exec/actions
     * <p>
     * play action have been prepared
     *
     * @param actionNameList action name List
     * @param robot          robot
     * @return {@link Constants.RESULT}
     */
    int execActions(List<String> actionNameList, Robot robot);

    /**
     * POST /cancel/actions
     * <p>
     * cancel all actions
     *
     * @param robot robot
     * @return {@link Constants.RESULT}
     */
    int cancelAllActions(Robot robot);

}