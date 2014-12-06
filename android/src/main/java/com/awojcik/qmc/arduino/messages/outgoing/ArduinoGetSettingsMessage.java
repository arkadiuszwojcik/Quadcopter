package com.awojcik.qmc.arduino.messages.outgoing;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.ArduinoMessageType;

public class ArduinoGetSettingsMessage implements ArduinoMessage
{
    public ArduinoMessageType getMessageType()
    {
        return ArduinoMessageType.MSG_GET_SETTINGS;
    }

    @Override
    public String toString()
    {
        return this.getMessageType().getText();
    }
}
