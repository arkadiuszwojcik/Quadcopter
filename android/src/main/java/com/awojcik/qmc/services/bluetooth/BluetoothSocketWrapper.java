package com.awojcik.qmc.services.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothSocketWrapper
{
	public BluetoothSocketWrapper(BluetoothDevice bluetoothDevice, BluetoothSocketCallback callback)
	{
		this.bluetoothDevice = bluetoothDevice;
		this.callback = callback;
	}
	
	public void close()
	{
		try
		{
			if (this.socket != null)
			{
				this.socket.close();
			}
		}
		catch (IOException ex)
		{
		}
	}
	
	public boolean connect()
	{
		try
		{
			this.socket = this.bluetoothDevice.createRfcommSocketToServiceRecord(SERIAL_UUID);
			this.socket.connect();
		} 
		catch (IOException e)
		{
			this.close();
			return false;
		}
		
		this.startReceiverThread();
		return true;
	}
	
	public boolean writeData(String data)
	{
		try 
		{
			OutputStream stream = this.socket.getOutputStream();
			stream.write(data.getBytes());
			return true;
		} 
		catch (IOException e) 
		{
			return false;
		}
	}
	
	private void startReceiverThread()
	{
		Thread receiverThread = new Thread(new BluetoothReceiverThread(this));
        receiverThread.start();
	}
	
	public BluetoothSocket getInternalBluetoothSocket()
	{
		return this.socket;
	}
	
	public BluetoothSocketCallback getCallbackObject()
	{
		return this.callback;
	}
	
	private BluetoothSocket socket;
	private final BluetoothDevice bluetoothDevice;
	private final BluetoothSocketCallback callback;
	private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
}