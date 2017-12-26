package com.afunx.server.interfaces;

import com.afunx.data.bean.MotionBean;
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
     * @param motorBeanList motor bean list(store request)
     * @param robot         robot
     * @return {@link Constants.RESULT}
     */
    int execMotors(List<MotorBean> motorBeanList, Robot robot);

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
     * POST /query/motion/index
     * <p>
     * query motion's frame index running
     *
     * @param frameIndex motion's frame index (when index < 0 means no motion is running)
     * @param robot      robot
     * @return {@link Constants.RESULT}
     */
    int queryMotion(int[] frameIndex, Robot robot);

    /**
     * POST /prepare/motion
     * <p>
     * prepare motion for play
     *
     * @param motionBean motion bean(store request)
     * @return {@link Constants.RESULT}
     */
    int prepareMotion(MotionBean motionBean, Robot robot);

    /**
     * POST /exec/motion
     * <p>
     * play motion have been prepared
     *
     * @param motionName motion name
     * @param robot      robot
     * @return {@link Constants.RESULT}
     */
    int execMotion(String motionName, Robot robot);

    /**
     * POST /query/motions/index
     * <p>
     * query motion index running
     *
     * @param motionIndex motion index (when index < 0 means no motion is running)
     * @return {@link Constants.RESULT}
     */
    int queryMotions(int[] motionIndex, Robot robot);

    /**
     * POST /prepare/motions
     * <p>
     * prepare motion list for play
     *
     * @param motionBeanList motion bean list(store request)
     * @param robot          robot
     * @return {@link Constants.RESULT}
     */
    int prepareMotionList(List<MotionBean> motionBeanList, Robot robot);

    /**
     * POST /exec/motions
     * <p>
     * play motion have been prepared
     *
     * @param motionNameList motion name List
     * @param robot          robot
     * @return {@link Constants.RESULT}
     */
    int execMotionList(List<String> motionNameList, Robot robot);

    /**
     * POST /cancel/motions
     * <p>
     * cancel all motions
     *
     * @param robot robot
     * @return {@link Constants.RESULT}
     */
    int cancelAllMotions(Robot robot);

}