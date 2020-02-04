package com.heneng.launcher.ui.activity.application;

import android.os.Handler;
import android.os.Message;

import com.blxt.quickactivity.AbstractFullActivityQ;

public abstract class BaseActivity extends AbstractFullActivityQ {

    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doMessage(msg);
        }
    };

    public void doMessage(Message message){

    }
}
