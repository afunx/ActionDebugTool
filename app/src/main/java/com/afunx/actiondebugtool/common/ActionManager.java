package com.afunx.actiondebugtool.common;

import android.content.Context;

import com.afunx.actiondebugtool.utils.PrefUtils;
import com.afunx.data.bean.MotionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 15/01/2018.
 */

public class ActionManager {

    private ActionManager() {
    }

    public static ActionManager get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * read action list from SharedPreference
     *
     * @param appContext application Context
     * @return MotionBean list from SharedPreference
     */
    public List<MotionBean> readActionList(Context appContext) {
        List<MotionBean> list = (List<MotionBean>) PrefUtils.readObject(appContext);
        return list != null ? list : new ArrayList<MotionBean>();
    }

    /**
     * write action to SharedPreference
     *
     * @param appContext application Context
     * @param motionBean MotionBean to be written
     */
    public void writeAction(Context appContext, MotionBean motionBean) {
        List<MotionBean> list = readActionList(appContext);
        // find motion bean with the same motion name
        int index = getMotionBeanIndex(list, motionBean.getName());
        if (index != -1) {
            // remove previous one
            list.remove(index);
            // add new one
            list.add(index, motionBean);
        } else {
            // add new motionBean at the end
            list.add(motionBean);
        }
        writeActionList(appContext, list);
    }

    /**
     * write action list to SharedPreference
     *
     * @param appContext     application Context
     * @param motionBeanList MotionBean list to be written
     */
    public void writeActionList(Context appContext, List<MotionBean> motionBeanList) {
        PrefUtils.writeObject(appContext, motionBeanList);
    }

    /**
     * check whether action name is exist in SharedPreference
     *
     * @param appContext application Context
     * @param actionName action name
     * @return action of the same name exist or not
     */
    public boolean isActionNameExistInSp(Context appContext, String actionName) {
        List<MotionBean> list = readActionList(appContext);
        return getMotionBeanIndex(list, actionName) != -1;
    }

    /**
     * delete action to SharedPreference by motionName
     *
     * @param appContext application Context
     * @param actionName action name to be deleted
     */
    public void deleteMotion(Context appContext, String actionName) {
        List<MotionBean> list = readActionList(appContext);
        int index = getMotionBeanIndex(list, actionName);
        if (index == -1) {
            throw new IllegalStateException("actionName: " + actionName + " not exist");
        }
        list.remove(index);
        writeActionList(appContext, list);
    }

    /**
     * input action list from sd card
     *
     * @return MotionBean list from sd card
     */
    public List<MotionBean> inputActionList() {
        return null;
    }

    /**
     * output action to sd card
     *
     * @param motionBean MotionBean to be output
     */
    public void outputAction(MotionBean motionBean) {

    }

    /**
     * output action list to sd card
     *
     * @param motionBeanList MotionBean List to be output
     */
    public void outputActionList(List<MotionBean> motionBeanList) {

    }

    /**
     * get MotionBean index by motion name
     *
     * @param motionBeanList MotionBean List
     * @param motionName     motion name
     * @return index
     */
    private int getMotionBeanIndex(List<MotionBean> motionBeanList, String motionName) {
        for (int i = 0; i < motionBeanList.size(); i++) {
            if (motionName.equals(motionBeanList.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    private static class SingletonHolder {
        private static final ActionManager INSTANCE = new ActionManager();
    }
}
