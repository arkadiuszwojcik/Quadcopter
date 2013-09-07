package com.awojcik.qmc.modules.bluetooth;

import gueei.binding.Command;
import android.view.View;

import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.google.inject.Inject;

public class StopScanCommand extends Command
{
	private final IntraModuleMessenger mIntraMessenger;
	
	@Inject
	public StopScanCommand(IntraModuleMessenger intraMessenger)
	{
		this.mIntraMessenger = intraMessenger;
	}
	
	@Override
	public void Invoke(View arg0, Object... arg1) 
	{
		this.mIntraMessenger.send(new StopScanMessage());
	}
}
