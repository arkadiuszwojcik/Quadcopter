package com.awojcik.qmc.providers;

import android.app.Activity;
import android.os.Handler;

import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.QBluetoothService;
import com.google.inject.Inject;

public class QBluetoothServiceProvider 
{
	private Activity mActivity;

	@Inject
	public QBluetoothServiceProvider(Activity activity)
	{
		this.mActivity = activity;
	}
	
	public ServiceManager Create(Handler messageHandler)
	{
		ServiceManager bluetoothService;
		bluetoothService = new ServiceManager(this.mActivity, QBluetoothService.class, messageHandler);
		
		return bluetoothService;
	}
}
