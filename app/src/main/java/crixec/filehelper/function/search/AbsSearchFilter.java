package crixec.filehelper.function.search;

import java.io.File;

/**
 * Created by crixec on 17-2-12.
 */

public abstract class AbsSearchFilter {
    public abstract  boolean onFilter(File file);
}
