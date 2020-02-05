package com.example.rfid_api.service;

import com.example.x6.serial.SerialPort;

import java.util.List;


public interface SerialPortsService {
	public List<String> findSerialPorts();

	public SerialPort open(String port, int baudrate);

	public void close(SerialPort serialPorts);

	public boolean send(SerialPort serialPorts, byte[] data);

	public byte[] read(SerialPort serialPorts);
}
