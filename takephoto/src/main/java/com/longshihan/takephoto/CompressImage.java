package com.longshihan.takephoto;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/1 0001
 * @des 压缩图片的接口
 * @function
 */

public interface CompressImage {
    void compress();

    /**
     * 压缩结果监听器
     */
    interface CompressListener {
        /**
         * 压缩成功
         * @param image 已经压缩图片
         */
        void onCompressSuccess(CropImage image);

        /**
         * 压缩失败
         * @param image 压缩失败的图片
         * @param msg 失败的原因
         */
        void onCompressFailed(CropImage image,String msg);
    }
}
