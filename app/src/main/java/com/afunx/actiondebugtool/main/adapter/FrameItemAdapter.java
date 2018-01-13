package com.afunx.actiondebugtool.main.adapter;

import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afunx.actiondebugtool.R;
import com.afunx.actiondebugtool.edit.EditContract;
import com.afunx.data.bean.FrameBean;

import java.util.List;
import java.util.Locale;

/**
 * Created by afunx on 12/01/2018.
 */

public class FrameItemAdapter extends RecyclerView.Adapter<FrameItemAdapter.ViewHolder> {

    private final List<FrameBean> mFrameBeanList;

    private final EditContract.Presenter mEditPresenter;

    private int mSelectedIndex = -1;

    public FrameItemAdapter(List<FrameBean> frameBeanList, EditContract.Presenter editPresenter) {
        mFrameBeanList = frameBeanList;
        mEditPresenter = editPresenter;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        int prevSelectedIndex = mSelectedIndex;
        mSelectedIndex = selectedIndex;
        notifyItemChanged(prevSelectedIndex);
        notifyItemChanged(mSelectedIndex);
    }

    public void delete(int index) {
        mFrameBeanList.remove(index);
        notifyItemRemoved(index);
    }

    private int _frameIndex = 0;

    /**
     * generate frame index(it should never repeat)
     * (it only be used to show item, besides, frame index means mFrameBeanList's position)
     *
     * @return frame index
     */
    private int genFrameIndex() {
        return ++_frameIndex;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FrameBean frameBean = mFrameBeanList.get(position);
        if (frameBean.getIndex() == 0) {
            frameBean.setIndex(genFrameIndex());
        }
        holder.tvFrameIndex.setText(String.format(Locale.US, "%d", frameBean.getIndex()));
        holder.tvFrameName.setText(frameBean.getName());
        holder.tvFrameRuntime.setText(String.format(Locale.US, "%d", frameBean.getTime()));
        // set selected or not
        holder.percentRelativeLayout.setSelected(position == mSelectedIndex);
        // set onClickListener
        holder.percentRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    // play frame when item has been selected already
                    mEditPresenter.playSelectedFrame();
                } else {
                    // update selected index
                    mSelectedIndex = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
        });
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
            tvFrameName = (TextView) view.findViewById(R.id.tv_frame_name);
            tvFrameRuntime = (TextView) view.findViewById(R.id.tv_frame_runtime);
        }
    }
}
