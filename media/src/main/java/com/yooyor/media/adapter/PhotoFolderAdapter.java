package com.yooyor.media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yooyor.media.R;
import com.yooyor.media.model.entity.PhotoFolder;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by xuefengyang on 2015/11/19.
 */
public class PhotoFolderAdapter extends BaseAdapter {

    private  List<PhotoFolder> mFolderList;
    private Context mContext;

    public PhotoFolderAdapter(List<PhotoFolder> folderList, Context context) {
        this.mFolderList = folderList;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mFolderList!=null?mFolderList.size():0;
    }
    @Override
    public PhotoFolder getItem(int position) {
        return mFolderList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_photo_folder,parent,false);
            holder=new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.folder_image);
            holder.titleView  =(TextView)convertView.findViewById(R.id.folder_name);
            holder.textCount =(TextView)convertView.findViewById(R.id.text_folder_count);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoFolder folder = getItem(position);
        holder.titleView.setText(folder.name);
        holder.textCount.setText(String.valueOf(folder.count));
        Glide.with(mContext).load(new File(folder.firstPhotoUrl)).centerCrop().into(holder.imageView);
        return convertView;

    }
    private static class ViewHolder {
        ImageView imageView;
        TextView  titleView;
        TextView  textCount;
    }
}
