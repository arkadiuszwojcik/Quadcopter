package com.awojcik.qmc.arduino.messages.outgoing;

import com.awojcik.qmc.arduino.ArduinoMessage;
import com.awojcik.qmc.arduino.ArduinoMessageType;
import com.awojcik.qmc.utilities.ArrayExtenisons;
import com.awojcik.qmc.utilities.StringExtensions;

public class ArduinoSaveFloatMessage implements ArduinoMessage
{
    public ArduinoSaveFloatMessage(int offset, float value)
    {
        this.offset = offset;
        this.values = new float[] { value };
    }

    public ArduinoSaveFloatMessage(int offset, float[] values)
    {
        this.offset = offset;
        this.values = values;
    }

    public ArduinoMessageType getMessageType()
    {
        return ArduinoMessageType.MSG_WRITE_FLOAT;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        String type = this.getMessageType().getText();
        String values = StringExtensions.join(ArrayExtenisons.toStringArray(this.values), " ");
        sb.append(type).append(" ").append(this.offset).append(" ").append(values);
        return sb.toString();
    }

    private final int offset;
    private final float[] values;
}
