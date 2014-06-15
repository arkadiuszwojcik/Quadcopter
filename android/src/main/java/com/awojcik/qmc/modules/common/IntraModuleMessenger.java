package com.awojcik.qmc.modules.common;

import java.util.LinkedList;
import java.util.List;

public class IntraModuleMessenger 
{
	public void register(IIntraModuleMessageListener listener)
	{
		listeners.add(listener);
	}
	
	public void send(Object message)
	{
		for (IIntraModuleMessageListener callback : listeners)
		{
			callback.onMessage(message);
		}
	}

    private final List<IIntraModuleMessageListener> listeners =
            new LinkedList<IIntraModuleMessageListener>();
}
