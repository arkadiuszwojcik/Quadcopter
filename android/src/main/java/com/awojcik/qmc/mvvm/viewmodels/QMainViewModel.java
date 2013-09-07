package com.awojcik.qmc.mvvm.viewmodels;

import com.awojcik.qmc.modules.terminal.TerminalViewModel;
import com.awojcik.qmc.modules.terminal.TerminalViewModelFactory;
import com.awojcik.qmc.mvvm.commands.QOpenBluetoothDialogCommand;
import com.google.inject.Inject;

public class QMainViewModel 
{
	@Inject
	public QOpenBluetoothDialogCommand OpenBluetoothDialogCommand;
	
	public TerminalViewModel TerminalViewModel;
	
	@Inject
	public QMainViewModel(TerminalViewModelFactory terminalViewModelFactory)
	{
		this.TerminalViewModel = terminalViewModelFactory.Create();
	}
}
