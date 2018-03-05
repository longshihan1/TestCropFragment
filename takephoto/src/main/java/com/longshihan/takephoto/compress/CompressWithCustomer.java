package com.longshihan.takephoto.compress;

import android.content.Context;

import com.longshihan.takephoto.CompressConfig;
import com.longshihan.takephoto.CompressImage;
import com.longshihan.takephoto.CropImage;
import com.longshihan.takephoto.options.CustomOptions;
import com.longshihan.takephoto.options.LubanOptions;

import java.io.File;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/5 0005
 * @des 自定义压缩配置
 * @function
 */

public class CompressWithCustomer implements CompressImage {
    private CropImage image;
    private CompressListener listener;
    private Context context;
    private CustomOptions options;
    private File file =null;

    public CompressWithCustomer(Context context, CompressConfig config, CropImage images,
                             CompressListener listener) {
        options = config.getCustomOptions();
        this.image = images;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void compress() {
        listener.onCompressSuccess(image);
    }
}
