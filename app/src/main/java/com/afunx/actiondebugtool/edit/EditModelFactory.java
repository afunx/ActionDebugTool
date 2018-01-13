package com.afunx.actiondebugtool.edit;

import android.content.Context;

/**
 * Created by afunx on 13/01/2018.
 */

class EditModelFactory {
    /**
     * load model from storage, if null, create a instance
     *
     * @return EditModel
     */
    static EditModel provideEditModel(Context context) {
        return new EditModel();
    }
}
