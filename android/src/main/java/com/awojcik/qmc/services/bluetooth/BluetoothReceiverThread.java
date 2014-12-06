package com.awojcik.qmc.services.bluetooth;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

class BluetoothReceiverThread implements Runnable
{
	private final String TAG = "BluetoothReceiverThread";
	
	public BluetoothReceiverThread(BluetoothSocketWrapper socket)
	{
		this.socket = socket;
		
		this.readBuffer = new byte[1024];
		this.stopWorker = false;
	}
	
	public void run() 
	{
        try
        {
            this.getInputStream();
            this.receiverLoop();
        }
        catch (IOException ex)
        {
            Log.e(TAG, ex.getMessage());
            this.stopWorker = true;
        }
	}

    private void getInputStream() throws IOException
    {
        this.stream = socket.getInternalBluetoothSocket().getInputStream();
    }

    private void receiverLoop() throws IOException
    {
        while (!Thread.currentThread().isInterrupted() && !this.stopWorker)
        {
            int bytesAvailable = this.stream.available();

            if (!(bytesAvailable > 0)) continue;

            byte[] packet = this.readPacket(bytesAvailable);
            this.processPacket(packet);
        }
    }
    
    private byte[] readPacket(int count) throws IOException
    {
        byte[] packet = new byte[count];
        this.stream.read(packet);
        return packet;
    }

    private void processPacket(byte[] packet) throws IOException
    {
        for (int i=0; i<packet.length; i++)
        {
            byte b = packet[i];
            if (b == Delimiter)
            {
                final String data = new String(this.readBuffer, 0, this.readBufferPosition, "US-ASCII");
                this.socket.getCallbackObject().receivedData(data);
                this.readBufferPosition = 0;
            }
            else
            {
                this.readBuffer[this.readBufferPosition++] = b;
            }
        }
    }
	
	private int readBufferPosition;
	private byte[] readBuffer;
	private boolean stopWorker;
	private InputStream stream;

	private final BluetoothSocketWrapper socket;
	private static final byte Delimiter = '\n';
}
