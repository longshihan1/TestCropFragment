package com.longshihan.takephoto;

import android.content.Context;

import java.io.File;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/2 0002
 * @des
 * @function
 */

public class CompressWithLuBan implements CompressBitmap {
    private CropImage image;
    private CompressListener listener;
    private Context context;
    private LubanOptions options;
    private File file =null;

    public CompressWithLuBan(Context context, CompressConfig config, CropImage images,
                             CompressListener listener) {
        options = config.getLubanOptions();
        this.image = images;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void compress() {
        if (image == null) {
            listener.onCompressFailed(image, " images is null");
            return;
        }
        file=new File(image.originalPath);
        compressOne();
    }

    private void compressOne() {
        Luban.compress(context,file)
                .putGear(Luban.CUSTOM_GEAR)
                .setMaxHeight(options.getMaxHeight())
                .setMaxWidth(options.getMaxWidth())
                .setMaxSize(options.getMaxSize() / 1000)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        image.compressPath = file.getPath();
                        image.compressed = true;
                        listener.onCompressSuccess(image);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressFailed(image, e.getMessage() + " is compress failures");
                    }
                });
    }

    private void handleCompressCallBack(File file) {
        image.compressed = true;
        image.compressPath = file.getPath();
        listener.onCompressSuccess(image);
    }
}