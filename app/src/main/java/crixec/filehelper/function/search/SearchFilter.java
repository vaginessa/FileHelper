package crixec.filehelper.function.search;

/**
 * Created by crixec on 17-2-12.
 */

public abstract class SearchFilter<T> {
    public abstract  boolean onFilter(T t);
}
