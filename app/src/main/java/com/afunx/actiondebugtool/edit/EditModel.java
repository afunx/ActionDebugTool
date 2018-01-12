package com.afunx.actiondebugtool.edit;

import android.content.Context;

/**
 * Created by afunx on 12/01/2018.
 */

class EditModel {
    /**
     * load model from storage, if null, create a instance
     *
     * @return EditModel
     */
    static EditModel load(Context context) {
        return new EditModel();
    }
}
