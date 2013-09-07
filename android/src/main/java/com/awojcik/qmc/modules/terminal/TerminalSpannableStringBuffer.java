package com.awojcik.qmc.modules.terminal;

import com.awojcik.qmc.utilities.StringExtensions;

import android.text.SpannableStringBuilder;

public class TerminalSpannableStringBuffer 
{
	private SpannableStringBuilder spannedBuffer = new
			SpannableStringBuilder();
	
	private final int maxLinesNum;
	
	public TerminalSpannableStringBuffer(int maxLinesNum)
	{
		this.maxLinesNum = maxLinesNum;
		this.clear();
	}
	
	public SpannableStringBuilder getBuffer()
	{
		return this.spannedBuffer;
	}
	
	public void clear()
	{
		this.spannedBuffer.clearSpans();
		this.spannedBuffer.clear();
		
		for (int i=0; i<this.maxLinesNum;i++)
		{
			this.spannedBuffer.append('\n');
		}
	}
	
	public void appendLine(String line, int color)
	{
		if (line == null) return;
			
		int numLines = this.getLinesNumber(line) + 1;
		
		this.spannedBuffer.append('\n');
		this.spannedBuffer.append(line);
		
		this.removeUpperLines(numLines);
	}
	
	private int getLinesNumber(String line)
	{
		return StringExtensions.CharCount(line, '\n');
	}
	
	private void removeUpperLines(int num)
	{
		if (num == 0) return;
		
		int foundLines = 0;
		
		for (int i=0; i<this.spannedBuffer.length(); i++)
		{
			char c = this.spannedBuffer.charAt(i);
			
			if (c != '\n') continue;
			
			foundLines++;
			
			if (foundLines < num) continue;
			
			this.spannedBuffer.replace(0, i + 1, "");
			break;
		}
	}
}