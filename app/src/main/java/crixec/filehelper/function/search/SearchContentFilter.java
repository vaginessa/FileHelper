package crixec.filehelper.function.search;

import android.text.TextUtils;

import java.io.File;

import crixec.filehelper.Utils;

/**
 * Created by crixec on 17-2-16.
 */

public class SearchContentFilter extends AbsSearchFilter {
    private String content;

    public SearchContentFilter(String content) {
        this.content = content;
    }

    @Override
    public boolean onFilter(File file) {
        if (file != null && file.exists()) {
            StringBuilder stringBuilder = Utils.readFile(file);
            if (!TextUtils.isEmpty(stringBuilder)) {
                return stringBuilder.indexOf(content) > -1;
            }
        }
        return false;
    }
}
