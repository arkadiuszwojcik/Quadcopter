package com.awojcik.qmc.modules.imu;

import android.app.Activity;

import de.greenrobot.event.EventBus;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
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
        this.eventBus = new EventBus();
    }

    @Override
    protected void configure()
    {
        this.bind(Activity.class).toProvider(this.activityProvider);
        this.bind(BluetoothServiceFactory.class).toInstance(this.bluetoothServiceProvider);
        this.bind(IntraModuleMessenger.class).in(Singleton.class);
        this.bind(EventBus.class).toInstance(this.eventBus);
    }

    private final Provider<Activity> activityProvider;
    private final BluetoothServiceFactory bluetoothServiceProvider;
    private final EventBus eventBus;
}
