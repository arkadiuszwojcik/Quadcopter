package com.awojcik.qmc;

import gueei.binding.Binder;
import gueei.binding.v30.DefaultKernelV30;
import android.app.Application;

public class QApplication extends Application 
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		// init andoid-binding
		Binder.init(this, new DefaultKernelV30());
	}
}
