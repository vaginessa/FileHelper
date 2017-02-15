package crixec.filehelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
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
    public static String computeDigest(File file, String type){
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance(type);
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    public static String computeFileMD5(File file) {
        return computeDigest(file, "MD5");
    }

    public static String computeFileSHA(File file) {
        return computeDigest(file, "SHA");
    }


    public static String computeUseableFileSize(File file) {
        return computeReadableFileSize(file) + "(" + file.length() + ")";
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

    public static String computeReadableTime(long time) {
        return computeTime(time, "HH:mm MM-dd-yyyy");
    }
    public static String computeTime(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date((time)));
    }
    public static String computeUseableTime(long time) {
        return computeTime(time, "yyyy-MM-dd HH:mm:ss");
    }
    public static boolean isTextEmpty(CharSequence text) {
        if (text == null || text.length() == 0 || text.toString().equals("") || text.toString().trim().equals("")) {
            return true;
        } else
            return false;
    }

    public static boolean createFile(File file) {
        try {
            return file != null && file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFolder(File file) {
        return file != null && file.mkdirs();
    }

    public static boolean delete(File clickedFile) {
        return clickedFile != null && clickedFile.delete();
    }

    public static String realPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSymlink(File file) {
        boolean result = false;
        try {
            File canon = null;
            if (file == null) return false;
            if (file.getParent() == null) {
                canon = file;
            } else {
                File canonDir = file.getParentFile().getCanonicalFile();
                canon = new File(canonDir, file.getName());
            }
            result = !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isSymlinkFile(File file) {
        if (isSymlink(file)) {
            try {
                return file.getCanonicalFile().isFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.isFile();
    }

    public static boolean isSymlinkDirectory(File file) {
        if (isSymlink(file)) {
            try {
                return file.getCanonicalFile().isDirectory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.isDirectory();
    }
}
