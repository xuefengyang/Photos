package com.yooyor.photo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xuefengyang on 2015/11/17.
 */
public class TimeLineView extends View {

    private Paint mPaint;
    public TimeLineView(Context context) {
        this(context,null);
    }
    public TimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }
    private void initialize(){
        mPaint =new Paint();
        mPaint.setColor(Color.BLUE);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(getMeasuredWidth()/2,0,getMeasuredWidth()/2+5,getMeasuredHeight(),mPaint);

    }
}
