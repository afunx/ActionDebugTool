package com.afunx.client.interfaces;

import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotionBean;
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
     * GET /query/motion/index
     * <p>
     * query motion's frame index running
     *
     * @param frameIndex motion's frame index (when index < 0 means no motion is running)
     * @return {@link Constants.RESULT}
     */
    int queryMotion(int[] frameIndex);

    /**
     * POST /prepare/motion
     * <p>
     * prepare motion for play
     *
     * @param motionBean motion bean(store request)
     * @return {@link Constants.RESULT}
     */
    int prepareMotion(MotionBean motionBean);

    /**
     * POST /exec/motion
     * <p>
     * play motion have been prepared
     *
     * @param motionName motion name
     * @return {@link Constants.RESULT}
     */
    int execMotion(String motionName);

    /**
     * GET /query/motions/index
     * <p>
     * query motion index running
     *
     * @param motionIndex motion index (when index < 0 means no motion is running)
     * @return {@link Constants.RESULT}
     */
    int queryMotions(int[] motionIndex);

    /**
     * POST /prepare/motions
     * <p>
     * prepare motion list for play
     *
     * @param motionBeanList motion bean list(store request)
     * @return {@link Constants.RESULT}
     */
    int prepareMotions(List<MotionBean> motionBeanList);

    /**
     * POST /exec/motions
     * <p>
     * play motion have been prepared
     *
     * @param motionNameList motion name list
     * @return {@link Constants.RESULT}
     */
    int execMotions(List<String> motionNameList);

    /**
     * POST /cancel/motions
     * <p>
     * cancel all motions
     *
     * @return {@link Constants.RESULT}
     */
    int cancelAllMotions();
}
