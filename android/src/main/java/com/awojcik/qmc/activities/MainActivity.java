package com.awojcik.qmc.activities;

import de.greenrobot.event.EventBus;
import roboguice.RoboGuice;

import com.awojcik.qmc.R;
import com.awojcik.qmc.modules.imu.ImuScene;
import com.awojcik.qmc.modules.messages.MessageConverter;
import com.awojcik.qmc.opengl.GLSurfaceView;
import com.awojcik.qmc.mvvm.viewmodels.QMainViewModel;
import com.awojcik.qmc.providers.QBluetoothServiceProvider;
import com.awojcik.qmc.services.ServiceManager;
import com.awojcik.qmc.services.bluetooth.QBluetoothService;
import com.awojcik.qmc.utilities.QRoboBindingActivity;
import com.google.inject.Inject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TabHost;

public class MainActivity extends QRoboBindingActivity
{
    private static String TAG = "QMC:MainActivity";

	@Inject 
	private QBluetoothServiceProvider bluetoothServiceProvider;
	
	private ServiceManager bluetoothService;

    private EventBus eventBus;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        this.switchToFullScreenMode();
        this.startBluetoothService();
        this.bindViewModel();

        this.eventBus = new EventBus();

        //this.setupOpenGlFrame();
        this.setupTabControl();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }

    private void setupOpenGlFrame()
    {
        FrameLayout frame = (FrameLayout)this.findViewById(R.id.glSurfaceFrame);
        frame.addView(new GLSurfaceView(this, new ImuScene(this.eventBus)));
    }

    private void setupTabControl()
    {
        TabHost tabHost = (TabHost)findViewById(R.id.tab_host);
        tabHost.setup();

        this.createTabView(tabHost, "Control", R.id.tab_zero_container);
        this.createTabView(tabHost, "IMU", R.id.tab_one_container);
        this.createTabView(tabHost, "Console", R.id.tab_two_container);
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
    	this.bluetoothService = bluetoothServiceProvider.Create(new BluetoothServiceHandler());
        this.bluetoothService.start();
    }
    
    private void bindViewModel()
    {
    	QMainViewModel mainViewModel = RoboGuice.getInjector(this).getInstance(QMainViewModel.class);
    	this.inflateAndBind(R.xml.mainactivity_metadata, mainViewModel);
        mainViewModel.init();
    }
    
    class BluetoothServiceHandler extends Handler
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
            if (msg.what == QBluetoothService.MSG_DATA_CHUNK_RESPONSE)
            {
                String data = msg.getData().getString(QBluetoothService.KEY_MSG_DATA_CHUNK_RESPONSE_DATA);
                Object m = MessageConverter.fromString(data);
                if (m != null) MainActivity.this.eventBus.post(m);
            }
    	}
    }

}

