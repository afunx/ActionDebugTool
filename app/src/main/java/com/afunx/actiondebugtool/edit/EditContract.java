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
    }

    interface Presenter extends BasePresenter {
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
         * play motion from selected frame
         */
        void playMotionFromSelectedFrame();

        /**
         * delete selected frame
         */
        void deleteSelectedFrame();

        /**
         * copy selected frame
         */
        void copySelectedFrame();

        /**
         * insert frame after selected
         */
        void insertFrameAfterSelected();
    }
}
