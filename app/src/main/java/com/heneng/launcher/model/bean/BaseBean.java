package com.heneng.launcher.model.bean;


public class BaseBean {

    protected String title;
    protected String content;
    protected Object image;// Bitmap 或 Drawable

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setImage(Object image) {
        this.image = image;
    }
}
