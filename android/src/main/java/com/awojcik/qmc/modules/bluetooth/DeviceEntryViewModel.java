package com.awojcik.qmc.modules.bluetooth;

import android.bluetooth.BluetoothClass;
import gueei.binding.observables.StringObservable;

class DeviceEntryViewModel
{
	public final StringObservable Name = new StringObservable();
	public final StringObservable Address = new StringObservable();
	public final BluetoothClass DeviceClass;
	
	public DeviceEntryViewModel(String name, String address, BluetoothClass deviceClass)
	{
		this.Name.set(name);
		this.Address.set(address);
		this.DeviceClass = deviceClass;
	}
}
