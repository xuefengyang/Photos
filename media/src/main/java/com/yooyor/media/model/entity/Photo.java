package com.yooyor.media.model.entity;

import java.util.Comparator;

/**
 * Created by xuefengyang on 2015/11/21.
 */
public class Photo implements Comparable{

    public String path;
    public long date;

    public Photo(String path, long date) {
        this.path = path;
        this.date = date;

    }
    @Override
    public int compareTo(Object another) {
        Photo photo =(Photo)another;
        return photo.date>this.date?1:photo.date<this.date?-1:0;
    }
}