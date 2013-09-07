package com.awojcik.qmc.modules.bluetooth;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.google.inject.Inject;

import android.view.View;
import gueei.binding.Command;

public class ScanCommand extends Command
{
	private final IntraModuleMessenger mIntraMessenger;
	
	@Inject
	public ScanCommand(IntraModuleMessenger intraMessenger)
	{
		this.mIntraMessenger = intraMessenger;
	}
	
	@Override
	public void Invoke(View arg0, Object... arg1) 
	{
		this.mIntraMessenger.send(new ScanMessage());
	}
}
