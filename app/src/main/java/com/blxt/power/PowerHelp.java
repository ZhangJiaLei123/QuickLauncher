package com.blxt.power;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import static com.blxt.power.PowerReceiver.MSG_POWER_BROADCASTRECEIVER;


public class PowerHelp {
    static Context context = null;

    public static PowerHelp newInstance(@NonNull Context context){
        if(context == null){
            throw new NullPointerException("没有初始化异常,Context不能为空");
        }
        if(PowerHelp.context == null) {
            PowerHelp.context = context;
        }

        return new PowerHelp();
    }

    private PowerHelp(){

    }

    /**
     * 点亮屏幕
     */
    public static void openScreen(){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        Intent intent=new Intent(MSG_POWER_BROADCASTRECEIVER);
        intent.putExtra(PowerReceiver.MSG_POWER_INTENT_KEY_DO, PowerReceiver.MSG_POWER_VALUE_ON);
        context.sendBroadcast(intent);
    }

    /**
     * 关闭屏幕
     */
    public static void closeScreen(){
        if(context == null){
            throw new NullPointerException("没有初始化");
        }
        Intent intent=new Intent(MSG_POWER_BROADCASTRECEIVER);
        intent.putExtra(PowerReceiver.MSG_POWER_INTENT_KEY_DO, PowerReceiver.MSG_POWER_VALUE_OFF);
        context.sendBroadcast(intent);
    }

}
