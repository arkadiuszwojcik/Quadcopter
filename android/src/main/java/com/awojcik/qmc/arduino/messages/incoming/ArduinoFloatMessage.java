package com.awojcik.qmc.arduino.messages.incoming;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.ArduinoMessageType;

public class ArduinoFloatMessage implements ArduinoMessage
{
    public ArduinoFloatMessage(int offset, float[] values)
    {
        this.offset = offset;
        this.values = values;
    }

    public ArduinoMessageType getMessageType()
    {
        return ArduinoMessageType.MSG_FLOAT;
    }

    public int getOffset()
    {
        return this.offset;
    }

    public float[] getValues()
    {
        return this.values;
    }

    private final int offset;
    private final float[] values;
}
