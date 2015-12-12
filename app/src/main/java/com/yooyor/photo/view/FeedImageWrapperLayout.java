package com.yooyor.photo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yooyor.photo.DimenUtility;
import com.yooyor.photo.R;

/**
 * Created by xuefengyang on 2015/11/17.
 *
 * 省略 margin和padding的定义改为
 */
public class FeedImageWrapperLayout extends ViewGroup {

    private int     mDividerPadding;
    private boolean mIsPadding;
    private boolean mIsPaddingLeft;
    private boolean mIsPaddingRight;
    private boolean mIsPaddingTop;
    private boolean mIsPaddingBottom;

    public FeedImageWrapperLayout(Context context) {
        this(context,null);
    }
    public FeedImageWrapperLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public FeedImageWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FeedImageWrapperLayout);
        mDividerPadding =a.getDimensionPixelSize(R.styleable.FeedImageWrapperLayout_itemDividerPadding,DimenUtility.dp2px(getContext(),8));
        mIsPadding      =a.getBoolean(R.styleable.FeedImageWrapperLayout_isPadding,true);
        mIsPaddingLeft  =a.getBoolean(R.styleable.FeedImageWrapperLayout_isPaddingLeft,true);
        mIsPaddingTop   =a.getBoolean(R.styleable.FeedImageWrapperLayout_isPaddingTop,true);
        mIsPaddingRight =a.getBoolean(R.styleable.FeedImageWrapperLayout_isPaddingRight,true);
        mIsPaddingBottom=a.getBoolean(R.styleable.FeedImageWrapperLayout_isPaddingBottom,true);
        a.recycle();
    }
    private void initialize(){
        mDividerPadding =DimenUtility.dp2px(getContext(),8);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireWidth  =0;
        int desireHeight =0;
        int padding      =mIsPadding?mDividerPadding:0;
        int paddingTop   =mIsPaddingTop?mDividerPadding:0;;
        int paddingBottom=mIsPaddingBottom?mDividerPadding:0;;
        int paddingLeft  =mIsPaddingLeft?mDividerPadding:0;;
        int paddingRight =mIsPaddingRight?mDividerPadding:0;;
        if(mIsPadding){
            padding=mDividerPadding;
        }
        int childCount   = getChildCount();
        int childVisibleCount=0;
        int visibleChildIndex=0;
        for (int i = 0; i < childCount; i++){
            View child = getChildAt(i);
            if(child.getVisibility()!=View.GONE){
                childVisibleCount++;
                measureChild(child,widthMeasureSpec,heightMeasureSpec);
                visibleChildIndex=i;
            }
        }
        if(childCount>=3){
            desireWidth=paddingLeft+paddingRight+2*padding+2*getChildAt(visibleChildIndex).getMeasuredWidth();
        }else{
            desireWidth=paddingLeft+paddingRight+padding+getChildAt(visibleChildIndex).getMeasuredWidth();
        }
        int  rows = (int)Math.ceil(childVisibleCount / 3d);
        desireHeight =paddingTop+paddingBottom+padding*(rows-1)+rows*getChildAt(visibleChildIndex).getMeasuredHeight();

        setMeasuredDimension(resolveSizeAndState(desireWidth,widthMeasureSpec,0),resolveSizeAndState(desireHeight,heightMeasureSpec,0));

    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int padding      =mIsPadding?mDividerPadding:0;
        int paddingTop   =mIsPaddingTop?mDividerPadding:0;
        int paddingLeft  =mIsPaddingLeft?mDividerPadding:0;

        int inCrementHorizontal=0;
        int inCrementVertical  =0;

        int childVisibleCount  =0;
        for (int i = 0; i <childCount; i++) {
            final View child = getChildAt(i);
            if(child.getVisibility()!=View.GONE){
                childVisibleCount++;
                l =paddingLeft+inCrementHorizontal;
                t =paddingTop+inCrementVertical;
                r =l+child.getMeasuredWidth();
                b =t+child.getMeasuredHeight();
                child.layout(l, t, r, b);

                inCrementHorizontal+=padding+child.getMeasuredWidth();
                if(childVisibleCount>=3&&childVisibleCount%3==0){
                    inCrementHorizontal =0;
                    inCrementVertical+=padding+child.getMeasuredHeight();
                }
            }
        }

    }

}
