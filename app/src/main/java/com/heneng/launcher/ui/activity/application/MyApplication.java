package com.heneng.launcher.ui.activity.application;

import com.blxt.quickactivity.AbstractApplication;
import com.heneng.launcher.util.ImageTools;
import com.heneng.quicknoti.TipToast;

public class MyApplication extends AbstractApplication {
    static MyApplication instance = null;

    public static MyApplication getInstance(){
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        TipToast.newInstance(getBaseContext(), null);

        // 初始化背景图片
        ImageTools.initToBackgroundImageFile(getBaseContext());
    }

}
