package com.awojcik.qmc.modules.control;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;

import gueei.binding.Command;
import gueei.binding.observables.CharSequenceObservable;
import gueei.binding.observables.IntegerObservable;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.BluetoothService;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceMessages;
import com.google.inject.Inject;

public class ControlViewModel
{
    public IntegerObservable PrimaryProgress = new IntegerObservable(180);

    public CharSequenceObservable Text = new CharSequenceObservable("ggg");

    public final Command SeekBarChange = new Command()
    {
        public void Invoke(View view, Object... args)
        {
            int val = PrimaryProgress.get();
            ControlViewModel.this.sendCommand("TH " + Integer.toString(val));
            Text.set(Integer.toString(val));
        }
    };

    private ServiceManager bluetoothService;

    private IntraModuleMessenger intraModuleMessenger;

    private Activity activity;

    @Inject
    public ControlViewModel(
            Activity activity,
            BluetoothServiceFactory bluetoothServiceProvider,
            IntraModuleMessenger intraModuleMessenger)
    {
        this.activity = activity;
        this.bluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
        this.intraModuleMessenger = intraModuleMessenger;
    }

    private  void sendCommand(String command)
    {
        if (!command.endsWith("\n")) command = command + "\n";

        Message message = this.getSendCommandMessage(command);
        try
        {
            this.bluetoothService.send(message);
        }
        catch (RemoteException e)
        {
        }
    }

    private Message getSendCommandMessage(String strData)
    {
        return BluetoothServiceMessages.createSendDataChunkMessage(strData);
    }

    class BluetoothServiceHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
        }
    }
}
