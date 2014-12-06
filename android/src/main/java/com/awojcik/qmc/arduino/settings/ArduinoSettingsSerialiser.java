package com.awojcik.qmc.arduino.settings;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoFloatMessage;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoIntegerMessage;
import com.awojcik.qmc.arduino.messages.outgoing.ArduinoSaveFloatMessage;
import com.awojcik.qmc.arduino.messages.outgoing.ArduinoSaveIntegerMessage;

import java.util.LinkedList;
import java.util.List;

public class ArduinoSettingsSerialiser
{
    public Iterable<ArduinoMessage> toMessages(ArduinoSettings settings)
    {
        List<ArduinoMessage> messages = new LinkedList<ArduinoMessage>();

        messages.add(new ArduinoSaveFloatMessage(0, settings.getGyroscopeOffsets()));
        messages.add(new ArduinoSaveFloatMessage(12, settings.getAccelerometerOffsets()));
        messages.add(new ArduinoSaveFloatMessage(24, settings.getMagnetometerOffsets()));

        messages.add(new ArduinoSaveFloatMessage(36, settings.getMagnetometerDeclination()));

        messages.add(new ArduinoSaveFloatMessage(40, settings.getRollMasterPID()));
        messages.add(new ArduinoSaveFloatMessage(52, settings.getRollSlavePID()));

        messages.add(new ArduinoSaveFloatMessage(64, settings.getPitchMasterPID()));
        messages.add(new ArduinoSaveFloatMessage(76, settings.getPitchSlavePID()));

        messages.add(new ArduinoSaveFloatMessage(88, settings.getYawMasterPID()));
        messages.add(new ArduinoSaveFloatMessage(100, settings.getYawSlavePID()));

        messages.add(new ArduinoSaveFloatMessage(112, settings.getMinPIDvalue()));
        messages.add(new ArduinoSaveFloatMessage(116, settings.getMaxPIDvalue()));

        messages.add(new ArduinoSaveIntegerMessage(120, settings.getMotorMinSignal()));
        messages.add(new ArduinoSaveIntegerMessage(124, settings.getMotorMaxSignal()));

        return messages;
    }

    public void updateFromMessage(ArduinoSettings settings, ArduinoMessage message)
    {
        if (message instanceof ArduinoFloatMessage)
        {
            this.updateFromFloatMessage(settings, (ArduinoFloatMessage) message);
        }
        else if (message instanceof ArduinoIntegerMessage)
        {
            this.updateFromIntegerMessage(settings, (ArduinoIntegerMessage)message);
        }
    }

    private void updateFromFloatMessage(ArduinoSettings settings, ArduinoFloatMessage message)
    {
        switch (message.getOffset())
        {
            case 0:
                settings.setGyroscopeOffsets(message.getValues());
                break;
            case 12:
                settings.setAccelerometerOffsets(message.getValues());
                break;
            case 24:
                settings.setMagnetometerOffsets(message.getValues());
                break;
            case 36:
                settings.setMagnetometerDeclination(message.getValues()[0]);
                break;
            case 40:
                settings.setRollMasterPID(message.getValues());
                break;
            case 52:
                settings.setRollSlavePID(message.getValues());
                break;
            case 64:
                settings.setPitchMasterPID(message.getValues());
                break;
            case 76:
                settings.setPitchSlavePID(message.getValues());
                break;
            case 88:
                settings.setYawMasterPID(message.getValues());
                break;
            case 100:
                settings.setYawSlavePID(message.getValues());
                break;
            case 112:
                settings.setMinPIDvalue(message.getValues()[0]);
                break;
            case 116:
                settings.setMaxPIDvalue(message.getValues()[0]);
                break;
        }
    }

    private void updateFromIntegerMessage(ArduinoSettings settings, ArduinoIntegerMessage message)
    {
        switch (message.getOffset())
        {
            case 120:
                settings.setMotorMinSignal(message.getValues()[0]);
                break;
            case 124:
                settings.setMotorMaxSignal(message.getValues()[0]);
                break;
        }
    }
}
