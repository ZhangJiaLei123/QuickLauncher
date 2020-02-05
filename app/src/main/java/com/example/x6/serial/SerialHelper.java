package com.example.x6.serial;

import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


/**
 * 串口读取工具
 */
public abstract class SerialHelper {
    public static final int MAX_DATA_LEN = 498; // 读长度

    public SerialBean serialBean;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;

    //----------------------------------------------------
    public SerialHelper(SerialBean serialBean) {
        this.serialBean = serialBean;
    }


    //----------------------------------------------------
    public boolean open() throws SecurityException, InvalidParameterException {
        mSerialPort = new SerialPort(serialBean);
        serialBean.isOpen =  mSerialPort.connect();
        if (!serialBean.isOpen){
            return false;
        }
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
        mSendThread = new SendThread();
        mSendThread.setSuspendFlag();
        mSendThread.start();
        return true;
    }

    //----------------------------------------------------
    public void close() {
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        serialBean.setOpen(false);
    }

    //----------------------------------------------------
    public void send(byte[] bOutArray, int nSize) {
        try {
            mOutputStream.write(bOutArray, 0, nSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 串口读线程
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                if (mInputStream == null) return;
                byte[] buffer = new byte[MAX_DATA_LEN];
                int size = 0;
                try {
                    size = mInputStream.read(buffer);
                } catch (Exception e) {
                    Log.e("串口读取异常", serialBean.toString(), e);
                    onError(serialBean, e);
                    return;
                }

                if (size > 0) {
                    SerialDataBean ComRecData = new SerialDataBean(serialBean, buffer, size);
                    onDataReceived(ComRecData);
                }
                try {
                    sleep(50);
                } catch (Exception e) {
                }
            }
        }
    }

    //----------------------------------------------------
    private class SendThread extends Thread {
        public boolean suspendFlag = true;

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                send(getbLoopData(), 1);
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }

    //----------------------------------------------------
    public boolean isOpen() {
        return serialBean.isOpen;
    }

    //----------------------------------------------------
    public byte[] getbLoopData() {
        return _bLoopData;
    }

    //----------------------------------------------------
    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    //----------------------------------------------------
    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    //----------------------------------------------------
    public int getiDelay() {
        return iDelay;
    }

    //----------------------------------------------------
    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    //----------------------------------------------------
    public void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }
    }

    //----------------------------------------------------
    public void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }
    }

    //----------------------------------------------------
    protected abstract void onDataReceived(SerialDataBean ComRecData);

    protected abstract void onError(SerialBean serialBean, Exception e);

}