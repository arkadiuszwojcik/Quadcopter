package com.awojcik.qmc.modules.terminal.commands;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.terminal.messages.SendMessage;
import com.google.inject.Inject;

import android.view.View;
import gueei.binding.Command;

public class SendCommand extends Command 
{
	@Inject
	public SendCommand(IntraModuleMessenger intraMessenger)
	{
		this.intraModuleMessenger = intraMessenger;
	}
	
	@Override
	public void Invoke(View arg0, Object... arg1) 
	{
		this.intraModuleMessenger.send(new SendMessage());
	}

    private final IntraModuleMessenger intraModuleMessenger;
}
