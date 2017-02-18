package crixec.filehelper;

import android.os.Environment;

import java.io.File;

/**
 * Created by crixec on 17-2-11.
 */

public class SettingHelper {
    public static File getDefautlStartStorage() {
        if (BuildConfig.DEBUG)
            return new File(Environment.getExternalStorageDirectory(), "test");
        return Environment.getExternalStorageDirectory();
    }
}
