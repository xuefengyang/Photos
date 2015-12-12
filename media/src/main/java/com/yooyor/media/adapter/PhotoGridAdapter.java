package com.yooyor.media.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yooyor.media.R;
import com.yooyor.media.listener.ICheckImageCallBack;
import com.yooyor.media.model.entity.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuefengyang on 2015/11/22.
 */
public class PhotoGridAdapter extends BaseAdapter {

    private List<Photo> mDataList;
    private Context     mContext;
    private SparseArray<String>  mCheckedImageUrl;
    private ICheckImageCallBack  mCallBack;
    private boolean      isCanContinueSelect=true;

    public boolean isCanContinueSelect() {
        return isCanContinueSelect;
    }

    public void setIsCanContinueSelect(boolean isCanContinueSelect) {
        this.isCanContinueSelect = isCanContinueSelect;
    }

    public PhotoGridAdapter(Context context,List<Photo> photos,ICheckImageCallBack callback){
           mDataList =photos;
           mContext  =context;
           mCallBack =callback;
           mCheckedImageUrl=new SparseArray<>();

    }
    @Override
    public int getCount() {
        return mDataList==null?0:mDataList.size();
    }
    @Override
    public Photo getItem(int position) {
        return mDataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_photo_grid,parent,false);
            holder=new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_photo_item);
            holder.selected  = (ImageView) convertView.findViewById(R.id.image_photo_selected);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
            reSizeImageWidget(holder.imageView);
            bindData(position, holder);
            bindListener(position, holder);
        return convertView;
    }
    public void refreshData(List<Photo> photos){
        mDataList =photos;
        notifyDataSetChanged();
    }
    private void reSizeImageWidget(ImageView imageView){
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width =(screenWidth(mContext)-dp2px(8,mContext))/3;
        lp.height=lp.width;
        imageView.setLayoutParams(lp);
    }
    private void bindData(int position,ViewHolder holder){
        holder.selected.setVisibility(View.VISIBLE);
        if(mCheckedImageUrl.size()>0&&!TextUtils.isEmpty(mCheckedImageUrl.get(position, ""))){
            holder.selected.setImageResource(R.drawable.tt_album_img_selected);
            holder.imageView.setColorFilter(0x77000000);
        }else{
            holder.selected.setImageResource(R.drawable.tt_album_img_select_nor);
            holder.imageView.setColorFilter(null);
        }
        Photo photo = getItem(position);
        Glide.with(mContext).load(new File(photo.path)).placeholder(R.drawable.place_holder_sp).centerCrop().crossFade().into(holder.imageView);

    }
    private void bindListener(final int position, final ViewHolder holder){
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckedImageUrl.size()>0&&!TextUtils.isEmpty(mCheckedImageUrl.get(position,""))) {
                    holder.imageView.setColorFilter(null);
                    holder.selected.setImageResource(R.drawable.tt_album_img_select_nor);
                    mCheckedImageUrl.remove(position);
                } else {
                    if(isCanContinueSelect==false){
                        return ;
                    }
                    holder.imageView.setColorFilter(0x77000000);
                    holder.selected.setImageResource(R.drawable.tt_album_img_selected);
                    mCheckedImageUrl.put(position, getItem(position).path);
                }

                doAnimation(holder.selected);
                calcCheckImageCount();
            }
        });
    }
    private void doAnimation(View v){
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(v, "scaleX", 0.6f, 0.8f, 1.2f, 1.4f, 1.2f, 1.0f);
        scaleXAnimator.setDuration(300);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(v, "scaleY", 0.6f, 0.8f, 1.2f, 1.4f, 1.2f, 1.0f);
        scaleYAnimator.setDuration(300);
        List<Animator> animatorList=new ArrayList<>();
        animatorList.add(scaleXAnimator);
        animatorList.add(scaleYAnimator);

        AnimatorSet set=new AnimatorSet();
        set.playTogether(animatorList);
        set.start();

    }
    private void calcCheckImageCount(){
        if(mCallBack!=null){
            mCallBack.onCheckImageUrl(mCheckedImageUrl);
            mCallBack.onCheckCount(mCheckedImageUrl.size());
        }
    }
    private static class ViewHolder{
        ImageView imageView;
        ImageView selected;
    }
    private  int screenWidth(Context context){
     return  context.getResources().getDisplayMetrics().widthPixels;
    }
    private  int dp2px(int dp,Context context){
         return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics()));
    }


}
