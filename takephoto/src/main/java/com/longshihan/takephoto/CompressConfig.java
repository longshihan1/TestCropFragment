package com.longshihan.takephoto;


import com.longshihan.takephoto.options.CustomOptions;
import com.longshihan.takephoto.options.LubanOptions;

import java.io.Serializable;

/**
 * 压缩配置类
 *
 */
public class CompressConfig implements Serializable {
    /**
     * Luban压缩配置
     */
    private LubanOptions lubanOptions;
    private CustomOptions customOptions;
    public static CompressConfig ofDefaultConfig(CustomOptions options){
        return new CompressConfig(options);
    }
    public static CompressConfig ofLuban(LubanOptions options){
        return new CompressConfig(options);
    }
    private CompressConfig(){}

    private CompressConfig(LubanOptions options){
        this.lubanOptions=options;
    }

    public LubanOptions getLubanOptions() {
        return lubanOptions;
    }

    private CompressConfig(CustomOptions options){
        this.customOptions=options;
    }

    public CustomOptions getCustomOptions() {
        return customOptions;
    }

}

