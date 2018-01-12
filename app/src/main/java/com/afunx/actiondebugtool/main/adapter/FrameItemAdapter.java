package com.afunx.actiondebugtool.main.adapter;

import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afunx.actiondebugtool.R;
import com.afunx.data.bean.FrameBean;

import java.util.List;
import java.util.Locale;

/**
 * Created by afunx on 12/01/2018.
 */

public class FrameItemAdapter extends RecyclerView.Adapter<FrameItemAdapter.ViewHolder> {

    private final List<FrameBean> mFrameBeanList;

    public FrameItemAdapter(List<FrameBean> frameBeanList) {
        mFrameBeanList = frameBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // aspect ratio(width : height)
        final float aspectRatio = 1.0f;
        // percent height of parent
        final float heightPercent = 1.0f;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        int height = (int) (parent.getHeight() * heightPercent);
        int width = (int) (height * aspectRatio);

        if (height == 0 || width == 0) {
            // when height or width is 0, request layout later
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    int height = parent.getHeight();
                    int width = (int) (height * aspectRatio);
                    viewHolder.percentRelativeLayout.getLayoutParams().width = width;
                    viewHolder.percentRelativeLayout.getLayoutParams().height = height;
                    parent.requestLayout();
                }
            });
        } else {
            viewHolder.percentRelativeLayout.getLayoutParams().width = width;
            viewHolder.percentRelativeLayout.getLayoutParams().height = height;
        }


        viewHolder.percentRelativeLayout.getLayoutParams().width = width;
        viewHolder.percentRelativeLayout.getLayoutParams().height = height;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FrameBean frameBean = mFrameBeanList.get(position);
        holder.tvFrameIndex.setText(String.format(Locale.US, "%d", position));
        holder.tvFrameName.setText(frameBean.getName());
        holder.tvFrameRuntime.setText(String.format(Locale.US, "%d", frameBean.getTime()));
    }

    @Override
    public int getItemCount() {
        return mFrameBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private PercentRelativeLayout percentRelativeLayout;

        private TextView tvFrameIndex;
        private TextView tvFrameName;
        private TextView tvFrameRuntime;

        ViewHolder(View view) {
            super(view);
            percentRelativeLayout = (PercentRelativeLayout) view.findViewById(R.id.prl_frame_item);
            tvFrameIndex = (TextView) view.findViewById(R.id.tv_frame_index);
            tvFrameName =  (TextView) view.findViewById(R.id.tv_frame_name);
            tvFrameRuntime = (TextView) view.findViewById(R.id.tv_frame_runtime);
        }
    }
}
