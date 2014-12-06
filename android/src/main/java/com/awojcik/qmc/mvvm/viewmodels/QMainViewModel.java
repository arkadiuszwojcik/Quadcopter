package com.awojcik.qmc.mvvm.viewmodels;

import com.awojcik.qmc.modules.control.ControlViewModel;
import com.awojcik.qmc.modules.control.ControlViewModelFactory;
import com.awojcik.qmc.modules.imu.ImuViewModelFactory;
import com.awojcik.qmc.modules.settings.SettingsViewModel;
import com.awojcik.qmc.modules.settings.SettingsViewModelFactory;
import com.awojcik.qmc.modules.terminal.TerminalViewModel;
import com.awojcik.qmc.modules.terminal.TerminalViewModelFactory;
import com.awojcik.qmc.modules.imu.ImuViewModel;
import com.awojcik.qmc.modules.imu.ImuViewModelFactory;
import com.awojcik.qmc.mvvm.commands.QOpenBluetoothDialogCommand;
import com.google.inject.Inject;

public class QMainViewModel 
{
	@Inject
	public QOpenBluetoothDialogCommand OpenBluetoothDialogCommand;
	
	public TerminalViewModel TerminalViewModel;

    public ImuViewModel ImuViewModel;

    public ControlViewModel ControlViewModel;

    public SettingsViewModel SettingsViewModel;
	
	@Inject
	public QMainViewModel(
            ControlViewModelFactory controlViewModelFactory,
            TerminalViewModelFactory terminalViewModelFactory,
            ImuViewModelFactory imuViewModelFactory,
            SettingsViewModelFactory settingsViewModelFactory)
	{
        this.ControlViewModel = controlViewModelFactory.Create();
		this.TerminalViewModel = terminalViewModelFactory.Create();
        this.ImuViewModel = imuViewModelFactory.Create();
        this.SettingsViewModel = settingsViewModelFactory.Create();
	}

    public void init()
    {
        this.ImuViewModel.init();
    }
}
