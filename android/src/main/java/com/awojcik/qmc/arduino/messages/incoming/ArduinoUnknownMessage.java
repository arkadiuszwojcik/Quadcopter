package com.awojcik.qmc.arduino.messages.incoming;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.ArduinoMessageType;

public class ArduinoUnknownMessage implements ArduinoMessage
{
    public ArduinoUnknownMessage()
    {}

    public ArduinoMessageType getMessageType()
    {
        return ArduinoMessageType.MSG_UNKNOWN;
    }
}
