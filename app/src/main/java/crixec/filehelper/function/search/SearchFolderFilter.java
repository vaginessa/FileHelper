package crixec.filehelper.function.search;

import java.io.File;

/**
 * Created by crixec on 17-2-16.
 */

public class SearchFolderFilter extends AbsSearchFilter {
    private String name;

    public SearchFolderFilter(String name) {
        this.name = name;
    }

    @Override
    public boolean onFilter(File file) {
        if (file != null && file.exists()) {
            return file.isDirectory() && file.getName().equals(name);
        }
        return false;
    }
}
