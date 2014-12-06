package com.awojcik.qmc.modules.bluetooth;

import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class BluetoothViewModelFactory 
{	
	private final Injector mInjector;
	
	@Inject
	public BluetoothViewModelFactory(BluetoothServiceFactory bluetoothServiceProvider)
	{
		this.mInjector = Guice.createInjector(new GuiceContext(bluetoothServiceProvider));
	}
	
	public BluetoothViewModel Create()
	{
		return mInjector.getInstance(BluetoothViewModel.class);
	}
}
