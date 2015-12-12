package com.yooyor.photo.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yooyor.photo.R;
import com.yooyor.photo.entity.PhotoBean;

import java.util.List;

/**
 * Created by xuefengyang on 2015/11/17.
 */
public class PhotoFeedAdapter extends RecyclerView.Adapter<PhotoFeedAdapter.ViewHolder>{

    private List<PhotoBean> mDataList;
    private Context         mContext;
    public PhotoFeedAdapter(Context context,List<PhotoBean> datalist){
            mContext =context;
            mDataList=datalist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_feed_photo_layout,parent,false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
