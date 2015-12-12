package com.yooyor.media.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yooyor.media.R;
import com.yooyor.media.adapter.PhotoGridAdapter;
import com.yooyor.media.listener.ICheckImageCallBack;
import com.yooyor.media.model.entity.Photo;
import com.yooyor.media.model.entity.PhotoFolder;
import com.yooyor.media.presenter.IPhotoPresenter;
import com.yooyor.media.presenter.PhotoPresenterImpl;
import com.yooyor.media.view.IShowPhotoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuefengyang on 2015/11/19.
 */
public class PhotoGridActivity extends AppCompatActivity implements IShowPhotoView,FolderListPopupWindow.OnSelectFolderListener ,ICheckImageCallBack{

    private final static String TAG="PhotoGridActivity";
    private GridView mPhotoGridView;
    private Button   mPhotoFolderBtn;
    private Button   mPhotoSelectCompleteBtn;
    private ProgressBar mProgressbar;
    private FolderListPopupWindow mPopupWindow;
    private List<PhotoFolder> mDataList;
    private PhotoGridAdapter  mAdapter;
    private IPhotoPresenter mPresenter;
    private int             mMaxSelectedCount;
    private final static int DEFAULT_SELECT_COUNT=9;
    public final static String KEY_PHOTO_URL="key_photo_url";
    private List<String> mSelectedPhotoUrls;
    private SparseArray<String> mAdapterSelectedUrls;

    public static Intent buildIntentForStart(Context context,int maxSelectCount){
        Intent intent =new Intent(context,PhotoGridActivity.class);
        intent.putExtra("maxCount", maxSelectCount);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        mMaxSelectedCount =getIntent().getIntExtra("maxCount",DEFAULT_SELECT_COUNT);
        if(mMaxSelectedCount<=0){
            Toast.makeText(getApplicationContext(),"selectCount must to more than 0",Toast.LENGTH_SHORT).show();
            finish();
        }
        initializeView();
        initializeVariable();
        mPresenter =new PhotoPresenterImpl(this);
        mPresenter.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.end();

    }
    private void initializeView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.photo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPhotoGridView  = (GridView) findViewById(R.id.photo_grid);
        mPhotoFolderBtn  =(Button)findViewById(R.id.photo_folder_btn);
        mProgressbar     =(ProgressBar)findViewById(R.id.progress_photo_transition);
        mPhotoSelectCompleteBtn =(Button)findViewById(R.id.btn_photo_selected);

        mPhotoSelectCompleteBtn.setEnabled(false);
        mPhotoSelectCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completePhotoSelected();
            }
        });
        mPhotoFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPopupWindow(v);
            }
        });
        mAdapter=new PhotoGridAdapter(this,null,this);
        mPhotoGridView.setAdapter(mAdapter);
        mPhotoFolderBtn.setText("全部");
    }
    private void initializeVariable(){
        mSelectedPhotoUrls=new ArrayList<>();
    }
    private void switchPopupWindow(View v){
        if(mPopupWindow==null){
            mPopupWindow=new FolderListPopupWindow(PhotoGridActivity.this,mDataList,this);
        }
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }else{
            mPopupWindow.show(v);
        }
    }
    private void completePhotoSelected(){
        int size = mAdapterSelectedUrls.size();
        for (int i = 0; i < size; i++) {
            mSelectedPhotoUrls.add(mAdapterSelectedUrls.valueAt(i));
        }
        Intent intent=new Intent();
        intent.putStringArrayListExtra(KEY_PHOTO_URL,(ArrayList)mSelectedPhotoUrls);
        setResult(RESULT_OK,intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        if(mPopupWindow!=null&&mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
            return ;
        }
        super.onBackPressed();
    }
    @Override
    public void onShowProgressbar() {
        mProgressbar.setVisibility(View.VISIBLE);
        mPhotoFolderBtn.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onDismissProgressbar() {
        mProgressbar.setVisibility(View.GONE);
        mPhotoFolderBtn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onShowPhotoFolder(List<PhotoFolder> folders) {
        mDataList=folders;
        mPhotoFolderBtn.setEnabled(mDataList.size() > 1);
    }
    @Override
    public void onShowPhotoGrid(List<Photo> photos) {
        mAdapter.refreshData(photos);
    }
    @Override
    public void onFailure(String msg) {
        Toast.makeText(this,"msg:"+msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onSelectFolder(PhotoFolder folder) {
        if(mPresenter!=null){
            mPresenter.showPhotosByFolder(folder);
            mPhotoFolderBtn.setText(folder.name);
        }
    }
    @Override
    public void onCheckImageUrl(SparseArray sa) {
        mAdapterSelectedUrls=sa;
    }
    @Override
    public void onCheckCount(int count) {
        if(count>0){
            mPhotoSelectCompleteBtn.setEnabled(true);
            mPhotoSelectCompleteBtn.setText(getResources().getText(R.string.photo_selected_complete)+"("+count+"/"+mMaxSelectedCount+")");
        }else{
            mPhotoSelectCompleteBtn.setEnabled(false);
            mPhotoSelectCompleteBtn.setText(getResources().getText(R.string.photo_selected_complete));
        }
        mAdapter.setIsCanContinueSelect(count<mMaxSelectedCount);
    }
    @Override
    public void onCheckCameraItem() {
        Toast.makeText(this,"jump camera",Toast.LENGTH_SHORT).show();
    }


}
