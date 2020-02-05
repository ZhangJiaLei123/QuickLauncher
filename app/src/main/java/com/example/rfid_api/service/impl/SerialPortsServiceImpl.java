package com.example.rfid_api.service.impl;

import com.example.rfid_api.dao.SerialPortsDao;
import com.example.rfid_api.dao.impl.SerialPortsDaoImpl;
import com.example.rfid_api.service.SerialPortsService;
import com.example.x6.serial.SerialPort;

import java.util.List;

public class SerialPortsServiceImpl implements SerialPortsService {

	SerialPortsDao dao = new SerialPortsDaoImpl();
	
	@Override
	public List<String> findSerialPorts() {
		return dao.findSerialPorts();
	}

	@Override
	public SerialPort open(String port, int baudrate) {
		return dao.open(port, baudrate);
	}

	@Override
	public void close(SerialPort serialPorts) {
		dao.close(serialPorts);
	}

	@Override
	public boolean send(SerialPort serialPorts, byte[] data) {
		return dao.send(serialPorts, data);
	}

	@Override
	public byte[] read(SerialPort serialPorts) {
		return dao.read(serialPorts);
	}
}
