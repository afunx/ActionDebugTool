package com.afunx.actiondebugtool.edit;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by afunx on 12/01/2018.
 */

class EditPresenter implements EditContract.Presenter {

    private final EditModel mEditModel;

    private final EditContract.View mEditView;

    EditPresenter(@NonNull Context appContext, @NonNull EditContract.View editView) {
        mEditModel = EditModelFactory.provideEditModel(appContext);
        mEditView = editView;

        editView.setPresenter(this);
    }
}