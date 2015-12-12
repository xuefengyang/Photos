package com.yooyor.media.listener;

import com.yooyor.media.model.entity.Photo;
import com.yooyor.media.model.entity.PhotoFolder;

import java.util.List;
import java.util.Map;

/**
 * Created by xuefengyang on 2015/11/21.
 */
public interface IQueryMediaCallBack {

       void onReceiveFolderList(List<PhotoFolder> folders);

       void onReceivePhotosMap(Map<PhotoFolder,List<Photo>> photos);

       void onError(String msg);

}
