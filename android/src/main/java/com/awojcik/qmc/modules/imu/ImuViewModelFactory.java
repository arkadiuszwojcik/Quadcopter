package com.awojcik.qmc.modules.imu;

import android.app.Activity;

import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ImuViewModelFactory
{
    @Inject
    public ImuViewModelFactory(Provider<Activity> activityProvider, BluetoothServiceFactory bluetoothServiceProvider)
    {
        this.injector = Guice.createInjector(new GuiceContext(activityProvider, bluetoothServiceProvider));
    }

    public ImuViewModel Create()
    {
        return injector.getInstance(ImuViewModel.class);
    }

    private final Injector injector;
}
