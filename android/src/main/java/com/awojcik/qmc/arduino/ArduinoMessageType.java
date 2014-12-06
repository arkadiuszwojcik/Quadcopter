package com.awojcik.qmc.arduino;

public enum ArduinoMessageType
{
    MSG_WRITE_FLOAT("WF"),
    MSG_WRITE_INTEGER("WI"),
    MSG_GET_SETTINGS("GS"),
    MSG_FLOAT("GF"),
    MSG_INTEGER("GI"),
    MSG_UNKNOWN("UNK");

    private String text;

    ArduinoMessageType(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }

    public static ArduinoMessageType fromString(String text)
    {
        if (text != null)
        {
            for (ArduinoMessageType cmd : ArduinoMessageType.values())
            {
                if (text.equalsIgnoreCase(cmd.text))
                {
                    return cmd;
                }
            }
        }
        return MSG_UNKNOWN;
    }
}
