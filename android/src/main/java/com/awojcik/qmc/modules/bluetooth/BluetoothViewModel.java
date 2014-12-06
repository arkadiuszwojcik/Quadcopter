package com.awojcik.qmc.modules.bluetooth;

import com.awojcik.qmc.modules.common.IIntraModuleMessageListener;
import com.awojcik.qmc.modules.common.IntraModuleMessenger;
import com.awojcik.qmc.services.bluetooth.BluetoothService;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.awojcik.qmc.services.AbstractService;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceMessages;
import com.google.inject.Inject;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.IntegerObservable;

public class BluetoothViewModel 
{	
	/*
	 * Commands
	 */
	public final ScanCommand BluetoothScanCommand;
	
	public final StopScanCommand BluetoothStopScanCommand;
	
	public final ConnectCommand BluetoothConnectCommand;
		
	/*
	 * Observables
	 */
	public final IntegerObservable ScanningVisibility = new IntegerObservable(View.GONE);
	
	public final IntegerObservable ScanningVisibilityNegation = new IntegerObservable(View.VISIBLE);
	
	public final ArrayListObservable<DeviceEntryViewModel> Devices =
			new ArrayListObservable<DeviceEntryViewModel>(DeviceEntryViewModel.class);
	
	public final Observable<Object> ClickedItem = 
			new Observable<Object>(Object.class);
	
	public final OnCancelListener CancelListener = new CancelListener();
	
	/*
	 * Private
	 */
	private ServiceManager mBluetoothService;
	
	private IntraModuleMessenger mIntraModuleMessenger;
	
	@Inject
	public BluetoothViewModel(
			BluetoothServiceFactory bluetoothServiceProvider,
			IntraModuleMessenger intraModuleMessenger,
			ScanCommand scanCommand,
			StopScanCommand stopScanCommand,
			ConnectCommand connectCommand)
	{
		this.mBluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
		this.mIntraModuleMessenger = intraModuleMessenger;
		this.mIntraModuleMessenger.register(new IntraMessageListener());
		
		this.BluetoothScanCommand = scanCommand;
		this.BluetoothStopScanCommand = stopScanCommand;
		this.BluetoothConnectCommand = connectCommand;
	}
	
	private void sendMessage(Message msg)
	{
		try
		{
			this.mBluetoothService.send(msg);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private void onStartScanning()
	{
		this.ScanningVisibility.set(View.VISIBLE);
		this.ScanningVisibilityNegation.set(View.GONE);
	}
	
	private void onFinishScanning()
	{
		this.ScanningVisibility.set(View.GONE);
		this.ScanningVisibilityNegation.set(View.VISIBLE);
	}
	
	private void onNewDevice(String name, String address, BluetoothClass deviceClass)
	{
		if (name == null && address == null) return;
		
		name = name == null ? address : name;
		
		this.addDevice(name, address, deviceClass);
	}
	
	private void addDevice(String name, String address, BluetoothClass deviceClass)
	{	
		for (DeviceEntryViewModel device : Devices)
		{
			if (device.Name.get().equals(name)) return;
		}
		
		this.Devices.add(new DeviceEntryViewModel(name, address, deviceClass));
	}
	
	class CancelListener implements OnCancelListener
	{
		public void onCancel(DialogInterface dialog) 
		{
			Message msg = BluetoothServiceMessages.createStopDiscoveryMessage();
			BluetoothViewModel.this.sendMessage(msg);
		}
	}
	
	class IntraMessageListener implements IIntraModuleMessageListener
	{
		public void onMessage(Object message) 
		{
			if (message instanceof ScanMessage)
			{
				Message msg = BluetoothServiceMessages.createStartDiscoveryMessage();
				BluetoothViewModel.this.sendMessage(msg);
			}
			
			if (message instanceof StopScanMessage)
			{
				Message msg = BluetoothServiceMessages.createStopDiscoveryMessage();
				BluetoothViewModel.this.sendMessage(msg);
			}
			
			if (message instanceof ConnectMessage)
			{
				String address = ((DeviceEntryViewModel)BluetoothViewModel.this.ClickedItem.get()).Address.get();
				Message msg = BluetoothServiceMessages.createConnectMessage(address);
				BluetoothViewModel.this.sendMessage(msg);
			}
		}
	}
	
	class BluetoothServiceHandler extends Handler
    {
		private static final String TAG = "DevicesViewModel.BluetoothServiceHandler";
		
    	@Override
    	public void handleMessage(Message msg)
    	{
    		try
    		{
    			if (msg.what == AbstractService.MSG_REGISTER_CLIENT)
    			{
    				Message m = BluetoothServiceMessages.createStartDiscoveryMessage();
    				BluetoothViewModel.this.sendMessage(m);
    			}
    			if (msg.what == BluetoothServiceMessages.MSG_DISCOVERY_STARTED_RESPONSE)
    			{
    				BluetoothViewModel.this.onStartScanning();
    			}
	    		if (msg.what == BluetoothServiceMessages.MSG_DISCOVERY_FINISHED_RESPONSE)
	    		{
	    			BluetoothViewModel.this.onFinishScanning();
	    		}
	    		if (msg.what == BluetoothServiceMessages.MSG_DEVICE_DISCOVERED_RESPONSE)
	    		{
                    BluetoothServiceMessages.DiscoveredDevice dev = BluetoothServiceMessages.getDeviceFromDeviceDiscoveredMessage(msg);
	    			BluetoothViewModel.this.onNewDevice(dev.getName(), dev.getAddress(), dev.getDeviceClass());
	    		}
    		} 
    		catch (Exception e)
    		{
    			Log.e(TAG, e.getMessage());
    		}
    	}
    }
}
