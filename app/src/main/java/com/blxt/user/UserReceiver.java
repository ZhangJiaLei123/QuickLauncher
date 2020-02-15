package com.blxt.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blxt.power.PowerHelp;
import com.blxt.quicklog.QLog;
import com.blxt.user.bean.UserHelper;

import static com.blxt.user.bean.UserHelper.MODEL_LOGIN;
import static com.blxt.user.bean.UserHelper.MSG_USER_BROADCASTRECEIVER;
import static com.blxt.user.bean.UserHelper.MSG_USER_KEY_USER_ACTION;
import static com.blxt.user.bean.UserHelper.MSG_USER_KEY_USER_INFO;


/**
 * 指纹客户端广播
 */
public class UserReceiver extends BroadcastReceiver {
    final String TAG = "UserReceiver";
    public static final String USERRECEIVER_NAME = "com.blxt.launcher.user.broadcast";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (context == null || intent == null) {
            Log.d("UserReceiver", "参数异常");
            return;
        }
        UserHelper userHelper =  UserHelper.newInstance(context);

        // 下位机数据
        if (intent.getAction().equals(MSG_USER_BROADCASTRECEIVER)
                || intent.getAction().equals(USERRECEIVER_NAME)) {
            // 请求当前用户信息
            String str = intent.getStringExtra(MSG_USER_KEY_USER_INFO);
            int _id = intent.getIntExtra(MSG_USER_KEY_USER_ACTION, -1);

            if(str != null){
                QLog.i(TAG,  "操作:" + _id,"用户信息", str);
            }
            else {
                if(_id < 10){
                    QLog.e(TAG, "空用户信息", _id + "");

                    // 进入登陆
                    // userHelper.openUserActivity(USERRECEIVER_NAME, MODEL_LOGIN);
                }
            }

            PowerHelp.newInstance(context);
            PowerHelp.openScreen();

        }


    }
}
