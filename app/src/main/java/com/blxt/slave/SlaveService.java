package com.blxt.slave;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.blxt.quicklog.QLog;
import com.example.x6.serial.SerialPortFinder;
import com.heneng.quicknoti.TipToast;

import java.util.Timer;
import java.util.TimerTask;

import static com.heneng.launcher.model.MConstant.MSGID_SLAVE_DATA_CONNECT;
import static com.heneng.launcher.model.MConstant.MSGID_SLAVE_DATA_ERROR;
import static com.heneng.launcher.model.MConstant.MSGID_SLAVE_DATA_UP;

/**
 * 下位机服务
 */
public class SlaveService extends Service {
    final static String TAG = "SlaveService";
    static SlaveService instances = null;
    int        m_nBaudrate = 115200;
    String     m_szDevice = "/dev/ttyS0";

    private TipToast tipToast = TipToast.getInstance();

    SlavePresente slave = null;
    SlaveHelp slaveHelp = null;

    Timer timer = null; // 连接下位机的定时器
    TimerTask timerTask = null;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            doMessage(msg);
        }
    };

    public void doMessage( Message message) {

        switch (message.what){
            case MSGID_SLAVE_DATA_UP: // 处理下位机数据
                byte slaveModel[] = (byte[])message.obj;
                slaveHelp.sendData(slaveModel);
                break;
            case MSGID_SLAVE_DATA_ERROR: // 下位机连接异常,需要重连
                slaveHelp.sendData(new byte[]{-2, 0, 0, 0, 0}); // 下位机异常
                reStart();
                break;
            case MSGID_SLAVE_DATA_CONNECT:
                slaveHelp.sendData(new byte[]{-1, 0, 0, 0, 0}); // 下位机连接断开
                break;
        }
    }


    /**
     * 启动客户端
     * @param context
     */
    public static SlaveService getInstances(Context context){
        if(instances == null){
            Intent instances = new Intent(context, SlaveService.class);
            instances.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startService(instances);
        }
        String[] devs = SerialPortFinder.getAllDevices();
        QLog.i(TAG, "串口列表");
        for(String s : devs){
            QLog.i(TAG, "串口:" +  s);
        }

        return instances;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"下位机服务开始");
        slaveHelp = SlaveHelp.newInstance(getBaseContext());
        // 启动

        slave = new SlavePresente(m_szDevice, m_nBaudrate);
        slave.setCallback(mHandler);


        reStart();
        instances = this;
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        slave.close();
        instances = null;
        QLog.i(TAG,"下位机服务停止");
    }

    /**
     * 启动指纹服务
     */
    private void starFingerService(){

        // 指纹
        //FingerService.star(this);
    }

    /**
     * 开锁
     */
    public void openDoor(){
        if (slave != null && slave.isOPen()) {
            slave.openLock();
        }
    }

    /**
     * 关锁
     */
    public void closeDoor(){
        if (slave != null && slave.isOPen()) {
            slave.closeLock();
            QLog.d(TAG, "发送", "关门指令");
        }
        else{
            QLog.e(TAG, "发送关门指令", "失败,下位机未连接");
        }
    }


    /**
     * 重启下位机连接
     * @return
     */
    public boolean reStart(){

        if(timer != null){
            return false;
        }

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // 先开串口
                if(!slave.open()){
                    QLog.e( TAG, "下位机服务启动失败");
                }
                else{
                    if(timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if(timerTask != null){
                        timerTask.cancel();
                        timerTask = null;
                    }
                    QLog.i(TAG, "下位机服务已启动,准备连接指纹设备");
                }

            }
        };
        timer.schedule(timerTask, 0, 2000);
        return true;
    }

}
