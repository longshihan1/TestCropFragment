package com.longshihan.takephoto.utils;

import android.os.Looper;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/2 0002
 * @des 线程判断的工具类
 * @function
 */

public class ThreadUtils {
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
