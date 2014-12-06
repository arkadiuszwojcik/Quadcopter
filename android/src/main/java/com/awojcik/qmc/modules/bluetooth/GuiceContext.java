package com.awojcik.qmc.modules.bluetooth;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class GuiceContext extends AbstractModule 
{
	private final BluetoothServiceFactory mBluetoothServiceProvider;
	
	public GuiceContext(BluetoothServiceFactory bluetoothServiceProvider)
	{
		this.mBluetoothServiceProvider = bluetoothServiceProvider;
	}
	
	@Override
	protected void configure() 
	{
		this.bind(BluetoothServiceFactory.class).toInstance(this.mBluetoothServiceProvider);
		this.bind(IntraModuleMessenger.class).in(Singleton.class);
	}
}
