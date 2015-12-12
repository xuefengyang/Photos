package com.yooyor.media.view;

import android.content.Context;

import com.yooyor.media.model.entity.Photo;
import com.yooyor.media.model.entity.PhotoFolder;

import java.util.List;

/**
 * Created by xuefengyang on 2015/11/21.
 */
public interface IShowPhotoView {

    void onShowProgressbar();

    void onDismissProgressbar();

    void onShowPhotoFolder(List<PhotoFolder> folders);

    void onShowPhotoGrid(List<Photo> photos);

    void onFailure(String msg);

    Context getContext();

}
