package com.awojcik.qmc.modules.settings;

import android.os.Message;
import android.os.Handler;
import android.util.Log;

import com.awojcik.qmc.modules.common.IIntraModuleMessageListener;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.settings.commands.LoadCommand;
import com.awojcik.qmc.modules.settings.commands.SaveCommand;
import com.awojcik.qmc.modules.settings.messages.LoadMessage;
import com.awojcik.qmc.modules.settings.messages.SaveMessage;
import com.awojcik.qmc.providers.QBluetoothServiceProvider;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.QBluetoothService;

import java.util.Formatter;
import java.util.Locale;

import gueei.binding.observables.StringObservable;

public class SettingsViewModel
{
    public final LoadCommand LoadCommand;
    public final SaveCommand SaveCommand;

    public final StringObservable YawP = new StringObservable("");
    public final StringObservable YawI = new StringObservable("");
    public final StringObservable YawD = new StringObservable("");

    public final StringObservable RollP = new StringObservable("");
    public final StringObservable RollI = new StringObservable("");
    public final StringObservable RollD = new StringObservable("");

    public final StringObservable PitchP = new StringObservable("");
    public final StringObservable PitchI = new StringObservable("");
    public final StringObservable PitchD = new StringObservable("");

    public final StringObservable EngineMin = new StringObservable("");
    public final StringObservable EngineMax = new StringObservable("");

    public final StringObservable AccOffsetX = new StringObservable("");
    public final StringObservable AccOffsetY = new StringObservable("");
    public final StringObservable AccOffsetZ = new StringObservable("");

    private final IntraModuleMessenger intraModuleMessenger;
    private final ServiceManager bluetoothService;

    public SettingsViewModel(
            QBluetoothServiceProvider bluetoothServiceProvider,
            IntraModuleMessenger intraModuleMessenger,
            LoadCommand loadCommand,
            SaveCommand saveCommand)
    {
        this.bluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
        this.intraModuleMessenger = intraModuleMessenger;
        this.intraModuleMessenger.register(new IntraMessageListener());

        this.LoadCommand = loadCommand;
        this.SaveCommand = saveCommand;
    }

    private String toString()
    {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("")
    }

    class IntraMessageListener implements IIntraModuleMessageListener
    {
        public void onMessage(Object message)
        {
            if (message instanceof LoadMessage)
            {
            }
            else if (message instanceof SaveMessage)
            {
            }
        }
    }

    class BluetoothServiceHandler extends Handler
    {
        private static final String TAG = "SettingsViewModel.BluetoothServiceHandler";

        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                if (msg.what == QBluetoothService.MSG_DEVICE_CONNECTED_RESPONSE)
                {
                    //String address = msg.getData().getString(QBluetoothService.KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS);
                    //TerminalViewModel.this.appendLine("Device connected: " + address);
                }

                if (msg.what == QBluetoothService.MSG_DATA_CHUNK_RESPONSE)
                {
                    //String data = msg.getData().getString(QBluetoothService.KEY_MSG_DATA_CHUNK_RESPONSE_DATA);
                    //TerminalViewModel.this.appendLine("New data: " + data);
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
