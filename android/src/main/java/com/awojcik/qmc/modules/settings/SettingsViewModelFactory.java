package com.awojcik.qmc.modules.settings;

import android.app.Activity;

import com.awojcik.qmc.modules.terminal.*;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class SettingsViewModelFactory
{
    @Inject
    public SettingsViewModelFactory(Provider<Activity> activityProvider, BluetoothServiceFactory bluetoothServiceProvider)
    {
        this.injector = Guice.createInjector(new com.awojcik.qmc.modules.settings.GuiceContext(activityProvider, bluetoothServiceProvider));
    }

    public SettingsViewModel Create()
    {
        return injector.getInstance(SettingsViewModel.class);
    }

    private final Injector injector;
}
