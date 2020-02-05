package com.example.rfid_api.dao.impl.example.noemhost;

public interface IUsbConnState {
    void onUsbConnected();

	void onUsbPermissionDenied();

	void onDeviceNotFound();
}
