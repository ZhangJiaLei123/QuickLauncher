package com.heneng.launcher.ui.activity.application;

import com.blxt.quickactivity.AbstractApplication;
import com.blxt.quickactivity.QAppConfig;
import com.blxt.quicklog.QLog;
import com.heneng.launcher.util.ImageTools;
import com.heneng.quicknoti.TipToast;

public class AppApplication extends AbstractApplication {
    static AppApplication instance = null;
    String LogPath = "/log.txt";

    public static AppApplication getInstances(){
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

    public QAppConfig getAppConfig() {

        return appConfig;
    }
}
