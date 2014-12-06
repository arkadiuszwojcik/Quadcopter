package com.awojcik.qmc.utilities;

public class StringExtensions 
{
	public static int charCount(String str, char c)
	{
		if (str == null) return 0;
		
		int count = 0;
		
		for (int i=0; i<str.length(); i++)
		{
			if (str.charAt(i) == c) count++;
		}
		
		return count;
	}

    public static String join(String[] array, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, il = array.length; i < il; i++)
        {
            if (i > 0)
                sb.append(separator);
            sb.append(array[i]);
        }
        return sb.toString();
    }
}
