package com.awojcik.qmc.modules.settings;

import android.app.Activity;

import com.awojcik.qmc.arduino.ArduinoMessageSerialiser;
import com.awojcik.qmc.arduino.settings.ArduinoSettingsSerialiser;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.settings.commands.LoadCommand;
import com.awojcik.qmc.modules.settings.commands.SaveCommand;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class GuiceContext extends AbstractModule
{
    private final Provider<Activity> activityProvider;
    private final BluetoothServiceFactory bluetoothServiceProvider;

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
        this.bind(ArduinoMessageSerialiser.class).in(Singleton.class);
        this.bind(ArduinoSettingsSerialiser.class).in(Singleton.class);
        this.bind(LoadCommand.class);
        this.bind(SaveCommand.class);
        this.bind(SettingsViewModel.class);
    }
}
