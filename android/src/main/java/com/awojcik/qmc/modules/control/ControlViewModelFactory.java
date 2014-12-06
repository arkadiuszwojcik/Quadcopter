package com.awojcik.qmc.modules.control;

import android.app.Activity;

import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ControlViewModelFactory
{
    @Inject
    public ControlViewModelFactory(Provider<Activity> activityProvider, BluetoothServiceFactory bluetoothServiceProvider)
    {
        this.injector = Guice.createInjector(new com.awojcik.qmc.modules.imu.GuiceContext(activityProvider, bluetoothServiceProvider));
    }

    public ControlViewModel Create()
    {
        return injector.getInstance(ControlViewModel.class);
    }

    private final Injector injector;
}
