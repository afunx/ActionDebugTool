package com.afunx.server.interfaces;

/**
 * Created by afunx on 23/12/2017.
 */

/**
 *                 send request
 *             ----------------->
 * RobotClient                   RobotServer
 *             <-----------------     |
 *              receive response      |
 *                                    |
 *                               RobotAdapter(if RobotAdapter process too long, reply timeout response)
 *
 * Created by afunx on 23/12/2017.
 */
public interface RobotServer {
    /**
     * set RobotAdapter
     *
     * @param robotAdapter robot adapter
     */
    void setAdapter(RobotAdapter robotAdapter);
}