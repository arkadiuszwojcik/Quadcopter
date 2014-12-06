package com.awojcik.qmc.modules.imu;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;

import com.awojcik.qmc.R;
import com.awojcik.qmc.modules.messages.MessageConverter;
import com.awojcik.qmc.opengl.GLSurfaceView;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceFactory;
import com.awojcik.qmc.services.bluetooth.BluetoothService;
import com.awojcik.qmc.services.bluetooth.BluetoothServiceMessages;
import com.google.inject.Inject;

import de.greenrobot.event.EventBus;

public class ImuViewModel
{
    private final Activity activity;

    private final EventBus eventBus;

    @Inject
    public ImuViewModel(Activity activity, EventBus eventBus, BluetoothServiceFactory bluetoothServiceProvider)
    {
        this.activity = activity;
        this.eventBus = eventBus;
        bluetoothServiceProvider.Create(new BluetoothServiceHandler());
    }

    public void init()
    {
        this.setupOpenGl();
    }

    private void setupOpenGl()
    {
        FrameLayout frame = (FrameLayout)this.activity.findViewById(R.id.glSurfaceFrame);
        frame.addView(new GLSurfaceView(this.activity, new ImuScene(this.eventBus)));
    }

    class BluetoothServiceHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == BluetoothServiceMessages.MSG_DATA_CHUNK_RESPONSE)
            {
                String data = BluetoothServiceMessages.getDataFromDataChunkMessage(msg);
                Object m = MessageConverter.fromString(data);
                if (m != null) ImuViewModel.this.eventBus.post(m);
            }
        }
    }
}
