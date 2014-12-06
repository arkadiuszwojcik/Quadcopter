package com.awojcik.qmc.utilities;

public class ArrayExtenisons
{
    public static String[] toStringArray(float[] floatArray)
    {
        String[] stringArray = new String[floatArray.length];
        for (int i=0; i<floatArray.length; i++)
        {
            stringArray[i] = Float.toString(floatArray[i]);
        }
        return stringArray;
    }

    public static String[] toStringArray(int[] intArray)
    {
        String[] stringArray = new String[intArray.length];
        for (int i=0; i<intArray.length; i++)
        {
            stringArray[i] = Integer.toString(intArray[i]);
        }
        return stringArray;
    }
}
