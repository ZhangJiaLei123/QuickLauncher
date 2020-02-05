package com.example.rfid_api.dao.impl.example.x6.serial;

/**
 * 串口信息
 */
public class SerialBean {
    public String name = "/dev/s3c2410_serial0"; // 串口路径
    public int iBaudRate = 9600; // 波特率
    public int flags; // 停止位
    protected boolean isOpen = false;

    public SerialBean(String name, int iBaudRate, int flags) {
        this.name = name;
        this.iBaudRate = iBaudRate;
        this.flags = flags;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen){
        this.isOpen = isOpen;
    }


    @Override
    public String toString() {
        return "SerialBean{" +
                "串口='" + name + '\'' +
                ", 波特率=" + iBaudRate +
                ", 状态 = }" + (isOpen ? "开启" : "关闭");
    }


}
