package com.longshihan.takephoto.options;

import com.longshihan.takephoto.CompressConfig;

import java.io.Serializable;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/5 0005
 * @des
 * @function
 */

public class CustomOptions implements Serializable {
    /**
     * 长或宽不超过的最大像素,单位px
     */
    private int maxPixel=1200;
    /**
     * 压缩到的最大大小，单位B
     */
    private int maxSize=100*1024;

    /**
     * 是否启用像素压缩
     */
    private boolean enablePixelCompress=true;
    /**
     * 是否启用质量压缩
     */
    private boolean enableQualityCompress=true;


    public CustomOptions setMaxPixel(int maxPixel) {
        this.maxPixel = maxPixel;
        return this;
    }


    public CustomOptions setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public CustomOptions enablePixelCompress(boolean enablePixelCompress) {
        this.enablePixelCompress = enablePixelCompress;
        return this;
    }

    public CustomOptions enableQualityCompress(boolean enableQualityCompress) {
        this.enableQualityCompress = enableQualityCompress;
        return this;
    }

    public static class Builder{
        private CustomOptions config;
        public Builder() {
            config=new CustomOptions();
        }
        public Builder setMaxSize(int maxSize) {
            config.setMaxSize( maxSize);
            return this;
        }
        public Builder setMaxPixel(int maxPixel) {
            config.setMaxPixel(maxPixel);
            return this;
        }
        public Builder enablePixelCompress(boolean enablePixelCompress) {
            config.enablePixelCompress(enablePixelCompress);
            return this;
        }
        public Builder enableQualityCompress(boolean enableQualityCompress) {
            config.enableQualityCompress(enableQualityCompress);
            return this;
        }

        public CustomOptions create(){
            return config;
        }
    }
}
