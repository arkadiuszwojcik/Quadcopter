package com.awojcik.qmc.modules.terminal;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.awojcik.qmc.R;
import com.awojcik.qmc.modules.common.IIntraMessageListener;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.providers.QBluetoothServiceProvider;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.QBluetoothService;
import com.awojcik.qmc.utilities.ActivityExtensions;
import com.google.inject.Inject;

import gueei.binding.observables.CharSequenceObservable;
import gueei.binding.observables.StringObservable;

public class TerminalViewModel 
{
	public SendCommand SendCommand;
	
	public StringObservable InputText = new StringObservable("DDD");
	
	//public StringObservable Text = new StringObservable();
	public CharSequenceObservable Text = new CharSequenceObservable("III");
	
	private StringBuffer buffer = new StringBuffer();
	
	private TerminalSpannableStringBuffer bufferTest = new TerminalSpannableStringBuffer(39);
	
	/*
	 * Private
	 */
	private ServiceManager bluetoothService;
	
	private IntraModuleMessenger intraModuleMessenger;
	
	private Activity activity;
	
	@Inject
	public TerminalViewModel(
			Activity activity,
			QBluetoothServiceProvider bluetoothServiceProvider,
			IntraModuleMessenger intraModuleMessenger,
			SendCommand sendCommand)
	{
		this.activity = activity;
		this.bluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
		this.intraModuleMessenger = intraModuleMessenger;
		this.intraModuleMessenger.register(new IntraMessageListener());
		
		this.SendCommand = sendCommand;
	}
	
	private void appendLine(String line)
	{
		this.bufferTest.appendLine(line, 0);
		this.Text.set(bufferTest.getBuffer());
	}
	
	private void clearInputControl()
	{
		this.InputText.set("");
	}
	
	private void scrollDown()
	{
		((ScrollView)this.activity.findViewById(R.id.terminal_scroll_view)).fullScroll(View.FOCUS_DOWN);
	}
	
	class IntraMessageListener implements IIntraMessageListener
	{
		public void onMessage(Object message)
		{
			if (message instanceof SendMessage)
			{	
				appendLine(InputText.get());
				clearInputControl();
				scrollDown();
				ActivityExtensions.closeInputControl(activity);
			}
		}
	}
	
	class BluetoothServiceHandler extends Handler
    {
		private static final String TAG = "TerminalViewModel.BluetoothServiceHandler";
		
    	@Override
    	public void handleMessage(Message msg)
    	{
    		try
    		{
    			if (msg.what == QBluetoothService.MSG_DEVICE_CONNECTED_RESPONSE)
    			{
    				String address = msg.getData().getString(QBluetoothService.KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS);
    				TerminalViewModel.this.appendLine("Device connected: " + address);
    			}
    			
    			if (msg.what == QBluetoothService.MSG_DATA_CHUNK_RESPONSE)
    			{
    				String data = msg.getData().getString(QBluetoothService.KEY_MSG_DATA_CHUNK_RESPONSE_DATA);
    				TerminalViewModel.this.appendLine("New data: " + data);
    			}
    		} 
    		catch (Exception e)
    		{
    			Log.e(TAG, e.getMessage());
    		}
    	}
    }
}
