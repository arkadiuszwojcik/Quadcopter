package com.awojcik.qmc.modules.terminal;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.awojcik.qmc.R;
import com.awojcik.qmc.modules.common.IIntraModuleMessageListener;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.modules.terminal.commands.SendCommand;
import com.awojcik.qmc.modules.terminal.messages.SendMessage;
import com.awojcik.qmc.services.bluetooth.BluetoothService;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceMessages;
import com.awojcik.qmc.utilities.ActivityExtensions;
import com.google.inject.Inject;

import gueei.binding.observables.CharSequenceObservable;
import gueei.binding.observables.StringObservable;

public class TerminalViewModel 
{
	public final SendCommand SendCommand;
	
	public final StringObservable InputText = new StringObservable("");

	public final CharSequenceObservable Text = new CharSequenceObservable("");
	
	private final TerminalSpannableStringBuffer bufferTest = new TerminalSpannableStringBuffer(39);

	private final ServiceManager bluetoothService;
	
	private final IntraModuleMessenger intraModuleMessenger;
	
	private final Activity activity;
	
	@Inject
	public TerminalViewModel(
			Activity activity,
			BluetoothServiceFactory bluetoothServiceProvider,
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

    private  void sendCommand(String command)
    {
        if (!command.endsWith("\n")) command = command + "\n";

        Message message = this.getSendCommandMessage(command);
        try
        {
            this.bluetoothService.send(message);
        }
        catch (RemoteException e)
        {
        }
    }

    private Message getSendCommandMessage(String strData)
    {
        Message msg = BluetoothServiceMessages.createSendDataChunkMessage(strData);
        return msg;
    }
	
	private void clearInputControl()
	{
		this.InputText.set("");
	}
	
	private void scrollDown()
	{
		((ScrollView)this.activity.findViewById(R.id.terminal_scroll_view)).fullScroll(View.FOCUS_DOWN);
	}
	
	class IntraMessageListener implements IIntraModuleMessageListener
	{
		public void onMessage(Object message)
		{
			if (message instanceof SendMessage)
			{	
				appendLine(InputText.get());
                sendCommand(InputText.get());
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
    			if (msg.what == BluetoothServiceMessages.MSG_DEVICE_CONNECTED_RESPONSE)
    			{
    				String address = BluetoothServiceMessages.getAddressFromDeviceConnectedMessage(msg);
    				TerminalViewModel.this.appendLine("Device connected: " + address);
    			}
    			
    			if (msg.what == BluetoothServiceMessages.MSG_DATA_CHUNK_RESPONSE)
    			{
    				String data = BluetoothServiceMessages.getDataFromDataChunkMessage(msg);
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
