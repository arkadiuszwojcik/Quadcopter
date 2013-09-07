package com.awojcik.qmc.activities;

import roboguice.RoboGuice;

import com.awojcik.qmc.R;
import com.awojcik.qmc.mvvm.viewmodels.QMainViewModel;
import com.awojcik.qmc.providers.QBluetoothServiceProvider;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.utilities.QRoboBindingActivity;
import com.google.inject.Inject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends QRoboBindingActivity
{
    private static String TAG = "QMC:MainActivity";

	@Inject 
	private QBluetoothServiceProvider bluetoothServiceProvider;
	
	private ServiceManager bluetoothService;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        this.switchToFullScreenMode();
        this.startBluetoothService();
        this.bindViewModel();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }
    
    private void switchToFullScreenMode()
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    private void startBluetoothService()
    {
    	this.bluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
        this.bluetoothService.start();
    }
    
    private void bindViewModel()
    {
    	QMainViewModel mainViewModel = RoboGuice.getInjector(this).getInstance(QMainViewModel.class);
    	this.inflateAndBind(R.xml.mainactivity_metadata, mainViewModel);
    }
    
    class BluetoothServiceHandler extends Handler
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    	}
    }

}

