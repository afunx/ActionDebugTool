package com.afunx.actiondebugtool.common;

import android.content.Context;
import android.os.Environment;

import com.afunx.actiondebugtool.utils.FileUtils;
import com.afunx.actiondebugtool.utils.GsonUtils;
import com.afunx.actiondebugtool.utils.PrefUtils;
import com.afunx.data.bean.ActionBean;

import java.util.ArrayList;
import java.util.Collections;
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
     * get action name by file name
     *
     * @param filename file name
     * @return action name or null(invalid)
     */
    private String getActionName(String filename) {
        if (filename.endsWith(Constants.UBA_SUF)) {
            return filename.substring(0, filename.length() - Constants.UBA_SUF.length());
        } else {
            return null;
        }
    }

    /**
     * get file path by action name
     * ( /sdcard/action-uba/name.uba )
     *
     * @param name action
     * @return file path
     */
    private String getFilePath(String name) {
        return getFilePathRoot() + name + Constants.UBA_SUF;
    }

    /**
     * get action file root path
     * ( /sdcard/action-uba/ )
     *
     * @return action file root path
     */
    private String getFilePathRoot() {
        return Environment.getExternalStorageDirectory() + Constants.UBA_ROOT_PATH;
    }

    /**
     * read action list from SharedPreference
     *
     * @param appContext application Context
     * @return ActionBean list from SharedPreference
     */
    public List<ActionBean> readActionList(Context appContext) {
        List<ActionBean> list = (List<ActionBean>) PrefUtils.readObject(appContext);
        return list != null ? list : new ArrayList<ActionBean>();
    }

    /**
     * write action to SharedPreference
     *
     * @param appContext application Context
     * @param actionBean ActionBean to be written
     */
    public void writeAction(Context appContext, ActionBean actionBean) {
        List<ActionBean> list = readActionList(appContext);
        // find action bean with the same action name
        int index = getActionBeanIndex(list, actionBean.getName());
        if (index != -1) {
            // remove previous one
            list.remove(index);
            // add new one
            list.add(index, actionBean);
        } else {
            // add new actionBean at the end
            list.add(actionBean);
        }
        writeActionList(appContext, list);
    }

    /**
     * write action list to SharedPreference
     *
     * @param appContext     application Context
     * @param actionBeanList ActionBean list to be written
     */
    public void writeActionList(Context appContext, List<ActionBean> actionBeanList) {
        PrefUtils.writeObject(appContext, actionBeanList);
    }

    /**
     * check whether action name is exist in SharedPreference
     *
     * @param appContext application Context
     * @param actionName action name
     * @return action of the same name exist or not
     */
    public boolean isActionNameExistInSp(Context appContext, String actionName) {
        List<ActionBean> list = readActionList(appContext);
        return getActionBeanIndex(list, actionName) != -1;
    }

    /**
     * delete action to SharedPreference by actionName
     *
     * @param appContext application Context
     * @param actionName action name to be deleted
     */
    public void deleteAction(Context appContext, String actionName) {
        List<ActionBean> list = readActionList(appContext);
        int index = getActionBeanIndex(list, actionName);
        if (index == -1) {
            throw new IllegalStateException("actionName: " + actionName + " not exist");
        }
        list.remove(index);
        writeActionList(appContext, list);
    }

    /**
     * input action list from sd card
     *
     * @return ActionBean list from sd card
     */
    public List<ActionBean> inputActionList() {
        String filePathRoot = getFilePathRoot();
        List<String> list = FileUtils.list(filePathRoot);
        if (list.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<ActionBean> actionBeanList = new ArrayList<>();
            for (String fileName : list) {
                String actionName = getActionName(fileName);
                String filePath = getFilePath(actionName);
                String json = FileUtils.readString(filePath);
                if (json != null) {
                    ActionBean actionBean = GsonUtils.fromGson(json, ActionBean.class);
                    actionBeanList.add(actionBean);
                }
            }
            return actionBeanList;
        }
    }

    /**
     * output action to sd card
     *
     * @param actionBean ActionBean to be output
     * @return write to sd card suc or not
     */
    public boolean outputAction(ActionBean actionBean) {
        String filePath = getFilePath(actionBean.getName());
        String json = GsonUtils.toGson(actionBean);
        return FileUtils.writeString(filePath, json);
    }

    /**
     * output action list to sd card
     *
     * @param actionBeanList ActionBean List to be output
     * @return write to sd card suc or not
     */
    public boolean outputActionList(List<ActionBean> actionBeanList) {
        for (ActionBean actionBean : actionBeanList) {
            if (!outputAction(actionBean)) {
                return false;
            }
        }
        return true;
    }

    /**
     * get ActionBean index by action name
     *
     * @param actionBeanList ActionBean List
     * @param actionName     action name
     * @return index
     */
    private int getActionBeanIndex(List<ActionBean> actionBeanList, String actionName) {
        for (int i = 0; i < actionBeanList.size(); i++) {
            if (actionName.equals(actionBeanList.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    private static class SingletonHolder {
        private static final ActionManager INSTANCE = new ActionManager();
    }
}
