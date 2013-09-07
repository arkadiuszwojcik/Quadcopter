package com.awojcik.qmc.modules.bluetooth;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.providers.QBluetoothServiceProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class GuiceContext extends AbstractModule 
{
	private final QBluetoothServiceProvider mBluetoothServiceProvider;
	
	public GuiceContext(QBluetoothServiceProvider bluetoothServiceProvider)
	{
		this.mBluetoothServiceProvider = bluetoothServiceProvider;
	}
	
	@Override
	protected void configure() 
	{
		this.bind(QBluetoothServiceProvider.class).toInstance(this.mBluetoothServiceProvider);
		this.bind(IntraModuleMessenger.class).in(Singleton.class);
	}
}
