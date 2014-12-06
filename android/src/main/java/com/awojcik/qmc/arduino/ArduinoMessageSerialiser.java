package com.awojcik.qmc.arduino;

import com.awojcik.qmc.arduino.messages.incoming.ArduinoFloatMessage;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoIntegerMessage;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoUnknownMessage;

public class ArduinoMessageSerialiser
{
    public ArduinoMessage fromString(String message)
    {
        ArduinoMessageType messageType = this.getMessageType(message);

        switch (messageType)
        {
            case MSG_FLOAT:
                return parseFloatMessage(message);
            case MSG_INTEGER:
                return parseIntegerMessage(message);
            case MSG_UNKNOWN:
            default:
                return new ArduinoUnknownMessage();
        }
    }

    public String toString(ArduinoMessage message)
    {
        return message.toString();
    }

    private ArduinoMessageType getMessageType(String message)
    {
        String[] tokens = message.split(" ");
        return ArduinoMessageType.fromString(tokens[0]);
    }

    private ArduinoMessage parseFloatMessage(String message)
    {
        String[] tokens = message.split(" ");
        if (tokens.length < 3) return new ArduinoUnknownMessage();

        int offset = Integer.parseInt(tokens[1]);

        float[] values = new float[tokens.length - 2];
        for (int i=2; i<tokens.length; i++)
        {
            values[i-2] = Float.parseFloat(tokens[i]);
        }

        return new ArduinoFloatMessage(offset, values);
    }

    private ArduinoMessage parseIntegerMessage(String message)
    {
        String[] tokens = message.split(" ");
        if (tokens.length < 3) return new ArduinoUnknownMessage();

        int offset = Integer.parseInt(tokens[1]);

        int[] values = new int[tokens.length - 2];
        for (int i=2; i<tokens.length; i++)
        {
            values[i-2] = Integer.parseInt(tokens[i]);
        }

        return new ArduinoIntegerMessage(offset, values);
    }
}
