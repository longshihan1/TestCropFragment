package com.longshihan.takephoto;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.longshihan.takephoto.compress.CompressWithCustomer;
import com.longshihan.takephoto.compress.CompressWithLuBan;
import com.longshihan.takephoto.listener.OnLoadBitmapListsner;
import com.longshihan.takephoto.options.CropOptions;
import com.longshihan.takephoto.utils.ThreadUtils;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/1 0001
 * @des 处理拍照裁剪的fragment，自带权限认定
 * @function
 */

public class TakePhotoFragment extends Fragment {
    private static final String TAG = TakePhotoFragment.class.getSimpleName();
    private OnLoadBitmapListsner onLoadListener;
    public static final int TAKE_PHOTO = 0x01;//拍照
    public static final int CHOOSE_PHOTO = 0x10;//选择相册
    public static final int PICTURE_CUT = 0x11;//剪切图片
    private Uri imageUri;//相机拍照图片保存地址
    private Uri outputUri;//裁剪万照片保存地址
    private String imagePath;//打开相册选择照片的路径
    private boolean isClickCamera;//是否是拍照裁剪
    private CropOptions cropOptions;
    private CompressConfig compressConfig;
    private CompressImage compressImage;

    public TakePhotoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto(imageUri);//裁剪图片
                }
                break;
            case CHOOSE_PHOTO://打开相册
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data);
                }
                break;
            case PICTURE_CUT://裁剪完成
                isClickCamera = true;
                try {
                    if (isClickCamera) {
                        takeResult(CropImage.of(outputUri, CropImage.FromType.OTHER));
                    } else {
                        takeResult(CropImage.of(imagePath, CropImage.FromType.CAMERA));
                    }
                } catch (Exception e) {
                    takeError(0, "裁剪返回错误");
                }
                break;
        }
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getActivity().getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!file.exists()){
            return;
        }
        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        if (cropOptions.getAspectX() != 0) {
            intent.putExtra("aspectX", cropOptions.getAspectX());
        } else {
            intent.putExtra("aspectX", 1);
        }
        if (cropOptions.getAspectY() != 0) {
            intent.putExtra("aspectY", cropOptions.getAspectY());
        } else {
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        if (cropOptions.getOutputX() != 0) {
            intent.putExtra("outputX", cropOptions.getOutputX());
        }else {
            intent.putExtra("outputX", 200);
        }
        if (cropOptions.getOutputY() != 0) {
            intent.putExtra("outputY", cropOptions.getOutputY());
        }else {
            intent.putExtra("outputY", 200);
        }
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent, PICTURE_CUT);
    }

    /**
     * 拍照
     */
    public void selectTakePhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 2);
        } else {
            openCamera();//打开相机
        }

    }

    private void openCamera() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
            //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
            imageUri = FileProvider.getUriForFile(getActivity(), "com.longshihan.takephoto.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    // 4.4及以上系统使用这个方法处理图片 相册图片返回的不再是真实的Uri,而是分装过的Uri
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d(TAG, "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        cropPhoto(uri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        cropPhoto(uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectAlbumPhoto();
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();//打开相机
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void takeResult(CropImage image) {
        if (null == compressConfig) {
            onLoadListener.onLoadBitmap(image);
        }else if (compressConfig.getLubanOptions()!=null){
            compressImage=new CompressWithLuBan(getActivity(), compressConfig, image, new CompressImage.CompressListener() {
                @Override
                public void onCompressSuccess(final CropImage image) {
                    if (ThreadUtils.isMainThread()){
                        onLoadListener.onLoadBitmap(image);
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onLoadListener.onLoadBitmap(image);
                            }
                        });
                    }
                }

                @Override
                public void onCompressFailed(final CropImage image, final String msg) {
                    if (!ThreadUtils.isMainThread()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onLoadListener.onLoadFailure(1, msg);
                            }
                        });
                    }else {
                        onLoadListener.onLoadFailure(1, msg);
                    }
                }
            });
            compressImage.compress();
        }else if (compressConfig.getCustomOptions()!=null){
            compressImage=new CompressWithCustomer(getActivity(), compressConfig, image, new CompressImage.CompressListener() {
                @Override
                public void onCompressSuccess(final CropImage image) {
                    if (ThreadUtils.isMainThread()){
                        onLoadListener.onLoadBitmap(image);
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onLoadListener.onLoadBitmap(image);
                            }
                        });
                    }
                }

                @Override
                public void onCompressFailed(CropImage image, final String msg) {
                    if (!ThreadUtils.isMainThread()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onLoadListener.onLoadFailure(1, msg);
                            }
                        });
                    }else {
                        onLoadListener.onLoadFailure(1, msg);
                    }
                }
            });
            compressImage.compress();
        }else {
            onLoadListener.onLoadBitmap(image);
        }
    }

    public void takeError(int type, String msg) {
        onLoadListener.onLoadFailure(type, msg);
    }


    /**
     * 相册
     */
    public void selectAlbumPhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            selectFromAlbum();//打开相册
        }
    }

    private void selectFromAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    public void setOnLoadListener(OnLoadBitmapListsner onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * 是否要拍照裁剪
     *
     * @param cameraCrop
     */
    public void setCameraCrop(boolean cameraCrop) {
        isClickCamera = cameraCrop;
    }

    public void setCropOptions(CropOptions cropOptions) {
        this.cropOptions = cropOptions;
    }

    public void setCompressConfig(CompressConfig compressConfig) {
        this.compressConfig = compressConfig;
    }
}
