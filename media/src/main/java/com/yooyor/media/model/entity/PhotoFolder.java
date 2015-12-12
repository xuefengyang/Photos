package com.yooyor.media.model.entity;

/**
 * Created by xuefengyang on 2015/11/19.
 *
 */
public class PhotoFolder implements Comparable {
    public String  firstPhotoUrl;
    public  int     count;
    public String  name;
    public PhotoFolder(String name, int count, String firstPhotoUrl) {
        this.name = name;
        this.count = count;
        this.firstPhotoUrl = firstPhotoUrl;

    }
    @Override
    public int compareTo(Object another) {
        PhotoFolder folder =(PhotoFolder)another;
        return folder.count-this.count;
    }
}
