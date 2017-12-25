package com.afunx.data.constants;

/**
 * Created by afunx on 23/12/2017.
 */

public interface Constants {
    interface RESULT {
        /**
         * robot execute suc
         */
        int SUC = 0;
        /**
         * network timeout
         */
        int NETWORK_TIMEOUT = 1;
        /**
         * robot is busy
         */
        int ROBOT_BUSY = 2;
        /**
         * robot don't get result in time
         */
        int ROBOT_TIMEOUT = 3;
        /**
         * motion hasn't been prepared
         */
        int MOTION_ABSENT = 4;
    }
}