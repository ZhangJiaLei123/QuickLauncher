package com.example.x6.serial;

/**
 * Created by X6 on 2017/5/4.
 */

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort extends SerialBean{

    private File device;
    private FileDescriptor mFd = null;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(SerialBean serialBean) {
        super(serialBean.name, serialBean.iBaudRate,serialBean.flags);
        // 驱动文件路径
        device = new File(name);

        // 检查访问权限，如果没有读写权限，进行文件操作，修改文件访问权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                //通过挂在到linux的方式，修改文件的操作权限
                Process su = Runtime.getRuntime().exec("/system/xbin/su");
                //一般的都是/system/bin/su路径，有的也是/system/xbin/su
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

    }

    /**
     * 连接
     * @see {com.gps.device.IDevice#connect()}
     * @return
     */
    public boolean connect() {

        if (mFd == null) {
            mFd = open(device.getAbsolutePath(), iBaudRate, flags);
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
        }
        if (mFd == null) {
            isOpen = false;

        } else {
            isOpen = true;
        }

        return isOpen;
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI(调用java本地接口，实现串口的打开和关闭)

    /**
     * @param path     串口设备的据对路径
     * @param baudrate 波特率
     * @param flags    校验位
     */
    private native FileDescriptor open(String path, int baudrate, int flags);

    public native void close();

    static {//加载jni下的C文件库
        System.loadLibrary("serial_port");
    }
}