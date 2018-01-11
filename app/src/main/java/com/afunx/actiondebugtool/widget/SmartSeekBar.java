package com.afunx.actiondebugtool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.afunx.actiondebugtool.R;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * SmartSeekBar is a smart seek bar which support seekBar, add button(including long press), subtract button(including long press) and edit text
 * <p>
 * Created by afunx on 10/01/2018.
 */

public class SmartSeekBar extends PercentRelativeLayout
        implements View.OnClickListener, View.OnLongClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener, FitHeightEditView.OnAlertDialogTextChangedListener {

    /**
     * SmartSeekBar change listener
     * <p>
     * it will be called on SmartSeekBar value changed in any ways
     */
    public interface OnSmartSeekBarChangeListener {
        void onSmartSeekBarChanged(int value);
    }

    private static final String TAG = "SmartSeekBar";

    private static final boolean DEBUG = false;

    /**
     * SeekBar
     */
    private SeekBar mSeekBar;
    /**
     * OnSmartSeekBarChangeListener will be called when SeekBar value is changed in any ways
     */
    private OnSmartSeekBarChangeListener mOnSmartSeekBarChangeListener;
    /**
     * seek bar min value from attrs
     */
    private final int seekBarMin;
    /**
     * seek bar max value from attrs
     */
    private final int seekBarMax;
    /**
     * button for subtracting value(support long press)
     */
    private final Button mBtnValueSub;
    /**
     * button for adding value(support long press)
     */
    private final Button mBtnValueAdd;
    /**
     * edit text support update by seekBar, add button, subtract button and edit
     */
    private final FitHeightEditView mEdtValue;

    /**
     * last progress from user
     */
    private int _progress;

    /**
     * last long press value
     */
    private int _value;

    /**
     * message.what for add value when mBtnValueAdd is long pressed
     */
    private static final int MSG_ADD = 1;

    /**
     * message.what for subtract value when mBtnValueSubtract is long pressed
     */
    private static final int MSG_SUB = -1;

    /**
     * is add button or subtract button long pressed now
     */
    private boolean mIsLongPressed = false;

    /**
     * Handler for update mEdtValue when mBtnValueSub or mBtnValueAdd are long pressed
     */
    private UpdateHandler mUpdateHandler;

    public SmartSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // inflate layout
        LayoutInflater.from(context).inflate(R.layout.smart_seek_bar, this, true);
        mBtnValueSub = (Button) findViewById(R.id.btn_subtract_smart_seek_bar);
        mBtnValueAdd = (Button) findViewById(R.id.btn_add_smart_seek_bar);
        mSeekBar = (SeekBar) findViewById(R.id.skb_smart_seek_bar);
        mEdtValue = (FitHeightEditView) findViewById(R.id.edt_smart_seek_bar);

        // parse attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartSeekBar);

        this.seekBarMin = a.getInt(R.styleable.SmartSeekBar_smart_seek_bar_min, -1);
        if (this.seekBarMin == -1) {
            throw new IllegalArgumentException("smart_seek_bar_min should be set");
        }
        this.seekBarMax = a.getInt(R.styleable.SmartSeekBar_smart_seek_bar_max, -1);
        if (this.seekBarMax == -1) {
            throw new IllegalArgumentException("smart_seek_bar_max should be set");
        }
        if (this.seekBarMin > this.seekBarMax) {
            throw new IllegalArgumentException("smart_seek_bar_max should be >= smart_seek_bar_min");
        }
        if (DEBUG) {
            Log.d(TAG, "seekBarMin: " + this.seekBarMin + ", seekBarMax: " + this.seekBarMax);
        }
        a.recycle();

        // init View
        initView();

        // init handler
        mUpdateHandler = new UpdateHandler(this);
    }

    private void initView() {
        mBtnValueSub.setOnClickListener(this);
        mBtnValueSub.setOnLongClickListener(this);
        mBtnValueSub.setOnTouchListener(this);
        mBtnValueAdd.setOnClickListener(this);
        mBtnValueAdd.setOnLongClickListener(this);
        mBtnValueAdd.setOnTouchListener(this);
        mEdtValue.setText(String.format(Locale.US, "%d", seekBarMin));
        mEdtValue.setOnAlertDialogTextChangedListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(seekBarMax - seekBarMin);
    }

    public void setOnSmartSeekBarChangeListener(OnSmartSeekBarChangeListener onSmartSeekBarChangeListener) {
        mOnSmartSeekBarChangeListener = onSmartSeekBarChangeListener;
    }

    /**
     * change value to progress
     *
     * @param value value
     * @return progress
     */
    private int value2progress(int value) {
        return value - seekBarMin;
    }

    /**
     * change progress to value
     *
     * @param progress progress
     * @return value
     */
    private int progress2value(int progress) {
        return progress + seekBarMin;
    }

    /**
     * constraint value in [seekBarMin, seekBarMax]
     *
     * @param value value to be constraint
     * @return constraint value
     */
    private int constraintValue(int value) {
        if (value < seekBarMin) {
            Log.i(TAG, "constraintValue() value: " + value + " to seekBarMin: " + seekBarMin);
            return seekBarMin;
        } else if (value > seekBarMax) {
            Log.i(TAG, "constraintValue() value: " + value + " to seekBarMax: " + seekBarMax);
            return seekBarMax;
        } else {
            return value;
        }
    }

    /**
     * get value from EditText
     *
     * @return value
     */
    private int getEditTextValue() {
        return Integer.parseInt(mEdtValue.getText().toString());
    }

    /**
     * set value to EditText
     *
     * @param value the value to be set
     */
    private void setEditTextValue(int value) {
        mEdtValue.setText(String.format(Locale.US, "%d", value));
    }

    /**
     * get value from SeekBar
     *
     * @return value
     */
    private int getSeekBarValue() {
        int progress = mSeekBar.getProgress();
        return progress2value(progress);
    }

    /**
     * set value to SeekBar
     *
     * @param value the value to be set
     */
    private void setSeekBarValue(int value) {
        mSeekBar.setProgress(value2progress(value));
    }

    /**
     * set value by button
     *
     * @param delta delta of value
     */
    private void setValueByButton(int delta) {
        int value1 = getEditTextValue();
        int value2 = getSeekBarValue();
        if (value1 != value2) {
            throw new IllegalStateException("value1: " + value1 + ", value2: " + value2 + ", they should be equal!");
        }
        updateValue(value1 + delta, CHANGE_SOURCE.FROM_BUTTON);
    }

    private enum CHANGE_SOURCE {
        FROM_SEEK_BAR,
        FROM_EDIT_TEXT,
        FROM_BUTTON
    }

    /**
     * update value
     *
     * @param value the value to be updated
     */
    private void updateValue(int value, CHANGE_SOURCE changeSource) {
        if (DEBUG) {
            Log.d(TAG, "updateValue() value: " + value + ", changeSource: " + changeSource.toString());
        }
        // constraint value in [seekBarMin, seekBarMax]
        value = constraintValue(value);

        switch (changeSource) {
            case FROM_SEEK_BAR:
                setEditTextValue(value);
                break;
            case FROM_EDIT_TEXT:
                setSeekBarValue(value);
                break;
            case FROM_BUTTON:
                setEditTextValue(value);
                setSeekBarValue(value);
                break;
        }

        if (mIsLongPressed) {
            _value = value;
        } else {
            triggerOnSmartSeekBarChangeListener(value);
        }
    }

    /**
     * trigger OnSmartSeekBarChangeListener
     *
     * @param value the value to be triggered
     */
    private void triggerOnSmartSeekBarChangeListener(int value) {
        if (DEBUG) {
            Log.d(TAG, "triggerOnSmartSeekBarChangeListener() value: " + value);
        }
        if (mOnSmartSeekBarChangeListener != null) {
            mOnSmartSeekBarChangeListener.onSmartSeekBarChanged(value);
        }
    }

    @Override
    public void onAlertDialogTextChanged(CharSequence text) {
        if (DEBUG) {
            Log.d(TAG, "onAlertDialogTextChanged() text: " + text);
        }
        int oldValue = Integer.parseInt(text.toString());
        int newValue = constraintValue(oldValue);
        if (oldValue != newValue) {
            setEditTextValue(newValue);
        }
        updateValue(newValue, CHANGE_SOURCE.FROM_EDIT_TEXT);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnValueSub) {
            setValueByButton(-1);
        } else if (v == mBtnValueAdd) {
            setValueByButton(1);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (DEBUG) {
            Log.d(TAG, "onTouch()");
        }
        if (mIsLongPressed) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mIsLongPressed = false;
                triggerOnSmartSeekBarChangeListener(_value);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        if (DEBUG) {
            Log.d(TAG, "onLongClick()");
        }
        if (v == mBtnValueAdd) {
            mIsLongPressed = true;
            Message message = Message.obtain();
            message.what = MSG_ADD;
            mUpdateHandler.sendMessage(message);
            return true;
        } else if (v == mBtnValueSub) {
            mIsLongPressed = true;
            Message message = Message.obtain();
            message.what = MSG_SUB;
            mUpdateHandler.sendMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (DEBUG) {
            Log.d(TAG, "onProgressChanged() progress: " + progress + ", fromUser: " + fromUser);
        }
        if (fromUser) {
            // only update EditText value, updateValue() at onStopTrackingTouch()
            setEditTextValue(progress2value(progress));
            _progress = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (DEBUG) {
            Log.d(TAG, "onStartTrackingTouch()");
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int value = progress2value(_progress);
        if (DEBUG) {
            Log.d(TAG, "onStopTrackingTouch() value: " + value);
        }
        updateValue(value, CHANGE_SOURCE.FROM_SEEK_BAR);
    }

    private static class UpdateHandler extends Handler {

        private WeakReference<SmartSeekBar> mSmartSeekBar;

        private UpdateHandler(SmartSeekBar smartSeekBar) {
            mSmartSeekBar = new WeakReference<>(smartSeekBar);
        }

        @Override
        public void handleMessage(Message msg) {
            if (DEBUG) {
                Log.d(TAG, "UpdateHandler handleMessage() msg.what: " + msg.what);
            }
            SmartSeekBar smartSeekBar = mSmartSeekBar.get();
            if (smartSeekBar != null) {
                if (smartSeekBar.mIsLongPressed) {
                    switch (msg.what) {
                        case MSG_ADD:
                            handleMessageAdd(msg);
                            break;
                        case MSG_SUB:
                            handleMessageSub(msg);
                            break;
                    }
                }
            }
        }

        private void handleMessageAdd(Message msg) {
            SmartSeekBar smartSeekBar = mSmartSeekBar.get();
            if (smartSeekBar != null) {
                smartSeekBar.setValueByButton(1);
                sendMessage(Message.obtain(msg));
            }
        }

        private void handleMessageSub(Message msg) {
            SmartSeekBar smartSeekBar = mSmartSeekBar.get();
            if (smartSeekBar != null) {
                smartSeekBar.setValueByButton(-1);
                sendMessage(Message.obtain(msg));
            }
        }

    }
}