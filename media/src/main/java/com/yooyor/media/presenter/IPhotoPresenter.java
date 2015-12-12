package com.yooyor.media.presenter;

import com.yooyor.media.model.entity.PhotoFolder;

/**
 * Created by xuefengyang on 2015/11/21.
 */
public interface IPhotoPresenter {
    void start();
    void end();
    void showPhotosByFolder(PhotoFolder folder);
    void showPhotosByPosition(int position);
    void showPhotosAll();

}
