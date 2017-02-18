package crixec.filehelper.function.search;

import java.io.File;

/**
 * Created by crixec on 17-2-12.
 */

public abstract class AbsSearchFilter {
    private boolean isWildcardable = false;

    public boolean isWildcardable() {
        return isWildcardable;
    }

    public void setWildcardable(boolean wildcardable) {
        isWildcardable = wildcardable;
    }

    public abstract boolean onFilter(File file);

    public boolean compare(String str1, String str2) {
        if (isWildcardable)
            return str1 != null && str2 != null && str1.contains(str2);
        return str1 != null && str2 != null && str1.equals(str2);
    }
}
