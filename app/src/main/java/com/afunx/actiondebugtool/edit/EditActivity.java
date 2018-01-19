package com.afunx.actiondebugtool.edit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afunx.actiondebugtool.R;
import com.afunx.actiondebugtool.common.ActionManager;
import com.afunx.actiondebugtool.data.FrameData;
import com.afunx.actiondebugtool.edit.adapter.FrameItemAdapter;
import com.afunx.actiondebugtool.save.SaveAsActivity;
import com.afunx.actiondebugtool.widget.SmartSeekBar;
import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotionBean;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, EditContract.View, SmartSeekBar.OnSmartSeekBarChangeListener {

    private final int ROBOT_PART_COUNT = 14;
    private final Button[] mRobotParts = new Button[ROBOT_PART_COUNT + 1];
    private Button mBtnFramesPlay;
    private Button mBtnFrameAdd;

    private RecyclerView mRycFrameItems;
    private FrameItemAdapter mAdapterFrameItems;
    private SmartSeekBar mSkbDeg;
    private SmartSeekBar mSkbRuntime;

    private String mMotionName;
    private EditContract.Presenter mEditPresenter;

    public void setPresenter(EditContract.Presenter presenter) {
        mEditPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        List<FrameData> frameDataList = getIntentFrameDataList();

        new EditPresenter(getApplicationContext(), this, frameDataList);

        getIntentIpAddress();

        initView(frameDataList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_enter_read_mode:
                mEditPresenter.enterReadMode();
                return true;
            case R.id.menu_item_read:
                mEditPresenter.readMotors();
                return true;
            case R.id.menu_item_copy:
                mEditPresenter.copySelectedFrame();
                return true;
            case R.id.menu_item_paste:
                mEditPresenter.pasteAfterSelected();
                return true;
            case R.id.menu_item_delete:
                mEditPresenter.deleteSelectedFrame();
                return true;
            case R.id.menu_item_save:
                doSave();
                return true;
            case R.id.menu_item_save_as:
                doSaveAs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doSave() {
        if (mMotionName == null) {
            doSaveAs();
        } else {
            // get frame bean list from mAdapterFrameItems(it is cloned from mAdapterFrameItems)
            List<FrameBean> frameBeanList = mAdapterFrameItems.getFrameBeanList();
            // create a MotionBean and add frameBeanList into it
            MotionBean motionBean = new MotionBean();
            motionBean.setName(mMotionName);
            motionBean.getFrameBeans().addAll(frameBeanList);
            ActionManager.get().writeAction(this, motionBean);
        }
    }

    private void doSaveAs() {

        Intent intent = new Intent(this, SaveAsActivity.class);

        // get frame bean list from mAdapterFrameItems(it is cloned from mAdapterFrameItems)
        List<FrameBean> frameBeanList = mAdapterFrameItems.getFrameBeanList();
        // create a MotionBean and add frameBeanList into it
        MotionBean motionBean = new MotionBean();
        motionBean.getFrameBeans().addAll(frameBeanList);

        intent.putExtra("action", motionBean);
        startActivity(intent);
    }

    private void getIntentIpAddress() {
        String ipAddr = getIntent().getStringExtra("ipAddr");
        if (ipAddr != null) {
            mEditPresenter.setRobotIpAddr(ipAddr);
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(ipAddr);
            }
        }
    }

    private List<FrameData> getIntentFrameDataList() {
        List<FrameData> frameDataList = new ArrayList<>();
        MotionBean motionBean = (MotionBean) getIntent().getSerializableExtra("action");
        if (motionBean != null) {
            List<FrameBean> frameBeans = motionBean.getFrameBeans();
            for (FrameBean frameBean : frameBeans) {
                FrameData frameData = new FrameData(frameBean);
                frameDataList.add(frameData);
            }
            mMotionName = motionBean.getName();
        }
        return frameDataList;
    }

    private void initView(List<FrameData> frameDataList) {
        // actionbar
        initViewActionBar();

        // robot part buttons
        initViewRobotPartButtons();

        // other buttons
        initOtherButtons();

        // frame recyclerView
        initFrameRecyclerView(frameDataList);

        // smart seek bar for degree and runtime
        initSmartSeekBars();
    }

    private void initViewActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewRobotPartButtons() {
        Button btnRobotPart01 = (Button) findViewById(R.id.btn_robot_part_01);
        btnRobotPart01.setOnClickListener(this);
        mRobotParts[1] = btnRobotPart01;

        Button btnRobotPart02 = (Button) findViewById(R.id.btn_robot_part_02);
        btnRobotPart02.setOnClickListener(this);
        mRobotParts[2] = btnRobotPart02;

        Button btnRobotPart03 = (Button) findViewById(R.id.btn_robot_part_03);
        btnRobotPart03.setOnClickListener(this);
        mRobotParts[3] = btnRobotPart03;

        Button btnRobotPart04 = (Button) findViewById(R.id.btn_robot_part_04);
        btnRobotPart04.setOnClickListener(this);
        mRobotParts[4] = btnRobotPart04;

        Button btnRobotPart05 = (Button) findViewById(R.id.btn_robot_part_05);
        btnRobotPart05.setOnClickListener(this);
        mRobotParts[5] = btnRobotPart05;

        Button btnRobotPart06 = (Button) findViewById(R.id.btn_robot_part_06);
        btnRobotPart06.setOnClickListener(this);
        mRobotParts[6] = btnRobotPart06;

        Button btnRobotPart07 = (Button) findViewById(R.id.btn_robot_part_07);
        btnRobotPart07.setOnClickListener(this);
        mRobotParts[7] = btnRobotPart07;

        Button btnRobotPart08 = (Button) findViewById(R.id.btn_robot_part_08);
        btnRobotPart08.setOnClickListener(this);
        mRobotParts[8] = btnRobotPart08;

        Button btnRobotPart09 = (Button) findViewById(R.id.btn_robot_part_09);
        btnRobotPart09.setOnClickListener(this);
        mRobotParts[9] = btnRobotPart09;

        Button btnRobotPart10 = (Button) findViewById(R.id.btn_robot_part_10);
        btnRobotPart10.setOnClickListener(this);
        mRobotParts[10] = btnRobotPart10;

        Button btnRobotPart11 = (Button) findViewById(R.id.btn_robot_part_11);
        btnRobotPart11.setOnClickListener(this);
        mRobotParts[11] = btnRobotPart11;

        Button btnRobotPart12 = (Button) findViewById(R.id.btn_robot_part_12);
        btnRobotPart12.setOnClickListener(this);
        mRobotParts[12] = btnRobotPart12;

        Button btnRobotPart13 = (Button) findViewById(R.id.btn_robot_part_13);
        btnRobotPart13.setOnClickListener(this);
        mRobotParts[13] = btnRobotPart13;

        Button btnRobotPart14 = (Button) findViewById(R.id.btn_robot_part_14);
        btnRobotPart14.setOnClickListener(this);
        mRobotParts[14] = btnRobotPart14;
    }


    private void initOtherButtons() {
        mBtnFramesPlay = (Button) findViewById(R.id.btn_frames_play);
        mBtnFramesPlay.setOnClickListener(this);

        mBtnFrameAdd = (Button) findViewById(R.id.btn_frame_add);
        mBtnFrameAdd.setOnClickListener(this);
    }

    private void initFrameRecyclerView(List<FrameData> frameDataList) {
        mRycFrameItems = (RecyclerView) findViewById(R.id.ryc_frame);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRycFrameItems.setLayoutManager(llm);
        mAdapterFrameItems = new FrameItemAdapter(mRycFrameItems, frameDataList, mEditPresenter);
        mRycFrameItems.setAdapter(mAdapterFrameItems);
    }

    private void initSmartSeekBars() {
        mSkbDeg = (SmartSeekBar) findViewById(R.id.smart_seek_bar_deg);
        mSkbDeg.setOnSmartSeekBarChangeListener(this);

        mSkbRuntime = (SmartSeekBar) findViewById(R.id.smart_seek_bar_runtime);
        mSkbRuntime.setOnSmartSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        // robot part buttons
        for (int i = 1; i < mRobotParts.length; i++) {
            if (v == mRobotParts[i]) {
                mEditPresenter.setSelectedMotor(i);
                return;
            }
        }
        if (v == mBtnFramesPlay) {
            // frames play button from selected frame
            mEditPresenter.playMotionFromSelectedFrame();
        } else if(v == mBtnFrameAdd) {
            // insert frame after selected frame
            mEditPresenter.insertFrameAfterSelected();
        }
    }


    @Override
    public void onSmartSeekBarChanged(View view, int value) {
        if (view == mSkbDeg) {
            mEditPresenter.setSelectedMotorDegree(value);
        } else if(view == mSkbRuntime) {
            mEditPresenter.setSelectedFrameRuntime(value);
        }
    }

    @Override
    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EditActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EditActivity.this, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getSelectedMotorId() {
        for (int i = 1; i < mRobotParts.length; i++) {
            if (mRobotParts[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedMotorId(int selectedMotorId) {
        if (selectedMotorId <= 0) {
            throw new IllegalArgumentException("selectedMotorId = " + selectedMotorId + " <= 0");
        }
        for (int i = 1; i < mRobotParts.length; i++) {
            if (mRobotParts[i].isSelected()) {
                mRobotParts[i].setSelected(false);
                break;
            }
        }
        mRobotParts[selectedMotorId].setSelected(true);
    }

    @Override
    public int getSelectedFrameIndex() {
        return mAdapterFrameItems.getSelectedIndex();
    }

    @Override
    public void setSelectedFrameIndex(int frameIndex) {
        mAdapterFrameItems.setSelectedIndex(frameIndex);
    }

    @Override
    public int getFrameCount() {
        return mAdapterFrameItems.getItemCount();
    }

    @Override
    public int getFrameRuntime() {
        return mSkbRuntime.getValue();
    }

    @Override
    public void setFrameRuntime(int runtime) {
        mSkbRuntime.setValue(runtime);
    }

    @Override
    public int getFrameRuntimeMin() {
        return mSkbRuntime.getMin();
    }

    @Override
    public void setFrameRuntimeMin(int runtimeMin) {
        mSkbRuntime.setMin(runtimeMin);
    }

    @Override
    public int getFrameRuntimeMax() {
        return mSkbRuntime.getMax();
    }

    @Override
    public void setFrameRuntimeMax(int runtimeMax) {
        mSkbRuntime.setMax(runtimeMax);
    }

    @Override
    public void updateFrame(int frameIndex) {
        mAdapterFrameItems.notifyItemChanged(frameIndex);
    }

    @Override
    public void insertFrame(int frameIndex) {
        mAdapterFrameItems.insertFrame(frameIndex);
    }

    @Override
    public void deleteFrame(int frameIndex) {
        mAdapterFrameItems.delete(frameIndex);
    }

    @Override
    public void copyFrame(int frameIndex) {
        mAdapterFrameItems.copy(frameIndex);
    }

    @Override
    public int getCopiedFrameIndex() {
        return mAdapterFrameItems.getCopiedFrameIndex();
    }

    @Override
    public void clearCopy() {
        mAdapterFrameItems.clearCopy();
    }

    @Override
    public void pasteAfterSelected() {
        mAdapterFrameItems.pasteAfterSelected();
    }

    @Override
    public int getMotorDeg() {
        return mSkbDeg.getValue();
    }

    @Override
    public void setMotorDeg(int deg) {
        mSkbDeg.setValue(deg);
    }

    @Override
    public int getMotorDegMin() {
        return mSkbDeg.getMin();
    }

    @Override
    public void setMotorDegMin(int degMin) {
        mSkbDeg.setMin(degMin);
    }

    @Override
    public int getMotorDegMax() {
        return mSkbDeg.getMax();
    }

    @Override
    public void setMotorDegMax(int degMax) {
        mSkbDeg.setMax(degMax);
    }

}