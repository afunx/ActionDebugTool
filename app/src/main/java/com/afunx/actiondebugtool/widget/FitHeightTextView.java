package com.afunx.actiondebugtool.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;

/**
 * it will make text most large to fit TextView height
 * <p>
 * refer to:
 * http://blog.csdn.net/qq_27489007/article/details/78063346
 * <p>
 * Created by afunx on 10/01/2018.
 */

public class FitHeightTextView extends AppCompatTextView {

    private static final String TAG = "FitHeightTextView";
    private static final boolean DEBUG = false;
    /**
     * whether set FitHeigtTextView biggest or not,
     * if BIGGEST = false, FitHeightTextView is just the same as AppCompatTextView
     */
    private static final boolean BIGGEST = false;
    private Paint mTextPaint;
    private final float minTextSizePx = 8;
    private final float maxTextSizePx = 1024;

    public FitHeightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // set gravity center
        setGravity(Gravity.CENTER);
        // set lines 1, only one line
        setLines(1);
        // deep copy TextPaint
        mTextPaint = new TextPaint(getPaint());
    }

    private float px2sp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (px / scale);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        refitText(text.toString(), this.getHeight());
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (h != oldh) {
            refitText(this.getText().toString(), h);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void refitText(String string, int height) {
        if (!BIGGEST) {
            return;
        }
        if (height > 0 && string.length() > 0) {
            // binary search to find the largest text to fit height
            final int fitTextSizePx = findTextSize(height, minTextSizePx, maxTextSizePx) - 20;
            if (fitTextSizePx == -1) {
                throw new IllegalStateException("fitTextSizePx is -1");
            }
            setTextSize(px2sp(getContext(), fitTextSizePx));
        }
    }

    /**
     * find the largest text by binary search
     *
     * @param height        TextView height
     * @param minTextSizePx min text size
     * @param maxTextSizePx max text size
     * @return fit text size
     */
    private int findTextSize(int height, float minTextSizePx, float maxTextSizePx) {
        float min = minTextSizePx;
        float max = maxTextSizePx;
        float mid = (min + max) / 2;
        while (min + 1 <= max) {
            if (DEBUG) {
                Log.d(TAG, "min: " + min + ", max: " + max + ", mid: " + mid);
            }
            if (isTextFit(height, mid)) {
                min = mid;
            } else {
                max = mid;
            }
            mid = (min + max) / 2;
        }
        return (int) min;
    }

    private boolean isTextFit(int height, float textSizePx) {
        mTextPaint.setTextSize(textSizePx);
        final int available = height - this.getPaddingTop() - this.getPaddingBottom();
        return mTextPaint.getFontSpacing() <= available;
    }
}