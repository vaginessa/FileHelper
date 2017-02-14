package crixec.filehelper;

import android.os.Environment;

import java.io.File;

/**
 * Created by crixec on 17-2-11.
 */

public class SettingHelper {
    public static File getDefautlStartStorage() {
        return Environment.getExternalStorageDirectory();
    }
}
