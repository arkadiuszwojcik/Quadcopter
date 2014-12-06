package com.awojcik.qmc.arduino.messages.incoming;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.ArduinoMessageType;

public class ArduinoIntegerMessage implements ArduinoMessage
{
    public ArduinoIntegerMessage(int offset, int[] values)
    {
        this.offset = offset;
        this.values = values;
    }

    public ArduinoMessageType getMessageType()
    {
        return ArduinoMessageType.MSG_INTEGER;
    }

    public int getOffset()
    {
        return this.offset;
    }

    public int[] getValues()
    {
        return this.values;
    }

    private final int offset;
    private final int values[];
}
