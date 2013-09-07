package com.awojcik.qmc.modules.common;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class IntraModuleMessenger 
{	
	private List<IIntraMessageListener> mListeners =
			new LinkedList<IIntraMessageListener>();
	
	public void register(IIntraMessageListener listener)
	{
		mListeners.add(listener);
	}
	
	public void send(Object message)
	{
		for (IIntraMessageListener callback : mListeners)
		{
			callback.onMessage(message);
		}
	}
}
