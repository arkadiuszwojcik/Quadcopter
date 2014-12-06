package com.awojcik.qmc.services.bluetooth;

import com.awojcik.qmc.services.AbstractService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Message;
import android.util.Log;

public class BluetoothService extends AbstractService
{
	@Override
	public void onStartService() 
	{
		this.broadcastReceiver = new BluetoothBroadcastReceiver(new BroadcastReceiverCallback());
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.registerReceiver(this.broadcastReceiver, this.createBluetoothIntentFilter());
	}
	
	@Override
	public void onStopService()
	{
		this.unregisterReceiver(this.broadcastReceiver);
	}

	@Override
	public void onReceiveMessage(Message msg)
	{
		if (msg.what == BluetoothServiceMessages.MSG_ENABLE_REQUEST)
		{
			Log.d(TAG, "Received: MSG_ENABLE_REQUEST");
			this.enableBluetooth();
		}
		
		else if (msg.what == BluetoothServiceMessages.MSG_DISABLE_REQUEST)
		{
			Log.d(TAG, "Received: MSG_DISABLE_REQUEST");
			this.disableBluetooth();
		}
		
		else if (msg.what == BluetoothServiceMessages.MSG_START_DISCOVER_REQUEST)
		{
			Log.d(TAG, "Received: MSG_START_DISCOVER_REQUEST");
			this.discoverDevices();
		}
		
		else if (msg.what == BluetoothServiceMessages.MSG_STOP_DISCOVER_REQUEST)
		{
			Log.d(TAG, "Received: MSG_STOP_DISCOVER_REQUEST");
			this.stopDiscoverDevices();
		}
		
		else if (msg.what == BluetoothServiceMessages.MSG_IS_ENABLED_REQUEST)
		{
			Log.d(TAG, "Received: MSG_IS_ENABLED_REQUEST");
			this.isEnabled();
		}
		
		else if (msg.what == BluetoothServiceMessages.MSG_CONNECT_TO_DEVICE_REQUEST)
		{
			Log.d(TAG, "Received: MSG_CONNECT_TO_DEVICE_REQUEST");
			this.connect(BluetoothServiceMessages.getAddressFromConnectToDeviceMessage(msg));
		}

        else if (msg.what == BluetoothServiceMessages.MSG_SEND_DATA_CHUNK_REQUEST)
        {
            Log.d(TAG, "Received: MSG_SEND_DATA_CHUNK_REQUEST");
            String data = BluetoothServiceMessages.getDataFromSendDataChunkMessage(msg);
            Log.d(TAG, "Try to send: " + data);
            this.sendDataChunk(data);
        }
	}
	
	class BroadcastReceiverCallback implements BluetoothBroadcastReceiverCallback
	{
		public void deviceDiscovered(String name, String address, BluetoothClass deviceClass) 
		{
			Log.d(TAG, "Sending: MSG_DEVICE_DISCOVERED_RESPONSE");
            Message msg = BluetoothServiceMessages.createDeviceDiscoveredMessage(name, address, deviceClass);
			BluetoothService.this.send(msg);
		}

		public void discoveryStarted()
		{
			Log.d(TAG, "Sending: MSG_DISCOVERY_STARTED_RESPONSE");
			Message msg = BluetoothServiceMessages.createDiscoveryStartedMessage();
			BluetoothService.this.send(msg);
		}
		
		public void discoveryFinished()
		{
			Log.d(TAG, "Sending: MSG_DISCOVERY_FINISHED_RESPONSE");
			Message msg = BluetoothServiceMessages.createDiscoveryFinishedMessage();
			BluetoothService.this.send(msg);
		}
    }
	
	class SocketCallback implements BluetoothSocketCallback
	{
		public void receivedData(String data)
		{
			BluetoothService.this.sendDataChunkResponse(data);
		}	
	}

    private IntentFilter createBluetoothIntentFilter()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        return filter;
    }

    private void sendDataChunk(String data)
    {
        if (this.socket != null)
        {
            this.socket.writeData(data);
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
			
			this.socket = new BluetoothSocketWrapper(device, new SocketCallback());
			
			if (this.socket.connect() == true)
			{
				this.sendConnectedResponse(address);
			}
		} 
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void sendIsEnabledResponse(boolean isEnabled)
	{
		Message msg = BluetoothServiceMessages.createIsEnabledMessage(isEnabled);
		BluetoothService.this.send(msg);
	}
	
	private void sendConnectedResponse(String address)
	{
		Message msg = BluetoothServiceMessages.createConnectedMessage(address);
		BluetoothService.this.send(msg);
	}
	
	private void sendDataChunkResponse(String dataChunk)
	{
		Message msg = BluetoothServiceMessages.createDataChunkMessage(dataChunk);
		BluetoothService.this.send(msg);
	}
	
	private BluetoothAdapter bluetoothAdapter;
	private BroadcastReceiver broadcastReceiver;
	private BluetoothSocketWrapper socket;
    private static final String TAG = "BluetoothService";
}
