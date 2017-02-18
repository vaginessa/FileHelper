package crixec.filehelper;

import android.util.Log;

/**
 * Created by crixec on 17-2-18.
 */

public class Logger {
    private static String TAG = "[FileHelper]";
    public static void i(Class clazz, CharSequence text){
        i(clazz.getSimpleName(), text.toString());
    }
    public static void i(String tag, CharSequence text){
        if(BuildConfig.DEBUG){
            Log.i(tag, text.toString());
        }
    }
    public static void i(CharSequence text){
        i(TAG, text.toString());
    }
}
