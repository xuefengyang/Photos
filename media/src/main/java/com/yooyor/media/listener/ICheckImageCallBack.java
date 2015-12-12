package com.yooyor.media.listener;

import android.util.SparseArray;

/**
 * Created by xuefengyang on 2015/11/22.
 */
public interface ICheckImageCallBack {

    void onCheckImageUrl(SparseArray sa);
    void onCheckCount(int count);
    void onCheckCameraItem();
}
