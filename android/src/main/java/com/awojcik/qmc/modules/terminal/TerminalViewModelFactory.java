package com.awojcik.qmc.modules.terminal;

import android.app.Activity;

import com.awojcik.qmc.providers.QBluetoothServiceProvider;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class TerminalViewModelFactory 
{
	private final Injector mInjector;
	
	@Inject
	public TerminalViewModelFactory(Provider<Activity> activityProvider, QBluetoothServiceProvider bluetoothServiceProvider)
	{
		this.mInjector = Guice.createInjector(new GuiceContext(activityProvider, bluetoothServiceProvider));
	}
	
	public TerminalViewModel Create()
	{
		return mInjector.getInstance(TerminalViewModel.class);
	}
}
