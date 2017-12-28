package com.afunx.server.interfaces;

/**
 * Created by afunx on 25/12/2017.
 */

public interface Robot {

    enum STATE {
        /**
         * robot is idle. robot could do more things until it is idle
         */
        IDLE,

        /**
         * robot is preparing.
         */
        PREPARE,

        /**
         * robot is cancelling.
         */
        CANCEL,

        /**
         * robot is executing.
         */
        EXECUTE,
    }

    /**
     * set robot new state
     *
     * @param newState new state
     */
    void setState(STATE newState);

    /**
     * get robot current state
     *
     * @return robot current state
     */
    STATE getState();

    /**
     * check whether robot is idle
     *
     * @return whether robot is idle
     */
    boolean isIdle();
}
