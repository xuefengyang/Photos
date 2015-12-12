package com.yooyor.media.presenter;

import android.content.Intent;
import android.util.Log;

import com.yooyor.media.listener.IQueryMediaCallBack;
import com.yooyor.media.model.QueryMediaHelper;
import com.yooyor.media.model.entity.Photo;
import com.yooyor.media.model.entity.PhotoFolder;
import com.yooyor.media.view.IShowPhotoView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuefengyang on 2015/11/21.
 */
public class PhotoPresenterImpl implements IPhotoPresenter,IQueryMediaCallBack {

    private IShowPhotoView mShowPhotoView;
    private List<PhotoFolder> mFolders;
    private Map<PhotoFolder,List<Photo>> mPhotos;
    public PhotoPresenterImpl(IShowPhotoView view){
            mShowPhotoView =view;
    }
    @Override
    public void start() {
        mShowPhotoView.onShowProgressbar();
        queryDataFromMode();
    }
    @Override
    public void end() {

    }
    private void queryDataFromMode(){
        new QueryMediaHelper(mShowPhotoView.getContext())
                .setCallBack(this)
                .startQueryMedia();
    }
    @Override
    public void showPhotosByFolder(PhotoFolder folder) {
        if(folder==null){
            mShowPhotoView.onFailure("无数据");
          return ;
        }else if(mPhotos.containsKey(folder)){
            List<Photo> photos = mPhotos.get(folder);
            mShowPhotoView.onShowPhotoGrid(photos);
        }else{
            showPhotosAll();
        }
    }
    @Override
    public void showPhotosByPosition(int position) {
        //TODO
    }
    @Override
    public void showPhotosAll() {
        List<Photo> all=new ArrayList<Photo>();
        Set<Map.Entry<PhotoFolder, List<Photo>>> entry  = mPhotos.entrySet();
        Iterator<Map.Entry<PhotoFolder, List<Photo>>> iterator = entry.iterator();
        while (iterator.hasNext()){
            Map.Entry<PhotoFolder, List<Photo>> next = iterator.next();
            List<Photo> photos = next.getValue();
            all.addAll(photos);
        }
        Collections.sort(all);
        mShowPhotoView.onShowPhotoGrid(all);
    }
    @Override
    public void onReceiveFolderList(List<PhotoFolder> folders) {
        mFolders=folders;
        mShowPhotoView.onShowPhotoFolder(mFolders);
        mShowPhotoView.onDismissProgressbar();
    }
    @Override
    public void onReceivePhotosMap(Map<PhotoFolder, List<Photo>> photos) {
        mPhotos=photos;
        mShowPhotoView.onDismissProgressbar();
        showPhotosAll();
    }
    @Override
    public void onError(String msg) {
        mShowPhotoView.onFailure(msg);
    }
}
