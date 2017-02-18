package crixec.filehelper.function.replace;

/**
 * Created by crixec on 17-2-18.
 */

public class ReplaceBean {
    private String orginContent;
    private String newContent;

    public ReplaceBean() {
    }

    public ReplaceBean(String orginContent, String newContent) {
        this.orginContent = orginContent;
        this.newContent = newContent;
    }

    public String getOrginContent() {
        return orginContent;
    }

    public void setOrginContent(String orginContent) {
        this.orginContent = orginContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}
