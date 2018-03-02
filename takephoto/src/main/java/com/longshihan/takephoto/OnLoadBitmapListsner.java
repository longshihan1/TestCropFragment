package com.longshihan.takephoto;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/1 0001
 * @des 返回CropImage的数据
 * @function
 */

public interface OnLoadBitmapListsner {
    void onLoadBitmap(CropImage image);//不传递bitmap，过于耗费内存
    void onLoadFailure(int type,String msg);//错误回调
}
