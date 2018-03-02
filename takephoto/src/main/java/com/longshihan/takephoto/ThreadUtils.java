package com.longshihan.takephoto;

import android.os.Looper;

/**
 * Created by LONGHE001.
 *
 * @time 2018/3/2 0002
 * @des
 * @function
 */

public class ThreadUtils {
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
