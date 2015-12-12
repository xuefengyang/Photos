package com.yooyor.photo;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by xuefengyang on 2015/11/17.
 */
public class DimenUtility {

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static float px2dp(Context context, int px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
