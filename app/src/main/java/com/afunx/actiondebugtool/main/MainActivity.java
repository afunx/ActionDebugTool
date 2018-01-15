package com.afunx.actiondebugtool.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afunx.actiondebugtool.R;
import com.afunx.actiondebugtool.common.ActionManager;
import com.afunx.actiondebugtool.main.adapter.ActionItemAdapter;
import com.afunx.client.impl.UdpDiscoverClientImpl;
import com.afunx.client.interfaces.UdpDiscoverClient;
import com.afunx.data.bean.MotionBean;
import com.afunx.permission.PermissionHelper;
import com.afunx.permission.PermissionListener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView mTvRobotIp;
    private Button mBtnScan;
    private Button mBtnAdd;
    private InetAddress mRobotInetAddr;

    private final List<MotionBean> mMotionBeanList = new ArrayList<>();
    private RecyclerView mRcyAction;
    private ActionItemAdapter mAdapterAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    /**
     * request permissions required util user allow all permissions
     */
    private void requestPermissions() {
        final PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGrantedAll() {
            }

            @Override
            public void onPermissionDenied(final List<String> deniedPermissionList) {
                PermissionHelper.requestPermissions(1, this, MainActivity.this);
            }
        };
        PermissionHelper.requestPermissions(1, permissionListener, this);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        mTvRobotIp = (TextView) findViewById(R.id.tv_robot_ip);
        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mBtnScan.setOnClickListener(this);
        mBtnAdd = (Button) findViewById(R.id.btn_add_new_action);
        mBtnAdd.setOnClickListener(this);
        mRcyAction = (RecyclerView) findViewById(R.id.ryc_action);
        mAdapterAction = new ActionItemAdapter(mMotionBeanList);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcyAction.setLayoutManager(llm);
        mRcyAction.setAdapter(mAdapterAction);
    }

    private void initData() {
        // clear previous motion bean list
        mMotionBeanList.clear();
        // init data from shared preferences
        List<MotionBean> motionBeanList = ActionManager.get().readActionList(this);
        if (!motionBeanList.isEmpty()) {
            mMotionBeanList.addAll(motionBeanList);
            mAdapterAction.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnScan) {
            doScan();
        } else if (v == mBtnAdd) {
            doAdd();
        }
    }

    private void doScan() {
        Log.i(TAG, "doScan()");
        new ScanTask().execute();
    }

    private void doAdd() {
        Log.i(TAG, "doAdd()");
        Intent intent = new Intent("com.afunx.actiondebugtool.editAction");
        startActivity(intent);
    }

    private class ScanTask extends AsyncTask<Void, Void, List<InetAddress>> {

        private final byte[] secret = new byte[]{0x75, 0x62, 0x74};
        private final int port = 32866;
        private final int soTimeout = 2000;

        @Override
        protected void onPreExecute() {
            mBtnScan.setText(R.string.scanning);
            mBtnScan.setEnabled(false);
        }

        @Override
        protected List<InetAddress> doInBackground(Void... params) {
            // discover robot
            UdpDiscoverClient udpDiscoverClient = new UdpDiscoverClientImpl();
            return udpDiscoverClient.discover(secret, port, soTimeout);
        }

        private void selectInetAddressed(final List<InetAddress> inetAddresses, final int index) {
            mRobotInetAddr = inetAddresses.get(index);
            mTvRobotIp.setText(mRobotInetAddr.getHostAddress());
        }

        private void clearInetAddres() {
            mRobotInetAddr = null;
            mTvRobotIp.setText("");
            Toast.makeText(MainActivity.this, R.string.no_robot_found, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(final List<InetAddress> inetAddresses) {

            final int size = inetAddresses.size();
            if (size > 0) {
                if (size == 1) {
                    // only one robot is found
                    selectInetAddressed(inetAddresses, 0);
                    String message = MainActivity.this.getResources().getString(R.string.one_robot_found);
                    message = String.format(message, inetAddresses.get(0).getHostAddress());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    // select robot ip address
                    final String[] ipAddrs = new String[size];
                    for (int i = 0; i < size; i++) {
                        ipAddrs[i] = inetAddresses.get(i).getHostAddress();
                    }
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.select_robot_ip)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setSingleChoiceItems(ipAddrs, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectInetAddressed(inetAddresses, which);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
            } else {
                clearInetAddres();
            }
            mBtnScan.setText(R.string.scan);
            mBtnScan.setEnabled(true);
        }
    }
}
