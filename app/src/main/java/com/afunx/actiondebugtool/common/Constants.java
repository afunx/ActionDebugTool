package com.afunx.actiondebugtool.common;

/**
 * Created by afunx on 16/01/2018.
 */

public interface Constants {

    int MOTORS_COUNT = 14;

    int[] MOTORS_DEG_MIN = new int[]{-120, -50, -120, -50, -120, -120, -50, -120, -120, -50, -10, -12, -50, -60};

    int[] MOTORS_DEG_MAX = new int[]{120, 50, 120, 50, 120, 120, 12, 120, 120, 12, 10, 10, 50, 100};

    int RUNTIME_MIN = 100;

    int RUNTIME_MAX = 5000;

    int RUNTIME_DEFAULT = 1000;

    String UBA_ROOT_PATH = "/action-uba/";

    String UBA_SUF = ".uba";
}
