package com.longshihan.takephoto;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/1 0001
 * @des 返回处理的图片对象
 * @function
 */

public class CropImage implements Serializable {
    public String originalPath;//原始文件地址
    public String compressPath;//压缩后的文件地址
    public Uri originalUri;
    public FromType fromType;
    public boolean cropped;
    public boolean compressed;

    public static CropImage of(String path, FromType fromType) {
        return new CropImage(path, fromType);
    }

    public static CropImage of(Uri uri, FromType fromType) {
        return new CropImage(uri, fromType);
    }

    private CropImage(String path, FromType fromType) {
        this.originalPath = path;
        this.fromType = fromType;
    }

    private CropImage(Uri uri, FromType fromType) {
        this.originalPath = uri.getPath();
        this.originalUri=uri;
        this.fromType = fromType;
    }

    public enum FromType {
        CAMERA, OTHER
    }
}
