package com.example.rfid_api.dao;

import com.example.x6.serial.SerialPort;

import java.util.List;

public interface SerialPortsDao {

	List<String> findSerialPorts();

	SerialPort open(String port, int baudrate);

	boolean send(SerialPort serialPorts, byte[] data);

	byte[] read(SerialPort serialPorts);

	void close(SerialPort serialPorts);
}
