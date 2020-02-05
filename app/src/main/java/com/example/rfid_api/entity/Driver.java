package com.example.rfid_api.entity;

import android.util.Log;

import java.io.File;
import java.util.Vector;

public class Driver {
	private static final String TAG = "SerialPort";
	private String mDriverName;
	private String mDeviceRoot;
	Vector<File> mDevices = null;

	public Driver(String name, String root) {
		mDriverName = name;
		mDeviceRoot = root;
	}

	public String getName() {
		return mDriverName;
	}

	public Vector<File> getDevices() {
		try {
			if (mDevices == null) {
				mDevices = new Vector<File>();
				File dev = new File("/dev");
				File[] files = dev.listFiles();
				int i;
				for (i = 0; i < files.length; i++) {
					if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
						Log.d(TAG, "Found new device: " + files[i]);
						mDevices.add(files[i]);
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return mDevices;
	}
}
