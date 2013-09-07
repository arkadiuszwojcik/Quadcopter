package com.awojcik.qmc.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class ActivityExtensions 
{
	public static void closeInputControl(Activity activity)
	{
		InputMethodManager inputManager = (InputMethodManager)
				activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
