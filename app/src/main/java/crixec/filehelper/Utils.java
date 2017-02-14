package crixec.filehelper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by crixec on 17-2-11.
 */

public class Utils {

    public static File[] listFiles(File parentFile) {
        if (parentFile == null)
            parentFile = SettingHelper.getDefautlStartStorage();
        File[] files = parentFile.listFiles();
        if (files == null)
            files = new File[]{};
        return files;
    }

    public static String computeReadableFileSize(File file) {
        if (file == null || file.isDirectory()) return "";
        long size = file.length();
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + " bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + " MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + " GB";
        }
        return "";
    }
    public static String computeReadableTime(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MM-dd-yyyy");
        return formatter.format(new Date((time)));
    }
    public static boolean isTextEmpty(CharSequence text){
        if(text == null || text.length() == 0 || text.toString().equals("") || text.toString().trim().equals("")){
            return true;
        }
        else
            return false;
    }
    public static boolean createFile(File file){
        try {
            return file != null && file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean createFolder(File file){
        return file != null && file.mkdirs();
    }
}
