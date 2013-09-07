package com.awojcik.qmc.mvvm.commands;

import com.awojcik.qmc.R;
import com.awojcik.qmc.modules.bluetooth.BluetoothViewModel;
import com.awojcik.qmc.modules.bluetooth.BluetoothViewModelFactory;

import com.google.inject.Inject;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import gueei.binding.Command;
import gueei.binding.v30.app.BindingWidgetV30;

public class QOpenBluetoothDialogCommand extends Command
{
	@Inject
	private Context mContext;
	
	@Inject
	private BluetoothViewModelFactory mBluetoothDevicesViewModelFactory;
	
	@Override
	public void Invoke(View arg0, Object... arg1) 
	{
		BluetoothViewModel viewModel = mBluetoothDevicesViewModelFactory.Create();
		Dialog dialog = BindingWidgetV30.createAndBindDialog(mContext, R.layout.bluetooth_dialog_layout, viewModel);
		dialog.setOnCancelListener(viewModel.CancelListener);
		dialog.setTitle("Bluetooth");
		dialog.show();
	}
}
