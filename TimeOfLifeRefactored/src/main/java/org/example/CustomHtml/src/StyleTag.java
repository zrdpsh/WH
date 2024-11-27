package org.example.CustomHtml.src;

public class StyleTag {
    private final String openTag;
    private final String closeTag;
    private final int flag;

    public StyleTag(String openTag, String closeTag, int flag) {
        this.openTag = openTag;
        this.closeTag = closeTag;
        this.flag = flag;
    }

    public String getOpenTag() {
        return openTag;
    }

    public String getCloseTag() {
        return closeTag;
    }

    public int getFlag() {
        return flag;
    }
}