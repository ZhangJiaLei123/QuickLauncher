package com.blxt.slave;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


public class SlaveHelp {
    /** 广播名 */
    public static final String MSG_SLAVE_BROADCASTRECEIVER = "com.blxt.slave.broadcast";
    public static final String MSG_SLAVE_REPLY_BROADCASTRECEIVER = "com.blxt.slave.broadcast.provider";

    /** 意图-key*/
    public static final String MSG_SLAVE_INTENT_KEY_DO = "slave.data";

    static Context context = null;

    public static SlaveHelp newInstance(@NonNull Context context){
        if(context == null){
            throw new NullPointerException("没有初始化异常,Context不能为空");
        }
        if(SlaveHelp.context == null) {
            SlaveHelp.context = context;
        }

        return new SlaveHelp();
    }

    private SlaveHelp(){

    }


    /**
     * 发送数据
     * @param data
     */
    public synchronized static void sendData(byte data[]){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        Intent intent=new Intent(MSG_SLAVE_BROADCASTRECEIVER);
        intent.putExtra(MSG_SLAVE_INTENT_KEY_DO, data);
        context.sendBroadcast(intent);
    }

    /**
     * 回复数据
     * @param data
     */
    public synchronized static void replyData(byte data[]){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        Intent intent=new Intent(MSG_SLAVE_REPLY_BROADCASTRECEIVER);
        intent.putExtra(MSG_SLAVE_INTENT_KEY_DO, data);
        context.sendBroadcast(intent);
    }

}
