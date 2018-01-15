package com.afunx.actiondebugtool.edit;

import android.content.Context;

import com.afunx.actiondebugtool.data.FrameData;

import java.util.List;

/**
 * Created by afunx on 13/01/2018.
 */

class EditModelFactory {
    /**
     * load model from storage, if null, create a instance
     *
     * @param frameDataList frameData list from MainActivity
     * @return EditModel
     */
    static EditModel provideEditModel(Context context, List<FrameData> frameDataList) {
        return new EditModel(context, frameDataList);
    }


}