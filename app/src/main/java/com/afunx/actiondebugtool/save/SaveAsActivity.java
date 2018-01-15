package com.afunx.actiondebugtool.save;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afunx.actiondebugtool.R;
import com.afunx.actiondebugtool.common.ActionManager;
import com.afunx.actiondebugtool.main.MainActivity;
import com.afunx.data.bean.MotionBean;

public class SaveAsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnConfirm;
    private Button mBtnCancel;

    private EditText mEdtFilename;

    private MotionBean mMotionBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_as);

        initView();

        mMotionBean = getIntentMotion();
    }

    private MotionBean getIntentMotion() {
        return (MotionBean) getIntent().getSerializableExtra("action");
    }

    private void initView() {
        mBtnConfirm = (Button) findViewById(R.id.btn_save_as_confirm);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.btn_save_as_cancel);
        mBtnCancel.setOnClickListener(this);
        mEdtFilename = (EditText) findViewById(R.id.edt_filename_save_as);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnConfirm) {
            doConfirm();
        } else if (v == mBtnCancel) {
            doCancel();
        }
    }

    private void doConfirm() {
        String filename = mEdtFilename.getText().toString();
        if (TextUtils.isEmpty(filename)) {
            // file name can't be empty
            Toast.makeText(this, R.string.filename_cannot_empty, Toast.LENGTH_SHORT).show();
        } else {
            ActionManager actionManager = ActionManager.get();
            if (actionManager.isActionNameExistInSp(this, filename)) {
                // file name has exist in SharedPreference already
                Toast.makeText(this, R.string.filename_has_exist, Toast.LENGTH_SHORT).show();
            } else {
                mMotionBean.setName(filename);
                // write file in SharedPreference
                actionManager.writeAction(this, mMotionBean);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void doCancel() {
        finish();
    }

}
