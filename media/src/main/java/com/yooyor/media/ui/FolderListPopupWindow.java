package com.yooyor.media.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yooyor.media.R;
import com.yooyor.media.adapter.PhotoFolderAdapter;
import com.yooyor.media.model.entity.PhotoFolder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by xuefengyang on 2015/11/21.
 */
public class FolderListPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private ViewGroup mWrapper;
    private ListView mFolderListView;
    private List<PhotoFolder> mDataList;
    private Context mContext;
    private boolean isRunDismissAnimation=false;
    private OnSelectFolderListener mListener;

    private final static int ANIMATION_DURATION=300;

    public interface OnSelectFolderListener{
        void onSelectFolder(PhotoFolder folder);
    }
    public FolderListPopupWindow(Context context, List<PhotoFolder> dataList,OnSelectFolderListener listener) {
        super(context);
        mDataList = dataList;
        mContext = context;
        mListener =listener;
        initializeView(context);
        setupData();
    }
    private void initializeView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_folder_list, null);
        mWrapper = (ViewGroup) view.findViewById(R.id.layout_photo_folder);
        mFolderListView = (ListView) view.findViewById(R.id.list_photo_folder);
        mFolderListView.setOnItemClickListener(this);
        mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(view);
        setFocusable(false);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
        final int[] size = calcSize();
        setWidth(size[0]);
        setHeight(size[1]);
    }
    private void setupData() {
        PhotoFolderAdapter adapter = new PhotoFolderAdapter(mDataList, mContext);
        mFolderListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mFolderListView.setAdapter(adapter);
        mFolderListView.setItemChecked(0,true);
        mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null&&mDataList!=null) {
                    mListener.onSelectFolder(mDataList.get(position));
                    dismiss();
                }
            }
        });
    }
    @Override
    public void dismiss() {
        if(isRunDismissAnimation){
            return ;
        }
        int colorFrom=0x77000000;
        int colorTo=0x00000000;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mWrapper.setBackgroundColor((Integer)animator.getAnimatedValue());
            }   
        });
        colorAnimation.setDuration(ANIMATION_DURATION);
        colorAnimation.start();

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(ANIMATION_DURATION);
        animation.setFillAfter(true);
        mFolderListView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isRunDismissAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                isRunDismissAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FolderListPopupWindow.super.dismiss();
                isRunDismissAnimation = false;
            }
        });
    }
    public void show(View v) {
        TranslateAnimation translateAnimation =
                new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f);
        mFolderListView.startAnimation(translateAnimation);

        showAtLocation(v, Gravity.NO_GRAVITY, 0, getActionbarheight()+getStatusBarHeight());
        int colorFrom=0x00000000;
        int colorTo  =0x77000000;
        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWrapper.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
        TranslateAnimation translateAnimation2 =
                new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF,
                        0, Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0);
        translateAnimation2.setDuration(ANIMATION_DURATION);
        mFolderListView.startAnimation(translateAnimation2);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * height =decorview.getHeight-topbar-bottom-bar
     *
     * @return
     */
    private int[] calcSize() {
        int[] size = new int[2];
        final View contentView = ((AppCompatActivity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        size[0] = contentView.getWidth();
        int btnHeight = dp2px(48);
        size[1] = contentView.getHeight() - getActionbarheight() - btnHeight;
        return size;
    }

    private int getActionbarheight(){
        int height=((AppCompatActivity) mContext).getSupportActionBar().getHeight();
        return height;
    }

    private int dp2px(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, mContext.getResources().getDisplayMetrics()));
    }

    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = mContext.getResources().getDimensionPixelSize(x);
            Log.d("TAG","sbar: "+sbar);
        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

}
