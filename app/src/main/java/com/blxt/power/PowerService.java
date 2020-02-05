package com.blxt.power;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.blxt.quicklog.QLog;

import static com.blxt.power.PowerReceiver.MSG_POWER_BROADCASTRECEIVER;

/**
 * 低功耗组件启动器
 * @author: Zhang
 * @date: 2019/7/21 - 19:14
 * @note Created by com.blxt.hn.power.
 */
public class PowerService extends Service {

    static Service instance = null;

    public static Service newInstance(Context context){
        QLog.i("PowerService","唤醒服务");
        if(instance == null) {
            //启动后台服务
            Intent service = new Intent(context, PowerService.class);
            service.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startService(service);
        }
        else{
            // 初始化广播助手,并点亮屏幕
            PowerHelp.newInstance(context);
            PowerHelp.openScreen();
        }
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        QLog.i(this,"服务注册成功");

        // 获取休眠锁
        PowerReceiver.getLock(this);

        // 注册广播
        PowerReceiver.init(this);
        PowerReceiver broadcastReceiver = new PowerReceiver();
        registerReceiver(broadcastReceiver,new IntentFilter(MSG_POWER_BROADCASTRECEIVER));

        // 初始化广播助手,并点亮屏幕
        PowerHelp.newInstance(this);
        PowerHelp.openScreen();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        QLog.e(this,"低功耗服务被杀死");
        PowerReceiver.releaseLock();
        super.onDestroy();
    }

}
