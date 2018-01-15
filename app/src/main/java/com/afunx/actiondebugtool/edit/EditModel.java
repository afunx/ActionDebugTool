package com.afunx.actiondebugtool.edit;

import android.content.Context;

import com.afunx.actiondebugtool.data.FrameData;

import java.util.List;

/**
 * Created by afunx on 12/01/2018.
 */

class EditModel {

    private final Context appContext;
    private final List<FrameData> mFrameDataList;

    EditModel(Context context, List<FrameData> frameDataList) {
        appContext = context.getApplicationContext();
        mFrameDataList = frameDataList;
    }

}
