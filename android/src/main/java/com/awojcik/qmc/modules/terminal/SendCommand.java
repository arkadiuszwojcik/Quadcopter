package com.awojcik.qmc.modules.terminal;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.google.inject.Inject;

import android.util.Log;
import android.view.View;
import gueei.binding.Command;

public class SendCommand extends Command 
{
	private final IntraModuleMessenger mIntraMessenger;
	
	@Inject
	public SendCommand(IntraModuleMessenger intraMessenger)
	{
		this.mIntraMessenger = intraMessenger;
	}
	
	@Override
	public void Invoke(View arg0, Object... arg1) 
	{
		this.mIntraMessenger.send(new SendMessage());
	}
}
