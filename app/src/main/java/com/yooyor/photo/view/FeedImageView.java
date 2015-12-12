package com.yooyor.photo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.yooyor.photo.DimenUtility;

/**
 * Created by xuefengyang on 2015/11/17.
 */
public class FeedImageView extends ImageView{
    public FeedImageView(Context context) {
        super(context);
    }
    public FeedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FeedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }   
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float parentWidth=(DimenUtility.getScreenWidth(getContext()))*0.9F;
        int totalWidth  = (int)Math.floor((parentWidth-4*DimenUtility.dp2px(getContext(),8)));
        setMeasuredDimension(totalWidth / 3, totalWidth / 3);
    }
}
