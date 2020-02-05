package com.heneng.launcher.ui.activity.application;

import com.blxt.quickactivity.AbstractApplication;
import com.blxt.quicklog.QLog;
import com.heneng.launcher.util.ImageTools;
import com.heneng.quicknoti.TipToast;

public class MyApplication extends AbstractApplication {
    static MyApplication instance = null;
    String LogPath = "/log.txt";

    public static MyApplication getInstance(){
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        LogPath = QLog.PATH.getAppCachePath(getBaseContext()) + LogPath;

        QLog.i(this, "日志路径", LogPath);

        QLog.isSave = true;
        new QLog(LogPath);

        TipToast.newInstance(getBaseContext(), null);

        // 初始化背景图片
        ImageTools.initToBackgroundImageFile(getBaseContext());
    }

}
