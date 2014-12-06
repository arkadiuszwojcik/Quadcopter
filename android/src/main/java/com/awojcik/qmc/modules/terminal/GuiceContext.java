package com.awojcik.qmc.modules.terminal;

import android.app.Activity;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.terminal.commands.SendCommand;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class GuiceContext extends AbstractModule
{
	public GuiceContext(Provider<Activity> activityProvider, BluetoothServiceFactory bluetoothServiceProvider)
	{
		this.activityProvider = activityProvider;
		this.bluetoothServiceProvider = bluetoothServiceProvider;
	}
	
	@Override
	protected void configure() 
	{
		this.bind(Activity.class).toProvider(this.activityProvider);
		this.bind(BluetoothServiceFactory.class).toInstance(this.bluetoothServiceProvider);
		this.bind(IntraModuleMessenger.class).in(Singleton.class);
		this.bind(SendCommand.class);
		this.bind(TerminalViewModel.class);
	}

    private final Provider<Activity> activityProvider;
    private final BluetoothServiceFactory bluetoothServiceProvider;
}
