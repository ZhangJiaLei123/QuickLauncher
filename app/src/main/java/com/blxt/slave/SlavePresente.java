package com.blxt.slave;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blxt.quicklog.QLog;
import com.example.x6.serial.SerialBean;
import com.example.x6.serial.SerialDataBean;
import com.example.x6.serial.SerialHelper;

import java.util.Timer;
import java.util.TimerTask;

import static com.heneng.launcher.model.MConstant.MSGID_SLAVE_DATA_DISCONNECT;
import static com.heneng.launcher.model.MConstant.MSGID_SLAVE_DATA_ERROR;
import static com.heneng.launcher.model.MConstant.MSGID_SLAVE_DATA_UP;

/**
 * 下位机处理
 */
public class SlavePresente{

    static final String TAG = "SlavePresente";

    SerialBean serialBean; // 串口信息
    SerialPortTool serialPortTool = null;   ///< 串口工具

    public Handler callback = null;         ///< 事件回调

    byte liveCount = 80;                     ///< 心跳计数
    private Timer timer;                    ///< 心跳定时
    byte slaveModel[] = new byte[8];        ///< 下位机状态 心跳,门状态(1/2),门动作(0/1/2),锁状态(1/2),锁动作(0/1/2); 0无状态1开2关
    TimerTask taskUpData = new TimerTask() {
        @Override
        public void run() {
            liveCount++;
            if(liveCount % 2 == 0){
                Log.i(TAG,"发送心跳包");
                live();
                returnState(slaveModel);
            }
            else{ // 获取下位机状态
                // 获取状态前,清空状态
                for(int i = 0; i < slaveModel.length; i++){
                    slaveModel[i] = 0;
                }
                getStateAll();
            }

            if(liveCount >= 0xFF){
                liveCount = 80;
            }

        }
    };


    public SlavePresente(String m_szDevice, int m_nBaudrate){
        serialBean = new SerialBean(m_szDevice, m_nBaudrate, 0);
        serialPortTool = new SerialPortTool(serialBean);

    }

    public void setCallback(Handler callback) {
        this.callback = callback;
    }

    public boolean isOPen(){
        return serialPortTool.serialBean.isOpen();
    }

    /**
     * 连接下位机
     * @return
     */
    public boolean open(){
        if(isOPen()){
            return true;
        }

        // 先开串口
        if(!serialPortTool.open()){
            QLog.e(TAG, "下位机串口启动失败");
           return false;
        }
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        // 1秒更新一次
        timer.schedule(taskUpData,5000,500);
        return true;
    }

    /**
     * 关闭连接
     */
    public void close(){
        if(serialPortTool != null) {
            serialPortTool.close();
        }
        // 关闭线程池
        if(timer != null)
            timer.cancel();
        callback.sendEmptyMessage(MSGID_SLAVE_DATA_DISCONNECT);
        QLog.e(TAG, "下位机连接断开");
    }

    // 发送心跳包
    private void live(){
        byte data[] = new byte[]{(byte) 0xBB, 0x66, (byte) 0xde,
                liveCount,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
                , 0 , 0};
        Msend(data);
    }

    // 发送开门指令
    public void openLock(){
        byte data[] = new byte[]{(byte) 0xBB, 0x66, (byte) 0xd0,
                01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
                , 0 ,0};
        Msend(data);
    }

    // 发送关门指令
    public void closeLock(){
        byte data[] = new byte[]{(byte) 0xBB, 0x66, (byte) 0xd0,
                00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
                , 0 ,0};
        Msend(data);
    }

    /**
     * 获取下位机状态
     */
    public void getStateAll(){
        byte data[] = new byte[]{(byte) 0xBB, 0x66, (byte) 0xda,
                01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
                , 0, 0};
        Msend(data);
    }

    /**
     * 发送协议包
     * @param data
     */
    private void Msend(byte data[]){
        short check = 0;
        for(int i = 0; i < data.length - 4; i++){
            check += (data[i] & 0xff);
        }
        data[data.length - 1] = (byte)(check >> 8);
        data[data.length - 2] = (byte)(check & 0xFF);

        serialPortTool.send(data);
    }

