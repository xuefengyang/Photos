package com.yooyor.media.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.yooyor.media.listener.IQueryMediaCallBack;
import com.yooyor.media.model.entity.Photo;
import com.yooyor.media.model.entity.PhotoFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by xuefengyang on 2015/11/19.
 */
public class QueryMediaHelper {

    private  QueryMediaHelper mInstance;
    private IQueryMediaCallBack  mCallBack;
    private Context mContext;
    private Handler mEventHandler;
    private Wrapper mDataCache;

    private final static int QUERY_ERROR=1;
    private final static int QUERY_SUCCESS=0;

    public QueryMediaHelper(Context context){
        mContext = context;
        initHandler();
    }
    private void initHandler(){
        mEventHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                    if(msg.what==QUERY_ERROR){
                        if(mCallBack!=null){
                            mCallBack.onError("无数据");
                        }
                    }else if(msg.what==QUERY_SUCCESS){
                        if(mCallBack!=null){
                            mDataCache= (Wrapper) msg.obj;
                            mCallBack.onReceiveFolderList(mDataCache.folders);
                            mCallBack.onReceivePhotosMap(mDataCache.photos);
                        }
                    }
            }
        };
    }
    public QueryMediaHelper setCallBack(IQueryMediaCallBack callback){
        mCallBack =callback;
        return this;
    }
    public void startQueryMedia() {
        if(!checkCache()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String section = MediaStore.Images.Media.MIME_TYPE + "=? or " +
                            MediaStore.Images.Media.MIME_TYPE + "=? or " +
                            MediaStore.Images.Media.MIME_TYPE + "=? ";
                    String[] sectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg"};
                    Cursor cursor = mContext.getContentResolver().query(uri, null, section, sectionArgs, MediaStore.Images.Media.DATE_MODIFIED);
                    if (cursor == null) {
                        Message message = mEventHandler.obtainMessage(QUERY_ERROR);
                        mEventHandler.sendMessage(message);
                        return;
                    }
                    HashMap<String, List<Photo>> photos = new HashMap<String, List<Photo>>();
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                        String parentPathName = new File(path).getParentFile().getName();
                        if (photos.containsKey(parentPathName)) {
                            photos.get(parentPathName).add(new Photo(path, date));
                        } else {
                            ArrayList<Photo> list = new ArrayList<Photo>();
                            list.add(new Photo(path, date));
                            photos.put(parentPathName, list);
                        }
                    }
                    cursor.close();
                    buildDataAgain(photos);

                }
            }).start();
        }
    }

    /**
     * 检查数据
     * @return 是否已经获取了
     */
    private boolean checkCache(){
            if(mDataCache!=null){
                mEventHandler.obtainMessage(QUERY_SUCCESS, mDataCache);
                return true;
            }
        return false;
    }
    /**
     * 二次封装数据 list<PhotoFolder> folder
     * Map<Folder,List<Photo>>
     * @param data source data
     */
    private void buildDataAgain(HashMap<String,List<Photo>> data){
        List<PhotoFolder> folders =new ArrayList<PhotoFolder>();
        HashMap<PhotoFolder,List<Photo>> photos=new HashMap<PhotoFolder,List<Photo>>();
        Set<Map.Entry<String, List<Photo>>> entry = data.entrySet();
        Iterator<Map.Entry<String, List<Photo>>> iterator = entry.iterator();
        int totalSize=0;
        long maxDate=0;
        String maxNewPath="";
        while (iterator.hasNext()){
            Map.Entry<String, List<Photo>> next = iterator.next();
            String key = next.getKey();
            List<Photo> value = next.getValue();
            Collections.sort(value);
            String path=value.get(0).path;
            long date = value.get(0).date;
            int size = value.size();
            totalSize+=size;
            if(date>maxDate){
                maxDate=date;
                maxNewPath=path;
            }
            PhotoFolder photoFolder = new PhotoFolder(key, size, path);
            photos.put(photoFolder, value);
            folders.add(photoFolder);
        }
        PhotoFolder folder=new PhotoFolder("全部",totalSize,maxNewPath);
        folders.add(0,folder);
        Collections.sort(folders);
        Message message = mEventHandler.obtainMessage(QUERY_SUCCESS, new Wrapper(photos, folders));
        mEventHandler.sendMessage(message);

    }
    private static class Wrapper{
        public  List<PhotoFolder> folders =null;
        public  HashMap<PhotoFolder,List<Photo>> photos=null;
        public Wrapper(HashMap<PhotoFolder, List<Photo>> photos, List<PhotoFolder> folders) {
            this.photos = photos;
            this.folders = folders;
        }
    }
}
