package com.awojcik.qmc.modules.messages;

public class MsgImu
{
    final float roll;
    final float pitch;
    final float yaw;

    public MsgImu(float roll, float pitch, float yaw)
    {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getRoll()
    {
        return this.roll;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public float getYaw()
    {
        return this.yaw;
    }
}
