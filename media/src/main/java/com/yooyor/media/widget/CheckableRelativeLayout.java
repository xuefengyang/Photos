package com.yooyor.media.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Created by xuefengyang on 2015/11/22.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private final static  int [] CHECKED_STATE_SET={android.R.attr.state_checked};
    private boolean mChecked;
    public CheckableRelativeLayout(Context context) {
        super(context);
    }
    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        if(checked!=mChecked){
            mChecked=checked;
            refreshDrawableState();
        }
    }
    @Override
    public boolean isChecked() {
        return mChecked;
    }
    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace+1);
        if(isChecked()){
            mergeDrawableStates(drawableState,CHECKED_STATE_SET);
        }
        return drawableState;

    }
}
