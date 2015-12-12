package com.yooyor.photo.test;

import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import com.yooyor.photo.DimenUtility;
import com.yooyor.photo.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }
    public void startAnimation(View v){
        MyAnimation animation =new MyAnimation(0, DimenUtility.getScreenWidth(this)*2/3,0,DimenUtility.getScreenHeight(this)/4,0,0);
        animation.setDuration(2000);
        animation.setInterpolator(new AnticipateOvershootInterpolator());
        v.startAnimation(animation);
    }
    public static class MyAnimation extends Animation{
        private int mFromX;
        private int mToX;

        private int mFromY;
        private int mToY;

        private int mCenterX;
        private int mCenterY;

        private Camera mCamera;

        public MyAnimation(int mFromX, int mToX, int mFromY, int mToY, int mCenterX, int mCenterY) {
            this.mFromX = mFromX;
            this.mToX = mToX;
            this.mFromY = mFromY;
            this.mToY = mToY;
            this.mCenterX = mCenterX;
            this.mCenterY = mCenterY;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera =new Camera();
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float dx =mFromX+(mToX-mFromX)*interpolatedTime;
            float dy =mFromY+(mToY-mFromY)*interpolatedTime;

            t.getMatrix().setTranslate(dx,dy);

        }
    }
}
