package com.awojcik.qmc.services.bluetooth;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.os.Message;

public class BluetoothServiceMessages
{
    public static final int MSG_IS_ENABLED_REQUEST = 1;
    public static final int MSG_IS_ENABLED_RESPONSE = 2;
    public static final int MSG_ENABLE_REQUEST = 3;
    public static final int MSG_DISABLE_REQUEST = 4;
    public static final int MSG_START_DISCOVER_REQUEST = 5;
    public static final int MSG_STOP_DISCOVER_REQUEST = 6;
    public static final int MSG_DISCOVERY_STARTED_RESPONSE = 7;
    public static final int MSG_DISCOVERY_FINISHED_RESPONSE = 8;
    public static final int MSG_DEVICE_DISCOVERED_RESPONSE = 9;
    public static final int MSG_CONNECT_TO_DEVICE_REQUEST = 10;
    public static final int MSG_DEVICE_CONNECTED_RESPONSE = 11;
    public static final int MSG_DATA_CHUNK_RESPONSE = 12;
    public static final int MSG_SEND_DATA_CHUNK_REQUEST = 13;

    public static final String KEY_MSG_DEVICE_DISCOVERED_RESPONSE_NAME = "name";
    public static final String KEY_MSG_DEVICE_DISCOVERED_RESPONSE_ADDRESS = "address";
    public static final String KEY_MSG_DEVICE_DISCOVERED_RESPONSE_CLASS = "class";
    public static final String KEY_MSG_IS_ENABLED_RESPONSE_VALUE = "value";
    public static final String KEY_MSG_CONNECT_TO_DEVICE_REQUEST_ADDRESS = "address";
    public static final String KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS = "address";
    public static final String KEY_MSG_DATA_CHUNK_RESPONSE_DATA = "data";
    public static final String KEY_MSG_SEND_DATA_CHUNK_REQUEST_DATA = "data";

    public static Message createStartDiscoveryMessage()
    {
        return Message.obtain(null, MSG_START_DISCOVER_REQUEST);
    }

    public static Message createStopDiscoveryMessage()
    {
        return Message.obtain(null, MSG_STOP_DISCOVER_REQUEST);
    }

    public static Message createDiscoveryStartedMessage()
    {
        return Message.obtain(null, MSG_DISCOVERY_STARTED_RESPONSE);
    }

    public static Message createDiscoveryFinishedMessage()
    {
        return Message.obtain(null, MSG_DISCOVERY_FINISHED_RESPONSE);
    }

    public static Message createDeviceDiscoveredMessage(String name, String address, BluetoothClass deviceClass)
    {
        Message msg = new Message();
        msg.what = MSG_DEVICE_DISCOVERED_RESPONSE;
        Bundle data = new Bundle();
        data.putString(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_NAME, name);
        data.putString(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_ADDRESS, address);
        data.putParcelable(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_CLASS, deviceClass);
        msg.setData(data);
        return msg;
    }

    public static Message createConnectMessage(String address)
    {
        Message msg = new Message();
        msg.what = MSG_CONNECT_TO_DEVICE_REQUEST;
        Bundle data = new Bundle();
        data.putString(KEY_MSG_CONNECT_TO_DEVICE_REQUEST_ADDRESS, address);
        msg.setData(data);
        return msg;
    }

    public static Message createIsEnabledMessage(boolean isEnabled)
    {
        Message msg = Message.obtain(null, MSG_IS_ENABLED_RESPONSE);
        Bundle data = new Bundle();
        data.putBoolean(KEY_MSG_IS_ENABLED_RESPONSE_VALUE, isEnabled);
        msg.setData(data);
        return msg;
    }

    public static Message createConnectedMessage(String address)
    {
        Message msg = Message.obtain(null, MSG_DEVICE_CONNECTED_RESPONSE);
        Bundle data = new Bundle();
        data.putString(KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS, address);
        msg.setData(data);
        return msg;
    }

    public static Message createDataChunkMessage(String dataChunk)
    {
        Message msg = Message.obtain(null, MSG_DATA_CHUNK_RESPONSE);
        Bundle data = new Bundle();
        data.putString(KEY_MSG_DATA_CHUNK_RESPONSE_DATA, dataChunk);
        msg.setData(data);
        return msg;
    }

    public static Message createSendDataChunkMessage(String dataChunk)
    {
        Message msg = Message.obtain(null, MSG_SEND_DATA_CHUNK_REQUEST);
        Bundle data = new Bundle();
        data.putString(KEY_MSG_SEND_DATA_CHUNK_REQUEST_DATA, dataChunk);
        msg.setData(data);
        return msg;
    }

    public static String getAddressFromConnectToDeviceMessage(Message msg)
    {
        return msg.getData().getString(KEY_MSG_CONNECT_TO_DEVICE_REQUEST_ADDRESS);
    }

    public static String getDataFromSendDataChunkMessage(Message msg)
    {
        return msg.getData().getString(KEY_MSG_SEND_DATA_CHUNK_REQUEST_DATA);
    }

    public static String getAddressFromDeviceConnectedMessage(Message msg)
    {
        return msg.getData().getString(KEY_MSG_DEVICE_CONNECTED_RESPONSE_ADDRESS);
    }

    public static String getDataFromDataChunkMessage(Message msg)
    {
        return msg.getData().getString(KEY_MSG_DATA_CHUNK_RESPONSE_DATA);
    }

    public static DiscoveredDevice getDeviceFromDeviceDiscoveredMessage(Message msg)
    {
        String address = (String)msg.getData().get(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_ADDRESS);
        String name = (String)msg.getData().get(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_NAME);
        BluetoothClass deviceClass = msg.getData().getParcelable(KEY_MSG_DEVICE_DISCOVERED_RESPONSE_CLASS);
        return new DiscoveredDevice(name, address, deviceClass);
    }

    public static class DiscoveredDevice
    {
        public DiscoveredDevice(String name, String address, BluetoothClass deviceClass)
        {
            this.name = name;
            this.address = address;
            this.deviceClass = deviceClass;
        }

        public String getName()
        {
            return this.name;
        }

        public String getAddress()
        {
            return this.address;
        }

        public BluetoothClass getDeviceClass()
        {
            return this.deviceClass;
        }

        private final String name;
        private final String address;
        private final BluetoothClass deviceClass;
    }
}
