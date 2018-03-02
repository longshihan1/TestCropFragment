package com.longshihan.takephoto;

import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;

import com.longshihan.takephoto.listener.OnLoadBitmapListsner;
import com.longshihan.takephoto.options.CropOptions;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/1 0001
 * @des
 * @function
 */

public class TakePhotoUtils {
    static final String TAG="TakePhotoUtils";
    private Activity activity;
    private CropOptions cropOptions;
    private OnLoadBitmapListsner onLoadBitmapListsner;
    private TakePhotoFragment fragment;
    private boolean isCameraCrop=true;
    private CompressConfig compressConfig;


    /**
     * Student的创建完全依靠Student.Builder，使用一种方法链的方式来创建
     *
     */
    public static class Builder {

        private TakePhotoUtils target;

        public Builder() {
            target = new TakePhotoUtils();
        }

        public Builder setActivity(Activity activity) {
            target.activity = activity;
            return this;
        }


        public Builder setCropOption(CropOptions options) {
            target.cropOptions = options;
            return this;
        }


        public Builder setCameraCrop(boolean cameraCrop) {
            target.isCameraCrop = cameraCrop;
            return this;
        }

        public Builder setCompressConfig(CompressConfig compressConfig){
            target.compressConfig=compressConfig;
            return this;
        }

        public Builder setOnLoadBitmapListsner(OnLoadBitmapListsner onLoadBitmapListsner) {
            target.onLoadBitmapListsner = onLoadBitmapListsner;
            return this;
        }
        /**
         * 生成fragmnet对象
         * @return
         */
        public TakePhotoUtils build(){
            target.fragment=findTakePhotoFragment(target.activity);
            boolean isNewInstance=target.fragment==null;
            if (isNewInstance){
                target.fragment=new TakePhotoFragment();
                target.fragment.setOnLoadListener(target.onLoadBitmapListsner);
                if (target.cropOptions==null){
                    target.cropOptions=new CropOptions();
                }
                target.fragment.setCropOptions(target.cropOptions);
                target.fragment.setCameraCrop(target.isCameraCrop);
                target.fragment.setCompressConfig(target.compressConfig);
                FragmentManager fragmentManager = target.activity.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(target.fragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
            return target;
        }

        private TakePhotoFragment findTakePhotoFragment(Activity activity) {
            if (activity==null){
                Log.d(TAG,"Activity不能为空");
                return null;
            }
            return (TakePhotoFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        }
    }

    /**
     * 拍照
     */
    public void selectTakePhoto(){
        if (fragment!=null) {
            fragment.selectTakePhoto();
        }
    }

    /**
     * 选择相册
     */
    public void selectAlbumPhoto(){
        if (fragment!=null) {
            fragment.selectAlbumPhoto();
        }
    }


}
