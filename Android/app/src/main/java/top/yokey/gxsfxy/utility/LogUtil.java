package top.yokey.gxsfxy.utility;

import android.util.Log;

public class LogUtil {

    //作用：输出日志
    public static void show(String string) {

        if (TextUtil.isEmpty(string)) {
            Log.d("Gxsfxy", "内容为空!");
        } else {
            Log.d("Gxsfxy", string);
        }

    }

}