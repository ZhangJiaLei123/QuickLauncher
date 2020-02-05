package com.blxt.slave;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blxt.quicklog.Converter;
import com.blxt.quicklog.QLog;

import static com.blxt.slave.SlaveHelp.MSG_SLAVE_INTENT_KEY_DO;
import static com.blxt.slave.SlaveHelp.MSG_SLAVE_REPLY_BROADCASTRECEIVER;

/**
 * 下位机广播信息接收
 */
public class SlaveReplyReceiver extends BroadcastReceiver {
    final String TAG =  "SlaveReplyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(context == null || intent == null){
            Log.d("SlaveReplyReceiver", "参数异常");
            return;
        }

        // 下位机数据
        if(intent.getAction().equals(MSG_SLAVE_REPLY_BROADCASTRECEIVER)){
            byte data[] = intent.getByteArrayExtra(MSG_SLAVE_INTENT_KEY_DO);
            if(data == null){
                QLog.e(TAG, "下位机数据获取异常");
                return;
            }
            QLog.i(TAG, "收到回复", Converter.ToString(data, "%X "));

            if(data.length >= 5){

            }

        }

    }
}
