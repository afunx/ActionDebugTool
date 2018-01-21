package com.afunx.actiondebugtool.edit;

import com.afunx.actiondebugtool.BasePresenter;
import com.afunx.actiondebugtool.BaseView;

/**
 * Created by afunx on 12/01/2018.
 */

public interface EditContract {

    interface View extends BaseView<Presenter> {
        /**
         * show Toast
         *
         * @param text Toast text
         */
        void showToast(String text);

        /**
         * show Toast
         * @param resId Resource String id
         */
        void showToast(int resId);

        /**
         * get selected motorId
         *
         * @return selected motorId or -1(no motor is selected)
         */
        int getSelectedMotorId();

        /**
         * set selected motorId
         *
         * @param selectedMotorId selected motorId
         */
        void setSelectedMotorId(int selectedMotorId);

        /**
         * get current motor degree from ui
         *
         * @return current motor degree
         */
        int getMotorDeg();

        /**
         * set current motor degree to ui
         *
         * @param deg motor degree
         */
        void setMotorDeg(int deg);

        /**
         * get current motor min degree from ui
         *
         * @return current motor min degree
         */
        int getMotorDegMin();

        /**
         * set current motor min degree to ui
         *
         * @param degMin min motor degree
         */
        void setMotorDegMin(int degMin);

        /**
         * get current motor max degree from ui
         *
         * @return current motor max degree
         */
        int getMotorDegMax();

        /**
         * set current motor max degree to ui
         *
         * @param degMax max motor degree
         */
        void setMotorDegMax(int degMax);

        /**
         * get selected frame index
         *
         * @return selected frame index or -1(no frame is selected)
         */
        int getSelectedFrameIndex();

        /**
         * set selected frame index
         *
         * @param frameIndex selected frame index
         */
        void setSelectedFrameIndex(int frameIndex);

        /**
         * get frame list size
         *
         * @return frame list size
         */
        int getFrameCount();

        /**
         * get current frame runtime from ui
         *
         * @return current frame runtime
         */
        int getFrameRuntime();

        /**
         * set current frame runtime to ui
         *
         * @param runtime frame runtime
         */
        void setFrameRuntime(int runtime);

        /**
         * get current frame min runtime from ui
         *
         * @return current frame min runtime
         */
        int getFrameRuntimeMin();

        /**
         * set current frame min runtime to ui
         *
         * @param runtimeMin min frame runtime
         */
        void setFrameRuntimeMin(int runtimeMin);

        /**
         * get current frame max runtime from ui
         *
         * @return current frame max runtime
         */
        int getFrameRuntimeMax();

        /**
         * set current frame max runtime to ui
         *
         * @param runtimeMax max frame runtime
         */
        void setFrameRuntimeMax(int runtimeMax);

        /**
         * update frame of some position
         *
         * @param frameIndex position to be updated
         */
        void updateFrame(int frameIndex);

        /**
         * insert frame after frameIndex
         *
         * @param frameIndex frame index to be inserted
         */
        void insertFrame(int frameIndex);

        /**
         * delete frame of frameIndex
         *
         * @param frameIndex frame index to be deleted
         */
        void deleteFrame(int frameIndex);

        /**
         * copy frame of frameIndex
         *
         * @param frameIndex frame index to be copied
         */
        void copyFrame(int frameIndex);

        /**
         * get copied frame of frameIndex
         *
         * @return copied frame index or -1(no frame is copied)
         */
        int getCopiedFrameIndex();

        /**
         * clear copy frame state
         */
        void clearCopy();

        /**
         * paste copied frame after selected
         */
        void pasteAfterSelected();
    }

    interface Presenter extends BasePresenter {

        /**
         * set robot ip address
         *
         * @param ipAddr robot's ip address
         */
        void setRobotIpAddr(String ipAddr);

        /**
         * get frame runtime min
         *
         * @return frame runtime min
         */
        int getFrameRuntimeMin();

        /**
         * get frame runtime max
         *
         * @return frame runtime max
         */
        int getFrameRuntimeMax();

        /**
         * insert frame after the selected frame
         */
        void insertFrameAfterSelected();

        /**
         * set selected frame index
         *
         * @param frameIndex frame index
         */
        void setSelectedFrameIndex(int frameIndex);

        /**
         * set selected motor
         *
         * @param motorId selected motorId
         */
        void setSelectedMotor(int motorId);

        /**
         * set selected frame motor degree
         *
         * @param degree motor degree
         */
        void setSelectedMotorDegree(int degree);

        /**
         * set selected frame runtime
         *
         * @param runtime frame runtime
         */
        void setSelectedFrameRuntime(int runtime);

        /**
         * enter read mode
         */
        void enterReadMode();

        /**
         * read motors
         */
        void readMotors();

        /**
         * play selected frame
         */
        void playSelectedFrame();

        /**
         * play action from selected frame
         */
        void playActionFromSelectedFrame();

        /**
         * delete selected frame
         */
        void deleteSelectedFrame();

        /**
         * copy selected frame or clear copied state(if it is copied already)
         */
        void copySelectedFrame();

        /**
         * paste copied frame after selected
         */
        void pasteAfterSelected();
    }
}
