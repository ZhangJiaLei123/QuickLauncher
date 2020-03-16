
package com.heneng.launcher.model.bean;

import android.graphics.drawable.Drawable;

public class AppInfoBean extends BaseBean{

    private String dataDir;
    private String id;
    private String launcherName;
    private String packageName;
    private int pageIndex;
    private int position;
    private boolean sysApp;

    public String getDataDir() {
        return this.dataDir;
    }

    public Drawable getImage() {
        return (Drawable)this.image;
    }

    public String getId() {
        return this.id;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public int getPosition() {
        return this.position;
    }

    public void setDataDir(String paramString) {
        this.dataDir = paramString;
    }

    public void setIcon(Drawable paramDrawable) {
        this.image = paramDrawable;
    }

    public void setId(String paramString) {
        this.id = paramString;
    }

    public void setPackageName(String paramString) {
        this.packageName = paramString;
    }

    public void setPageIndex(int paramInt) {
        this.pageIndex = paramInt;
    }

    public void setPosition(int paramInt) {
        this.position = paramInt;
    }

    public String toString() {
        return "AppBean [packageName=" + this.packageName + ", title=" + this.title + ", dataDir=" + this.dataDir + "]";
    }

    public boolean isSysApp() {
        return sysApp;
    }

    public void setSysApp(boolean sysApp) {
        this.sysApp = sysApp;
    }

    public String getLauncherName() {
        return launcherName;
    }

    public void setLauncherName(String launcherName) {
        this.launcherName = launcherName;
    }
}
