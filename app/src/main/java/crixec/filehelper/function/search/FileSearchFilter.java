package crixec.filehelper.function.search;

import java.io.File;

/**
 * Created by crixec on 17-2-12.
 */

public class FileSearchFilter extends SearchFilter<File> {
    public static final int TYPE_FILE = 0;
    public static final int TYPE_FOLDER = 1;
    private int type;

    public FileSearchFilter(int type) {
        this.type = type;
    }

    public FileSearchFilter() {
        this(TYPE_FILE);
    }

    @Override
    public boolean onFilter(File file) {
        if(file != null) {
            if(file.isDirectory() && type == TYPE_FOLDER) return true;
            if(file.isFile() && type == TYPE_FOLDER) return true;
        }
        return false;
    }
}
