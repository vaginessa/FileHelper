package crixec.filehelper.function.search;

import android.util.Log;

import java.io.File;

/**
 * Created by crixec on 17-2-16.
 */

public class SearchFileFilter extends AbsSearchFilter {
    private String name;

    public SearchFileFilter(String name) {
        this.name = name;
    }

    @Override
    public boolean onFilter(File file) {
        Log.i("SearchFilter", file.getPath());
        if (file != null && file.exists()) {
            return file.isFile() && file.getName().equals(name);
        }
        return false;
    }
}
