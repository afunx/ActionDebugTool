package com.afunx.data.constants;

/**
 * Created by afunx on 23/12/2017.
 */

public interface Constants {
    // when result is < 0 means robot sdk error code
    interface RESULT {
        /**
         * robot execute suc
         */
        int SUC = 0;
        /**
         * robot execute fail, unknown reason
         */
        int FAIL = 1;
        /**
         * network timeout
         */
        int NETWORK_TIMEOUT = 2;
        /**
         * robot is busy
         */
        int ROBOT_BUSY = 3;
        /**
         * robot don't get result in time
         */
        int ROBOT_TIMEOUT = 4;
        /**
         * motion hasn't been prepared
         */
        int MOTION_ABSENT = 5;
        /**
         * client IOException
         */
        int CLIENT_IOEXCEPTION = 6;
        /**
         * client Exception
         */
        int CLIENT_EXCEPTION = 7;
    }
    // robot busy state
    interface ROBOT_BUSY_STATE {
        /**
         * robot is idle
         */
        int IDLE = 0;
        /**
         * robot is busy
         */
        int BUSY = 1;
    }
}