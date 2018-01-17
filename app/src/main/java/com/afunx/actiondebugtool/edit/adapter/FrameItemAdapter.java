package com.afunx.actiondebugtool.edit.adapter;

import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afunx.actiondebugtool.R;
import com.afunx.actiondebugtool.data.FrameData;
import com.afunx.actiondebugtool.edit.EditContract;
import com.afunx.data.bean.FrameBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by afunx on 12/01/2018.
 */

public class FrameItemAdapter extends RecyclerView.Adapter<FrameItemAdapter.ViewHolder> {

    private RecyclerView mRecyclerView;

    private final List<FrameData> mFrameDataList;

    private final EditContract.Presenter mEditPresenter;

    public FrameItemAdapter(RecyclerView rycFrameItems, List<FrameData> frameDataList, EditContract.Presenter editPresenter) {
        mRecyclerView = rycFrameItems;
        mFrameDataList = frameDataList;
        mEditPresenter = editPresenter;
    }

    public int getSelectedIndex() {
        for (int index = 0; index < mFrameDataList.size(); index++) {
            if (mFrameDataList.get(index).isSelected()) {
                return index;
            }
        }
        return -1;
    }

    public void setSelectedIndex(int selectedIndex) {

        mEditPresenter.setSelectedFrameIndex(selectedIndex);

        int prevSelectedIndex = getSelectedIndex();
        if (prevSelectedIndex != selectedIndex) {
            if (prevSelectedIndex != -1) {
                mFrameDataList.get(prevSelectedIndex).setSelected(false);
                notifyItemChanged(prevSelectedIndex);
            }
            mFrameDataList.get(selectedIndex).setSelected(true);
            notifyItemChanged(selectedIndex);
        }
    }

    public void insertFrame(int frameIndex) {
        mFrameDataList.add(frameIndex, new FrameData());
        notifyItemInserted(frameIndex);
        scrollToSuitablePosition();
    }

    public void delete(int index) {
        mFrameDataList.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * get FrameBean list from mFrameDataList
     * (FrameBean is cloned from FrameData)
     *
     * @return FrameBean list
     */
    public List<FrameBean> getFrameBeanList() {
        List<FrameBean> frameBeanList = new ArrayList<>();
        for (FrameData frameData : mFrameDataList) {
            frameBeanList.add(frameData.getFrameBean().clone());
        }
        return frameBeanList;
    }

    /**
     * scroll items to suitable position after adding or pasting
     */
    private void scrollToSuitablePosition() {
        int lastVisibleItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
        int firstVisibleItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int selectedPosition = getSelectedIndex();
        if (selectedPosition >= lastVisibleItemPosition) {
            mRecyclerView.scrollToPosition(selectedPosition + 1);
        } else if (selectedPosition <= firstVisibleItemPosition) {
            mRecyclerView.scrollToPosition(selectedPosition - 2 < 0 ? 0 : selectedPosition - 2);
        } else {
            mRecyclerView.scrollToPosition(lastVisibleItemPosition + 1);
        }
    }

    public void copy(int copiedIndex) {
        int prevCopiedIndex = getCopiedFrameIndex();
        if (prevCopiedIndex != copiedIndex) {
            if (prevCopiedIndex != -1) {
                mFrameDataList.get(prevCopiedIndex).setCopied(false);
                notifyItemChanged(prevCopiedIndex);
            }
            mFrameDataList.get(copiedIndex).setCopied(true);
            notifyItemChanged(copiedIndex);
        }
    }

    public int getCopiedFrameIndex() {
        for (int index = 0; index < mFrameDataList.size(); index++) {
            if (mFrameDataList.get(index).isCopied()) {
                return index;
            }
        }
        return -1;
    }

    public void clearCopy() {
        for (int index = 0; index < mFrameDataList.size(); index++) {
            FrameData frameData = mFrameDataList.get(index);
            if (frameData.isCopied()) {
                frameData.setCopied(false);
                notifyItemChanged(index);
                return;
            }
        }
    }

    public void pasteAfterSelected() {
        int copiedIndex = getCopiedFrameIndex();
        if (copiedIndex == -1) {
            throw new IllegalStateException("copiedIndex should be >= 0");
        }
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) {
            throw new IllegalStateException("selectedIndex should be >= 0");
        }
        FrameData copied = mFrameDataList.get(copiedIndex).clone();
        mFrameDataList.add(selectedIndex + 1, copied);
        notifyItemInserted(selectedIndex + 1);
        notifyItemChanged(selectedIndex + 2);
        scrollToSuitablePosition();
    }

    private int _frameIndex = 0;

    /**
     * generate frame index(it should never repeat)
     * (it only be used to show item, besides, frame index means mFrameDataList's position)
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
        final FrameData frameData = mFrameDataList.get(position);
        final FrameBean frameBean = frameData.getFrameBean();
        boolean isSelected = frameData.isSelected();
        if (frameBean.getIndex() == 0) {
            frameBean.setIndex(genFrameIndex());
        }
        // set text by copied state
        String text = frameData.isCopied()
                ? String.format(Locale.US, "%d-C", frameBean.getIndex())
                : String.format(Locale.US, "%d", frameBean.getIndex());
        holder.tvFrameIndex.setText(text);
        holder.tvFrameName.setText(frameBean.getName());
        holder.tvFrameRuntime.setText(String.format(Locale.US, "%d", frameBean.getTime()));
        // set selected or not
        holder.percentRelativeLayout.setSelected(isSelected);

        // set onClickListener
        holder.percentRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    // play frame when item has been selected already
                    mEditPresenter.playSelectedFrame();
                } else {
                    int frameIndex = holder.getAdapterPosition();
                    // update selected index
                    setSelectedIndex(frameIndex);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFrameDataList.size();
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
