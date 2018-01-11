package com.afunx.actiondebugtool.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.afunx.actiondebugtool.R;

/**
 * Created by afunx on 10/01/2018.
 */

public class FitHeightEditView extends FitHeightTextView implements View.OnClickListener {

    /**
     * Listener for EditView changing text by alert dialog
     */
    public interface OnAlertDialogTextChangedListener {
        void onAlertDialogTextChanged(CharSequence text);
    }

    private final int inputType;
    private final String title;

    public FitHeightEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FitHeightEditView);
        inputType = a.getInt(R.styleable.FitHeightEditView_input_type, 1);
        title = a.getString(R.styleable.FitHeightEditView_alert_title);
        a.recycle();
    }

    private OnAlertDialogTextChangedListener mOnAlertDialogTextChangedListener;

    public void setOnAlertDialogTextChangedListener(OnAlertDialogTextChangedListener onAlertDialogTextChangedListener) {
        mOnAlertDialogTextChangedListener = onAlertDialogTextChangedListener;
    }

    @Override
    public void onClick(View v) {
        final Context context = this.getContext();
        final EditText editText = new EditText(context);
        editText.setInputType(inputType);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setText(editText.getText());
                        if (mOnAlertDialogTextChangedListener != null) {
                            mOnAlertDialogTextChangedListener.onAlertDialogTextChanged(editText.getText());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
