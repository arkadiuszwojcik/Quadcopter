package com.awojcik.qmc.modules.settings;

import android.os.Message;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.ArduinoMessageSerialiser;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoFloatMessage;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoIntegerMessage;
import com.awojcik.qmc.arduino.messages.outgoing.ArduinoGetSettingsMessage;
import com.awojcik.qmc.arduino.settings.ArduinoSettings;
import com.awojcik.qmc.arduino.settings.ArduinoSettingsSerialiser;
import com.awojcik.qmc.modules.common.IIntraModuleMessageListener;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.settings.commands.LoadCommand;
import com.awojcik.qmc.modules.settings.commands.SaveCommand;
import com.awojcik.qmc.modules.settings.messages.LoadMessage;
import com.awojcik.qmc.modules.settings.messages.SaveMessage;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceMessages;
import com.google.inject.Inject;

import gueei.binding.observables.StringObservable;

public class SettingsViewModel
{
    public final LoadCommand LoadCommand;
    public final SaveCommand SaveCommand;

    public final StringObservable YawMasterP = new StringObservable("");
    public final StringObservable YawMasterI = new StringObservable("");
    public final StringObservable YawMasterD = new StringObservable("");

    public final StringObservable YawSlaveP = new StringObservable("");
    public final StringObservable YawSlaveI = new StringObservable("");
    public final StringObservable YawSlaveD = new StringObservable("");

    public final StringObservable RollMasterP = new StringObservable("");
    public final StringObservable RollMasterI = new StringObservable("");
    public final StringObservable RollMasterD = new StringObservable("");

    public final StringObservable RollSlaveP = new StringObservable("");
    public final StringObservable RollSlaveI = new StringObservable("");
    public final StringObservable RollSlaveD = new StringObservable("");

    public final StringObservable PitchMasterP = new StringObservable("");
    public final StringObservable PitchMasterI = new StringObservable("");
    public final StringObservable PitchMasterD = new StringObservable("");

    public final StringObservable PitchSlaveP = new StringObservable("");
    public final StringObservable PitchSlaveI = new StringObservable("");
    public final StringObservable PitchSlaveD = new StringObservable("");

    public final StringObservable MinPID = new StringObservable("");
    public final StringObservable MaxPID = new StringObservable("");

    public final StringObservable EngineMin = new StringObservable("");
    public final StringObservable EngineMax = new StringObservable("");

    public final StringObservable AccOffsetX = new StringObservable("");
    public final StringObservable AccOffsetY = new StringObservable("");
    public final StringObservable AccOffsetZ = new StringObservable("");

    private final ArduinoSettings arduinoSettings = new ArduinoSettings();

    private final IntraModuleMessenger intraModuleMessenger;
    private final ServiceManager bluetoothService;
    private final ArduinoMessageSerialiser arduinoMessageSerialiser;
    private final ArduinoSettingsSerialiser arduinoSettingsSerialiser;

    @Inject
    public SettingsViewModel(
            BluetoothServiceFactory bluetoothServiceProvider,
            IntraModuleMessenger intraModuleMessenger,
            ArduinoMessageSerialiser arduinoMessageSerialiser,
            ArduinoSettingsSerialiser arduinoSettingsSerialiser,
            LoadCommand loadCommand,
            SaveCommand saveCommand)
    {
        this.bluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
        this.intraModuleMessenger = intraModuleMessenger;
        this.intraModuleMessenger.register(new IntraMessageListener());
        this.arduinoMessageSerialiser = arduinoMessageSerialiser;
        this.arduinoSettingsSerialiser = arduinoSettingsSerialiser;

        this.LoadCommand = loadCommand;
        this.SaveCommand = saveCommand;

        this.updateBindings();
    }

    private void onArduinoMessage(ArduinoMessage message)
    {
        if (message instanceof ArduinoFloatMessage || message instanceof ArduinoIntegerMessage)
        {
            this.arduinoSettingsSerialiser.updateFromMessage(this.arduinoSettings, message);
            this.updateBindings();
        }
    }

