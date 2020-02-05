package com.example.rfid_api.dao.impl.example.x6.serial;


/**
 * @author benjaminwan
 */
public class SerialDataBean extends SerialBean {
    public byte[] bRec = null;
    public int nSize = 0;

    public SerialDataBean(SerialBean serialBean, byte[] buffer, int size) {
        super(serialBean.name, serialBean.iBaudRate,serialBean.flags);

        bRec = new byte[size];
        System.arraycopy(buffer, 0, bRec, 0, size);
        nSize = size;
    }
}