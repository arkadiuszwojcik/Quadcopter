package com.awojcik.qmc.activities;

import roboguice.RoboGuice;

import com.awojcik.qmc.R;
import com.awojcik.qmc.mvvm.viewmodels.QMainViewModel;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.utilities.RoboBindingActivity;
import com.google.inject.Inject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

public class MainActivity extends RoboBindingActivity
{
    private static String TAG = "MainActivity";

	@Inject 
	private BluetoothServiceFactory bluetoothServiceProvider;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        this.switchToFullScreenMode();
        this.startBluetoothService();
        this.bindViewModel();

        this.setupTabControl();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }

    private void setupTabControl()
    {
        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

        this.createTabView(tabHost, "Control", R.id.tab_zero_container);
        this.createTabView(tabHost, "IMU", R.id.tab_one_container);
        this.createTabView(tabHost, "Console", R.id.tab_two_container);
        this.createTabView(tabHost, "Settings", R.id.tab_three_container);
    }

    private void createTabView(TabHost host, String name, int viewId)
    {
        TabHost.TabSpec spec = host.newTabSpec(name);
        spec.setContent(viewId);
        spec.setIndicator(name);
        host.addTab(spec);
    }
    
    private void switchToFullScreenMode()
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    private void startBluetoothService()
    {
        ServiceManager bluetoothService = bluetoothServiceProvider.Create(null);
        bluetoothService.start();
    }
    
    private void bindViewModel()
    {
    	QMainViewModel mainViewModel = RoboGuice.getInjector(this).getInstance(QMainViewModel.class);
    	this.inflateAndBind(R.xml.mainactivity_metadata, mainViewModel);
        mainViewModel.init();
    }
}

