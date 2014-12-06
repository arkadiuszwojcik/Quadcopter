package com.awojcik.qmc.arduino;

import com.awojcik.qmc.arduino.messages.incoming.ArduinoFloatMessage;
import com.awojcik.qmc.arduino.messages.incoming.ArduinoIntegerMessage;
import com.awojcik.qmc.arduino.messages.outgoing.ArduinoSaveFloatMessage;
import com.awojcik.qmc.arduino.messages.outgoing.ArduinoSaveIntegerMessage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestArduinoMessageSerialiser
{
    @Before
    public void init()
    {
        this.serialiser = new ArduinoMessageSerialiser();
    }

    @Test
    public void parsesStringToFloatMessage()
    {
        String strMessage = "GF 40 4.5 6.5 4.3";

        ArduinoMessage message = this.serialiser.fromString(strMessage);

        assertEquals(message.getClass(), ArduinoFloatMessage.class);
        ArduinoFloatMessage floatMessage = (ArduinoFloatMessage)message;
        assertEquals(40, floatMessage.getOffset());
        assertArrayEquals(new float[] { 4.5f, 6.5f, 4.3f }, floatMessage.getValues(), EPSILON);
    }

    @Test
    public void parsesStringToIntegerMessage()
    {
        String strMessage = "GI 40 4 6 99";

        ArduinoMessage message = this.serialiser.fromString(strMessage);

        assertEquals(message.getClass(), ArduinoIntegerMessage.class);
        ArduinoIntegerMessage integerMessage = (ArduinoIntegerMessage)message;
        assertEquals(40, integerMessage.getOffset());
        assertArrayEquals(new int[] { 4, 6, 99 }, integerMessage.getValues());
    }

    @Test
    public void serialisesSaveFloatMessageToString()
    {
        ArduinoSaveFloatMessage message = new ArduinoSaveFloatMessage(15, new float[] { 12.6f, 44.654f, 23f });

        String strMessage = this.serialiser.toString(message);

        assertEquals("WF 15 12.6 44.654 23.0", strMessage);
    }

    @Test
    public void serialisesSaveIntegerMessageToString()
    {
        ArduinoSaveIntegerMessage message = new ArduinoSaveIntegerMessage(15, new int[] { 12, 44, 1 });

        String strMessage = this.serialiser.toString(message);

        assertEquals("WI 15 12 44 1", strMessage);
    }

    private ArduinoMessageSerialiser serialiser;
    private final static float EPSILON = 0.00001f;
}
