package com.afunx.actiondebugtool.edit;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.afunx.actiondebugtool.R;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, EditContract.View {

    private final int ROBOT_PART_COUNT = 14;
    private final Button[] mRobotParts = new Button[ROBOT_PART_COUNT + 1];
    private EditContract.Presenter mPresenter;

    @Override
    public void setPresenter(EditContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

        new EditPresenter(EditModel.load(this), this);
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
                Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        for (int i = 1; i < mRobotParts.length; i++) {
            if (v == mRobotParts[i]) {
                Toast.makeText(this, "robot part " + i + " is clicked", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

}