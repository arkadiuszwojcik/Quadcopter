package com.awojcik.qmc.services.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class BluetoothBroadcastReceiver extends BroadcastReceiver
{
	public BluetoothBroadcastReceiver(BluetoothBroadcastReceiverCallback callback)
	{
		this.callback = callback;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String action = intent.getAction();
		
        if (BluetoothDevice.ACTION_FOUND.equals(action)) 
        {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            this.callback.deviceDiscovered(device.getName(), device.getAddress(), device.getBluetoothClass());
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
        {
        	this.callback.discoveryStarted();
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) 
        {
            this.callback.discoveryFinished();
        }
	}
	
	private final BluetoothBroadcastReceiverCallback callback;
}