    private void updateBindings()
    {
        this.RollMasterP.set(Float.toString(this.arduinoSettings.getRollMasterPID()[0]));
        this.RollMasterI.set(Float.toString(this.arduinoSettings.getRollMasterPID()[1]));
        this.RollMasterD.set(Float.toString(this.arduinoSettings.getRollMasterPID()[2]));

        this.RollSlaveP.set(Float.toString(this.arduinoSettings.getRollSlavePID()[0]));
        this.RollSlaveI.set(Float.toString(this.arduinoSettings.getRollSlavePID()[1]));
        this.RollSlaveD.set(Float.toString(this.arduinoSettings.getRollSlavePID()[2]));

        this.PitchMasterP.set(Float.toString(this.arduinoSettings.getPitchMasterPID()[0]));
        this.PitchMasterI.set(Float.toString(this.arduinoSettings.getPitchMasterPID()[1]));
        this.PitchMasterD.set(Float.toString(this.arduinoSettings.getPitchMasterPID()[2]));

        this.PitchSlaveP.set(Float.toString(this.arduinoSettings.getPitchSlavePID()[0]));
        this.PitchSlaveI.set(Float.toString(this.arduinoSettings.getPitchSlavePID()[1]));
        this.PitchSlaveD.set(Float.toString(this.arduinoSettings.getPitchSlavePID()[2]));

        this.YawMasterP.set(Float.toString(this.arduinoSettings.getYawMasterPID()[0]));
        this.YawMasterI.set(Float.toString(this.arduinoSettings.getYawMasterPID()[1]));
        this.YawMasterD.set(Float.toString(this.arduinoSettings.getYawMasterPID()[2]));

        this.YawSlaveP.set(Float.toString(this.arduinoSettings.getYawSlavePID()[0]));
        this.YawSlaveI.set(Float.toString(this.arduinoSettings.getYawSlavePID()[1]));
        this.YawSlaveD.set(Float.toString(this.arduinoSettings.getYawSlavePID()[2]));

        this.MinPID.set(Float.toString(this.arduinoSettings.getMinPIDvalue()));
        this.MaxPID.set(Float.toString(this.arduinoSettings.getMaxPIDvalue()));

        this.EngineMin.set(Integer.toString(this.arduinoSettings.getMotorMinSignal()));
        this.EngineMax.set(Integer.toString(this.arduinoSettings.getMotorMaxSignal()));
    }

    private ArduinoSettings getSettings()
    {
        ArduinoSettings settings = new ArduinoSettings();

        settings.setRollMasterPID(new float[] {
                Float.parseFloat(this.RollMasterP.get()),
                Float.parseFloat(this.RollMasterI.get()),
                Float.parseFloat(this.RollMasterD.get())});

        settings.setRollSlavePID(new float[]{
                Float.parseFloat(this.RollSlaveP.get()),
                Float.parseFloat(this.RollSlaveI.get()),
                Float.parseFloat(this.RollSlaveD.get())});

        settings.setPitchMasterPID(new float[]{
                Float.parseFloat(this.PitchMasterP.get()),
                Float.parseFloat(this.PitchMasterI.get()),
                Float.parseFloat(this.PitchMasterD.get())});

        settings.setPitchSlavePID(new float[]{
                Float.parseFloat(this.PitchSlaveP.get()),
                Float.parseFloat(this.PitchSlaveI.get()),
                Float.parseFloat(this.PitchSlaveD.get())});

        settings.setYawMasterPID(new float[]{
                Float.parseFloat(this.YawMasterP.get()),
                Float.parseFloat(this.YawMasterI.get()),
                Float.parseFloat(this.YawMasterD.get())});

        settings.setYawSlavePID(new float[]{
                Float.parseFloat(this.YawSlaveP.get()),
                Float.parseFloat(this.YawSlaveI.get()),
                Float.parseFloat(this.YawSlaveD.get())});

        settings.setMinPIDvalue(Float.parseFloat(this.MinPID.get()));
        settings.setMaxPIDvalue(Float.parseFloat(this.MaxPID.get()));

        settings.setMotorMinSignal(Integer.parseInt(this.EngineMin.get()));
        settings.setMotorMaxSignal(Integer.parseInt(this.EngineMax.get()));

        return settings;
    }

    class IntraMessageListener implements IIntraModuleMessageListener
    {
        public void onMessage(Object message)
        {
            try
            {
                if (message instanceof LoadMessage)
                {
                    SettingsViewModel.this.RollMasterP.set("666");
                    ArduinoMessage arduinoMessage = new ArduinoGetSettingsMessage();
                    String textArduinoMessage = SettingsViewModel.this.arduinoMessageSerialiser.toString(arduinoMessage);
                    Message bluetoothMessage = BluetoothServiceMessages.createSendDataChunkMessage(textArduinoMessage + "\n");
                    SettingsViewModel.this.bluetoothService.send(bluetoothMessage);
                    SettingsViewModel.this.RollMasterP.set("777");
                }
                else if (message instanceof SaveMessage)
                {
                    ArduinoSettings settings = SettingsViewModel.this.getSettings();
                    Iterable<ArduinoMessage> arduinoMessages = SettingsViewModel.this.arduinoSettingsSerialiser.toMessages(settings);
                    for (ArduinoMessage arduinoMessage : arduinoMessages)
                    {
                        String textArduinoMessage = SettingsViewModel.this.arduinoMessageSerialiser.toString(arduinoMessage);
                        Message bluetoothMessage = BluetoothServiceMessages.createSendDataChunkMessage(textArduinoMessage + "\n");
                        SettingsViewModel.this.bluetoothService.send(bluetoothMessage);
                    }
                }
            }
            catch (RemoteException e)
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
                if (msg.what == BluetoothServiceMessages.MSG_DATA_CHUNK_RESPONSE)
                {
                    String data = BluetoothServiceMessages.getDataFromDataChunkMessage(msg);
                    ArduinoMessage message = SettingsViewModel.this.arduinoMessageSerialiser.fromString(data);
                    SettingsViewModel.this.onArduinoMessage(message);
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
