package com.afunx.actiondebugtool.edit;

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
import com.afunx.actiondebugtool.main.adapter.FrameItemAdapter;
import com.afunx.actiondebugtool.widget.SmartSeekBar;
import com.afunx.data.bean.FrameBean;
import com.afunx.data.bean.MotorBean;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, EditContract.View, SmartSeekBar.OnSmartSeekBarChangeListener {

    private final int ROBOT_PART_COUNT = 14;
    private final Button[] mRobotParts = new Button[ROBOT_PART_COUNT + 1];

    private RecyclerView mRycFrameItems;
    private FrameItemAdapter mAdapterFrameItems;
    private SmartSeekBar mSkbDeg;
    private SmartSeekBar mSkbRuntime;

    private EditContract.Presenter mEditPresenter;

    public void setPresenter(EditContract.Presenter presenter) {
        mEditPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        new EditPresenter(getApplicationContext(), this);

        initView();
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
                Toast.makeText(this, R.string.enter_read_mode, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_read:
                Toast.makeText(this, R.string.read, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_copy:
                Toast.makeText(this, R.string.copy, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_paste:
                Toast.makeText(this, R.string.paste, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_delete:
                mEditPresenter.deleteSelectedFrame();
                return true;
            case R.id.menu_item_save:
                Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_save_as:
                Toast.makeText(this, R.string.save_as, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        // actionbar
        initViewActionBar();

        // robot part buttons
        initViewRobotPartButtons();

        // frame recyclerView
        initFrameRecyclerView();

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

    private void initFrameRecyclerView() {
        mRycFrameItems = (RecyclerView) findViewById(R.id.ryc_frame);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRycFrameItems.setLayoutManager(llm);
        mAdapterFrameItems = new FrameItemAdapter(mockFrameItems(), mEditPresenter);
        mRycFrameItems.setAdapter(mAdapterFrameItems);
    }

    private void initSmartSeekBars() {
        mSkbDeg = (SmartSeekBar) findViewById(R.id.smart_seek_bar_deg);
        mSkbDeg.setOnSmartSeekBarChangeListener(this);
        mSkbRuntime = (SmartSeekBar) findViewById(R.id.smart_seek_bar_runtime);
        mSkbRuntime.setOnSmartSeekBarChangeListener(this);
    }

    private List<FrameBean> mockFrameItems() {
        final int motorCount = 14;
        final int frameCount = 20;
        List<FrameBean> frameBeanList = new ArrayList<>();
        for (int i = 0; i < frameCount; i++) {
            FrameBean frameBean = new FrameBean();
            for (int j = 0; j < motorCount; j++) {
                MotorBean motorBean = new MotorBean();
                motorBean.setId(j + 1);
                motorBean.setDeg(0);
                frameBean.getMotorBeans().add(motorBean);
            }
            frameBean.setTime(1000);
            frameBean.setName("item " + i);
            frameBeanList.add(frameBean);
        }
        return frameBeanList;
    }

    @Override
    public void onClick(View v) {
        for (int i = 1; i < mRobotParts.length; i++) {
            if (v == mRobotParts[i]) {
                setSelectedMotorId(i);
                return;
            }
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
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
    public void deleteFrame(int frameIndex) {
        mAdapterFrameItems.delete(frameIndex);
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