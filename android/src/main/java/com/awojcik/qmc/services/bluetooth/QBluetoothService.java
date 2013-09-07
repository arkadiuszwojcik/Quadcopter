package com.awojcik.qmc.services.bluetooth;

import java.io.IOException;
import java.util.UUID;

import com.awojcik.qmc.services.AbstractService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class QBluetoothService extends AbstractService
{
	@Override
	public void onStartService() 
	{
		this.receiver = new QBluetoothBroadcastReceiver(new ReceiverCallback());
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		
		this.registerReceiver(this.receiver, filter);
	}
	
	@Override
	public void onStopService() 
	{
		this.unregisterReceiver(this.receiver);
	}

	@Override
	public void onReceiveMessage(Message msg)
	{
		if (msg.what == MSG_ENABLE_REQUEST)
		{
			Log.i(TAG, "Received: MSG_ENABLE_REQUEST");
			this.enableBluetooth();
		}
		
		if (msg.what == MSG_DISABLE_REQUEST)
		{
			Log.i(TAG, "Received: MSG_DISABLE_REQUEST");
			this.disableBluetooth();
		}
		
		if (msg.what == MSG_START_DISCOVER_REQUEST)
		{
			Log.i(TAG, "Received: MSG_START_DISCOVER_REQUEST");
			this.discoverDevices();
		}
		
		if (msg.what == MSG_STOP_DISCOVER_REQUEST)
		{
			Log.i(TAG, "Received: MSG_STOP_DISCOVER_REQUEST");
			this.stopDiscoverDevices();
		}
		
		if (msg.what == MSG_IS_ENABLED_REQUEST)
		{
			Log.i(TAG, "Received: MSG_IS_ENABLED_REQUEST");
			this.isEnabled();
		}
		
		if (msg.what == MSG_CONNECT_TO_DEVICE_REQUEST)
		{
			Log.i(TAG, "Received: MSG_CONNECT_TO_DEVICE_REQUEST");
			String address = msg.getData().getString(KEY_MSG_CONNECT_TO_DEVICE_REQUEST_ADDRESS);
			this.connect(address);
		}
	}
	
	public BluetoothDevice getRemoteDevice(String address)
	{
		return this.bluetoothAdapter.getRemoteDevice(address);
	}
	
	class ReceiverCallback implements IQBluetoothBroadcastReceiverCallback 
	{
		public void deviceDiscovered(String name, String address, BluetoothClass deviceClass) 
		{
			Log.i(TAG, "Sending: MSG_DEVICE_DISCOVERED_RESPONSE");
			
			Message msg = new Message();
			msg.what = MSG_DEVICE_DISCOVERED_RESPONSE;
			Bundle data = new Bundle();
			data.putString(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_NAME, name);
			data.putString(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_ADDRESS, address);
			data.putParcelable(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_CLASS, deviceClass);
			msg.setData(data);
			QBluetoothService.this.send(msg);
		}

		public void discoveryStarted()
		{
			Log.i(TAG, "Sending: MSG_DISCOVERY_STARTED_RESPONSE");
			
			Message msg = Message.obtain(null, MSG_DISCOVERY_STARTED_RESPONSE);
			QBluetoothService.this.send(msg);
		}
		
		public void discoveryFinished()
		{
			Log.i(TAG, "Sending: MSG_DISCOVERY_FINISHED_RESPONSE");
			
			Message msg = Message.obtain(null, MSG_DISCOVERY_FINISHED_RESPONSE);
			QBluetoothService.this.send(msg);
		}
    }
	
	class SocketCallback implements IQBluetoothSocketCallback
	{
		public void receivedData(String data) 
		{
			QBluetoothService.this.sendDataChunkResponse(data);
		}	
	}
	
	private void enableBluetooth()
	{
		boolean enabled = this.bluetoothAdapter.isEnabled();
		
		if (enabled == false)
		{
			enabled = this.bluetoothAdapter.enable();
		}
		
		this.sendIsEnabledResponse(enabled);
	}
	
	private void disableBluetooth()
	{
		boolean disabled = !this.bluetoothAdapter.isEnabled();
		
		if (disabled == false)
		{
			disabled = this.bluetoothAdapter.disable();
		}
		
		this.sendIsEnabledResponse(!disabled);
	}
	
	private void discoverDevices()
	{
		this.bluetoothAdapter.startDiscovery();
	}
	
	private void stopDiscoverDevices()
	{
		this.bluetoothAdapter.cancelDiscovery();
	}
	
	private void isEnabled()
	{
		boolean isEnabled = bluetoothAdapter.isEnabled();
		
		this.sendIsEnabledResponse(isEnabled);
	}
	
	private void connect(String address)
	{
		try 
		{
			BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);
		
			if (this.socket != null)
			{
				this.socket.close();
			}
			
			this.socket = new QBluetoothSocket(device, new SocketCallback());
			
			if (this.socket.connect() == true)
			{
				Log.i("DUPOZA", "CONNECTED");
				this.sendConnectedResponse(address);
			}
		} 
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void sendIsEnabledResponse(boolean response)
	{
		Message msg = Message.obtain(null, MSG_IS_ENABLED_RESPONSE);
		
		Bundle data = new Bundle();
		data.putBoolean(KEY_MSG_IS_ENABLED_RESPONSE_VALUE, response);
		msg.setData(data);
		QBluetoothService.this.send(msg);
	}
	
	private void sendConnectedResponse(String address)
	{
		Message msg = Message.obtain(null, MSG_DEVICE_CONNECTED_RESPONSE);
		
		Bundle data = new Bundle();
		data.putString(KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS, address);
		msg.setData(data);
		QBluetoothService.this.send(msg);
		Log.i("DUPOZA", "MESSAGED");
	}
	
	private void sendDataChunkResponse(String dataChunk)
	{
		Message msg = Message.obtain(null, MSG_DATA_CHUNK_RESPONSE);
		
		Bundle data = new Bundle();
		data.putString(KEY_MSG_DATA_CHUNK_RESPONSE_DATA, dataChunk);
		msg.setData(data);
		QBluetoothService.this.send(msg);
	}
	
	/*
	public QBluetoothSocket ConnectToDevice(String address, IQBluetoothSocketCallback callback)
	{
		BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(address);
		QBluetoothSocket socket = new QBluetoothSocket(device, callback);
		
		if (socket.connect() == true)
		{
			return socket;
		}
		
		return null;
	}
	*/
	
	public static final int MSG_IS_ENABLED_REQUEST = 1;
	public static final int MSG_IS_ENABLED_RESPONSE = 2;
	public static final int MSG_ENABLE_REQUEST = 3;
	public static final int MSG_DISABLE_REQUEST = 4;
	public static final int MSG_START_DISCOVER_REQUEST = 5;
	public static final int MSG_STOP_DISCOVER_REQUEST = 6;
	public static final int MSG_DISCOVERY_STARTED_RESPONSE = 7;
	public static final int MSG_DISCOVERY_FINISHED_RESPONSE = 8;
	public static final int MSG_DEVICE_DISCOVERED_RESPONSE = 9;
	public static final int MSG_CONNECT_TO_DEVICE_REQUEST = 10;
	public static final int MSG_DEVICE_CONNECTED_RESPONSE = 11;
	public static final int MSG_DATA_CHUNK_RESPONSE = 12;
	
	public static final Message MsgIsEnabledRequest = Message.obtain(null, MSG_IS_ENABLED_REQUEST);
	public static final Message MsgStartDiscoverRequest = Message.obtain(null, MSG_START_DISCOVER_REQUEST);
	public static final Message MsgStopDiscoveryRequest = Message.obtain(null, MSG_STOP_DISCOVER_REQUEST);
	
	public static final String KEY_MSG_DEVICE_DISCOVERED_RESPONSE_NAME = "name";
	public static final String KEY_MSG_DEVICE_DISCOVERED_RESPONSE_ADDRESS = "address";
	public static final String KEY_MSG_DEVICE_DISCOVERED_RESPONSE_CLASS = "class";
	
	public static final String KEY_MSG_IS_ENABLED_RESPONSE_VALUE = "value";
	
	public static final String KEY_MSG_CONNECT_TO_DEVICE_REQUEST_ADDRESS = "address";
	public static final String KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS = "address";
	
	public static final String KEY_MSG_DATA_CHUNK_RESPONSE_DATA = "data";
	
	private final String TAG = "QMC:QBluetoothService";
	
	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver receiver;
	private QBluetoothSocket socket;
}
