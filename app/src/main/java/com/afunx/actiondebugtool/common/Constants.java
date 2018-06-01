package com.afunx.actiondebugtool.common;

/**
 * Created by afunx on 16/01/2018.
 */

public interface Constants {

    int MOTORS_COUNT = 14;

    int[] MOTORS_DEG_MIN = new int[]{-90, -80, -90, -80, -70, -65, -10, -70, -65, -10, 10, -10, -50, -50};

    int[] MOTORS_DEG_MAX = new int[]{90, 0, 90, 0, 90, 90, 15, 90, 90, 15, 10, 10, 50, 50};

    int RUNTIME_MIN = 100;

    int RUNTIME_MAX = 5000;

    int RUNTIME_DEFAULT = 1000;

    String UBA_ROOT_PATH = "/action-uba/";

    String UBA_SUF = ".uba";
}
