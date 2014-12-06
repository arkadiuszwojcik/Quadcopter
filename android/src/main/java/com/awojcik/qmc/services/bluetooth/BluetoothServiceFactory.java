package com.awojcik.qmc.services.bluetooth;

import android.app.Activity;
import android.os.Handler;

import com.awojcik.qmc.services.ServiceManager;
import com.google.inject.Inject;

public class BluetoothServiceFactory
{
	private Activity activity;

	@Inject
	public BluetoothServiceFactory(Activity activity)
	{
		this.activity = activity;
	}
	
	public ServiceManager Create(Handler messageHandler)
	{
		return new ServiceManager(this.activity, BluetoothService.class, messageHandler);
	}
}
