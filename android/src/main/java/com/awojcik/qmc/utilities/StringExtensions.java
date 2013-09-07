package com.awojcik.qmc.utilities;

public class StringExtensions 
{
	public static int CharCount(String str, char c)
	{
		if (str == null) return 0;
		
		int count = 0;
		
		for (int i=0; i<str.length(); i++)
		{
			if (str.charAt(i) == c) count++;
		}
		
		return count;
	}
}
