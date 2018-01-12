package com.afunx.actiondebugtool.main.adapter;

import android.content.Intent;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afunx.actiondebugtool.R;
import com.afunx.data.bean.MotionBean;

import java.util.List;

/**
 * Created by afunx on 09/01/2018.
 */

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ViewHolder> {

    private final List<MotionBean> mMotionBeanList;

    public ActionItemAdapter(List<MotionBean> motionBeanList) {
        mMotionBeanList = motionBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // percent height of parent
        float percentHeight = 0.15f;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item, parent, false);
        int height = (int) (parent.getHeight() * percentHeight);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.percentRelativeLayout.getLayoutParams().height = height;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MotionBean motionBean = mMotionBeanList.get(position);
        holder.tvActionName.setText(motionBean.getName());
        // TODO btnActionEdit
        holder.btnActionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.afunx.actiondebugtool.editAction");
                intent.putExtra("action", mMotionBeanList.get(position));
                v.getContext().startActivity(intent);
            }
        });
        // TODO btnActionDelete
    }

    @Override
    public int getItemCount() {
        return mMotionBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private PercentRelativeLayout percentRelativeLayout;
        private Button btnActionEdit;
        private Button btnActionDelete;
        private TextView tvActionName;

        public ViewHolder(View view) {
            super(view);
            percentRelativeLayout = (PercentRelativeLayout) itemView.findViewById(R.id.prl_action_item);
            btnActionEdit = (Button) itemView.findViewById(R.id.btn_action_edit);
            btnActionDelete = (Button) itemView.findViewById(R.id.btn_action_delete);
            tvActionName = (TextView) itemView.findViewById(R.id.tv_action_name);
        }
    }
}
