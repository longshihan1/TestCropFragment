package com.longshihan.testcrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.longshihan.takephoto.CompressConfig;
import com.longshihan.takephoto.CropImage;
import com.longshihan.takephoto.options.CropOptions;
import com.longshihan.takephoto.options.LubanOptions;
import com.longshihan.takephoto.listener.OnLoadBitmapListsner;
import com.longshihan.takephoto.TakePhotoUtils;

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
                .setMaxHeight(180)
                .setMaxWidth(180)
                .setMaxSize(60000)
                .create();
        config= CompressConfig.ofLuban(option);

        takePhotoUtils=new TakePhotoUtils.Builder()
                .setActivity(this)
                .setOnLoadBitmapListsner(new OnLoadBitmapListsner() {
                    @Override
                    public void onLoadBitmap(CropImage image) {
                        Glide.with(MainActivity.this).load(image.compressPath).into(iv);
                    }

                    @Override
                    public void onLoadFailure(int type, String msg) {
                        //错误
                    }
                })
                .setCompressConfig(config)
                .setCropOption(new CropOptions.Builder().setAspectX(1).setAspectY(1).setOutputX(160).setAspectY(160).create())
                .build();
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
