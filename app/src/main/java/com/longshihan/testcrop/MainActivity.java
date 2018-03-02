package com.longshihan.testcrop;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.longshihan.takephoto.CompressConfig;
import com.longshihan.takephoto.CropImage;
import com.longshihan.takephoto.LubanOptions;
import com.longshihan.takephoto.OnLoadBitmapListsner;
import com.longshihan.takephoto.TakePhotoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button takeBtn;//打开相机按钮
    Button albumBtn;//选择相册按钮
    ImageView iv;//裁剪完照片展示控件
    TakePhotoUtils takePhotoUtils;
    private CompressConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takeBtn=findViewById(R.id.take_btn);
        albumBtn=findViewById(R.id.album_btn);
        iv=findViewById(R.id.iv);
        LubanOptions option=new LubanOptions.Builder()
                .setMaxHeight(60)
                .setMaxWidth(60)
                .setMaxSize(600)
                .create();
        config= CompressConfig.ofLuban(option);
        takePhotoUtils=new TakePhotoUtils.Builder()
                .setActivity(this)
                .setOnLoadBitmapListsner(new OnLoadBitmapListsner() {
                    @Override
                    public void onLoadBitmap(CropImage image) {
//                        iv.setImageBitmap(bitmap);
                        initImage(image);
                    }

                    @Override
                    public void onLoadFailure(int type, String msg) {
                        //错误
                    }
                })
                .setCompressConfig(config)
                .build();
    }

    @UiThread
    private void initImage(CropImage image) {
        Log.d("测试",image.compressPath);
        Log.d("测试",image.originalPath);
        File file=new File("/data/user/0/com.jph.simple/cache/luban_disk_cache/Luban_1519981060378.jpg");
        File file1=new File(image.originalPath);
        File file2=new File(getExternalCacheDir(),"crop_image.jpg");
        Glide.with(MainActivity.this).load(image.originalUri).into(iv);
        iv.setImageURI(image.originalUri);
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_btn:
                takePhotoUtils.selectTakePhoto();
                break;
            case R.id.album_btn:
                takePhotoUtils.selectAlbumPhoto();
                break;
        }
    }



}
