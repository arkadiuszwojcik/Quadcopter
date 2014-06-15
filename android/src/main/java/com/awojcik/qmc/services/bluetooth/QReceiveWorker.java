package com.awojcik.qmc.services.bluetooth;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

class QReceiveWorker implements Runnable
{
	private final String TAG = "QReceiveWorker";
	
	public QReceiveWorker(QBluetoothSocket socket)
	{
		this.socket = socket;
		this.callback = socket.getCallbackObject();
		
		this.readBuffer = new byte[1024];
		this.stopWorker = false;
	}
	
	public void run() 
	{
		try
		{
			this.stream = socket.getInternalBluetoothSocket().getInputStream();
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage());
			return;
		}
		
		while(!Thread.currentThread().isInterrupted() && !this.stopWorker)
		{
			try 
            {
                int bytesAvailable = this.stream.available();
                
                if(bytesAvailable > 0)
                {	
                    byte[] packetBytes = new byte[bytesAvailable];
                    this.stream.read(packetBytes);
                    for(int i=0; i<bytesAvailable; i++)
                    {
                        byte b = packetBytes[i];
                        if(b == Delimiter)
                        {
                            byte[] encodedBytes = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            this.callback.receivedData(data);
                            this.readBufferPosition = 0;
                        }
                        else
                        {
                            this.readBuffer[this.readBufferPosition++] = b;
                        }
                    }
                }
            } 
            catch (IOException ex) 
            {
                this.stopWorker = true;
            }
		}
	}
	
	private int readBufferPosition;
	private byte[] readBuffer;
	private boolean stopWorker;
	private InputStream stream;
	
	private final IQBluetoothSocketCallback callback;
	private final QBluetoothSocket socket;
	private static final byte Delimiter = '\n';
}