    /**
     * 接收数据解析
     * @param ComRecData
     */
    protected void dataReceived(SerialDataBean ComRecData) {

        if(ComRecData.nSize >= 5){

            boolean fal ;
            fal = ComRecData.bRec[0] == (byte)0x66
                    && ComRecData.bRec[1] == (byte)0xbb;
            if(fal)
            {
                switch (ComRecData.bRec[2]){
                    case (byte) 0xd0:
                        break;
                    case (byte) 0xda: // "综合数据",跟新状态
                        putEventDoor(ComRecData.bRec[3]);// 门状态
                        putEventLock(ComRecData.bRec[4]);// 锁状态
                        //returnState(slaveModel);
                        return;
                    case (byte) 0xDE: // 收到心跳包
                        slaveModel[0] = ComRecData.bRec[3];
                        return;
                    default:
                        //Log.d(TAG, "未知指令" +  Converter.ToHexString(ComRecData.bRec, "%s ") );
                        break;
                }
            }
        }
        QLog.d(TAG, "未知指令"  );
    }

    short lastDoorStateClose = 0;
    short lastDoorStateOpen = 0;
    short lastLockStateClose = 0;
    short lastLockStateOpen = 0;

    // 门状态
    public void putEventDoor(int doorState) {

        if (doorState == 0) { //柜门开
            lastDoorStateOpen++;
            lastDoorStateClose = -1;
            slaveModel[1] = 1;
            if (lastDoorStateOpen >= 1) {
                lastDoorStateOpen = 1;
            }

        } else { // 柜门关
            lastDoorStateClose++;
            lastDoorStateOpen = -1;
            slaveModel[1] = 2;
            if (lastDoorStateClose >= 1) {
                lastDoorStateClose = 1;
            }
        }

        slaveModel[2] = 0;
        // 关门动作
        if (lastDoorStateClose == 0) {
            slaveModel[2] = 2;
        }
        // 开门动作
        if (lastDoorStateOpen == 0) {
            slaveModel[2] = 1;
        }

    }

    /**
     * 锁状态
     *
     * @param onder2
     */
    public void putEventLock(int onder2) {
        if (onder2 == 1) { // 锁开
            lastLockStateOpen++;
            lastLockStateClose = -1;
            slaveModel[3] = 1;
            if (lastLockStateOpen >= 1) {
                lastLockStateOpen = 1;
            }
        } else { // 锁关
            lastLockStateClose++;
            lastLockStateOpen = -1;
            slaveModel[3] = 2;
            if (lastLockStateClose >= 1) {
                lastLockStateClose = 1;
            }
        }

        slaveModel[4] = 0;
        // 关门动作
        if (lastLockStateClose == 0) {
            slaveModel[4] = 2;
        }
        // 开门动作
        if (lastLockStateOpen == 0) {
            slaveModel[4] = 1;
        }
    }

    protected void returnState(byte slaveModel[]){
        Message eventBean = new Message();
        // 发送更新
        eventBean.what = MSGID_SLAVE_DATA_UP;
        eventBean.obj = slaveModel;
        callback.sendMessage(eventBean);
    }
    /**
     * 串口工具
     */
    class SerialPortTool extends SerialHelper {

        public SerialPortTool( SerialBean serialBean){
            super(serialBean);
        }

        /**
         * 下位机指令接收
         * @param ComRecData
         */
        @Override
        protected void onDataReceived(SerialDataBean ComRecData) {
            QLog.d(TAG, "收到串口数据" +  ComRecData.name );
            Message message = new Message();
            message.what = MSGID_SLAVE_DATA_UP;
            message.obj = ComRecData;
            callback.sendMessage(message);
            dataReceived(ComRecData);

        }

        @Override
        protected void onError(SerialBean serialBean, Exception e) {
            QLog.e(TAG, "致命错误", "串口读取异常" );
            e.printStackTrace();
            close(); // 关闭连接
            callback.sendEmptyMessage(MSGID_SLAVE_DATA_ERROR);
        }

        @Override
        public boolean open() {
            try
            {
                return super.open();
            } catch (Exception e) {
                return false;
            }
        }

        public void send(byte data[]){
            super.send(data, data.length);
        }

    }
}
