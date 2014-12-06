package com.awojcik.qmc.modules.terminal;

import android.app.Activity;

import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class TerminalViewModelFactory 
{
	@Inject
	public TerminalViewModelFactory(Provider<Activity> activityProvider, BluetoothServiceFactory bluetoothServiceProvider)
	{
		this.injector = Guice.createInjector(new GuiceContext(activityProvider, bluetoothServiceProvider));
	}
	
	public TerminalViewModel Create()
	{
		return injector.getInstance(TerminalViewModel.class);
	}

    private final Injector injector;
}
