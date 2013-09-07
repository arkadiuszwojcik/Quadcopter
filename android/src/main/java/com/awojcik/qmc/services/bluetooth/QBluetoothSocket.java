package com.awojcik.qmc.services.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class QBluetoothSocket
{
	public QBluetoothSocket(BluetoothDevice bluetoothDevice, IQBluetoothSocketCallback callback)
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
		catch (Exception e)
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
		
		this.beginListenForData();
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
	
	private void beginListenForData()
	{
		Thread workingThread = new Thread(new QReceiveWorker(this));
		workingThread.start();
	}
	
	public BluetoothSocket getInternalBluetoothSocket()
	{
		return this.socket;
	}
	
	public IQBluetoothSocketCallback getCallbackObject()
	{
		return this.callback;
	}
	
	private BluetoothSocket socket;
	
	private final BluetoothDevice bluetoothDevice;
	private final IQBluetoothSocketCallback callback;
	
	private static final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
}