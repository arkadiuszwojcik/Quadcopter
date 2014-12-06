package com.awojcik.qmc.services.bluetooth;

import android.bluetooth.BluetoothClass;

interface BluetoothBroadcastReceiverCallback
{
	public void deviceDiscovered(String name, String address, BluetoothClass deviceClass);
	public void discoveryStarted();
	public void discoveryFinished();
}
