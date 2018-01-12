package com.afunx.actiondebugtool.edit;

import android.support.annotation.NonNull;

/**
 * Created by afunx on 12/01/2018.
 */

class EditPresenter implements EditContract.Presenter {

    private final EditModel mEditModel;

    private final EditContract.View mEditView;

    EditPresenter(@NonNull EditModel editModel, @NonNull EditContract.View editView) {
        mEditModel = editModel;
        mEditView = editView;

        editView.setPresenter(this);
    }
}