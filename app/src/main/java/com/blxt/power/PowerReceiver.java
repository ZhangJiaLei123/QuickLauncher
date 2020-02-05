package com.blxt.power;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.blxt.quicklog.QLog;

import java.util.Calendar;

/**
 * 低功耗组件-屏幕点亮与关闭
 * @author: Zhang
 * @date: 2019/7/21 - 18:51
 * @note Created by com.blxt.hn.power.
 */
public class PowerReceiver extends BroadcastReceiver {
    static PowerManager.WakeLock mWakeLock;

    static Context context = null;

    /** 广播名 */
    public static final String MSG_POWER_BROADCASTRECEIVER = "com.blxt.power.screen";

    /** 意图-key*/
    public static final String MSG_POWER_INTENT_KEY_DO = "POWER.SCREEN";

    /** 意图值 */
    public static final String MSG_POWER_VALUE_ON = "on";
    public static final String MSG_POWER_VALUE_OFF = "off";

    protected static void init(Context context){
        PowerReceiver.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(context == null || intent == null){
            QLog.d(this, "参数异常");
            screenOn(PowerReceiver.context);
            return;
        }

        String strDo = intent.getStringExtra(MSG_POWER_INTENT_KEY_DO); // 主意图

        if(strDo != null ){
            if( strDo.equals(MSG_POWER_VALUE_ON) ){
                QLog.d(this, "点亮屏幕");
                screenOn(context);
            }
            else if(strDo.equals(MSG_POWER_VALUE_OFF)){
                QLog.d(this, "关闭屏幕");
                screenOff(context);
            }
        }
        else{
            QLog.d(this, "意图异常");
        }
    }

    /**
     * 点亮屏幕
     * @param context
     */
    @SuppressLint("InvalidWakeLockTag")
    protected static void screenOn(Context context){

        // 先判断屏幕是否点亮
        if(isScreenWork(context)){
            return;
        }

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
/*        PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
        SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
        SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
        FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
        ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
                ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间*/
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE
                , "matrix");
        wakeLock.acquire();
        wakeLock.release();

    }


    /**
     * 关闭屏幕
     * @param context
     */
    protected static void screenOff(Context context){
        // 先判断屏幕是否点亮
        if(!isScreenWork(context)){
            return;
        }
        //获取设备管理Manager
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //创建MyAdminReceiver，并获取到该类的ComponentName，
        ComponentName adminReceiver = new ComponentName(context, PowerReceiver.class);
        //判断设备管理是否已激活
        boolean isActive = policyManager.isAdminActive(adminReceiver);

        if(isActive){
            //如果该应用的设备管理权限已激活，则熄灭屏幕
            policyManager.lockNow();
            Log.i("error","关闭屏幕成功-" + context.getPackageName());

        } else {
            Log.e("error","关闭屏幕失败-" + "请激活设备管理器");
            Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
            //劝说用户开启管理员权限
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"和能低功耗管理");
            context.startActivity(intent);
        }

    }


    /**
     * 判断屏幕是否点亮
     * @param context
     * @return
     */
    protected static Boolean isScreenWork(Context context){
        PowerManager powerManager = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);

        return powerManager.isScreenOn();
    }

    /**
     * 同步方法   得到休眠锁
     * @param context
     * @return
     */
    protected static synchronized void getLock(Context context){
        if (mWakeLock != null){
            return;
        }
        PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                ,PowerService.class.getName());
        mWakeLock.setReferenceCounted(true);

        Calendar c= Calendar.getInstance();
        c.setTimeInMillis((System.currentTimeMillis()));
        int hour =c.get(Calendar.HOUR_OF_DAY);
        if(hour>=23||hour<=6){
            mWakeLock.acquire(5000);
        }else{
            mWakeLock.acquire(300000);
        }
    }

    protected static synchronized void releaseLock()
    {
        if(mWakeLock ==null){
            return;
        }

        if(mWakeLock.isHeld()) {
            mWakeLock.release();
        }

        mWakeLock=null;

    }

}
