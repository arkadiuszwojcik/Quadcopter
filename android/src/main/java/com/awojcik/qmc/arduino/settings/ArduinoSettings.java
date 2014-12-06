package com.awojcik.qmc.arduino.settings;

public class ArduinoSettings
{
    private float[] gyroscopeOffsets = new float[3];
    private float[] accelerometerOffsets = new float[3];
    private float[] magnetometerOffsets = new float[3];
    private float magnetometerDeclination;
    private float[] rollMasterPID = new float[3];
    private float[] rollSlavePID = new float[3];
    private float[] pitchMasterPID = new float[3];
    private float[] pitchSlavePID = new float[3];
    private float[] yawMasterPID = new float[3];
    private float[] yawSlavePID = new float[3];
    private float minPIDvalue;
    private float maxPIDvalue;
    private int motorMinSignal;
    private int motorMaxSignal;

    public float[] getGyroscopeOffsets() {
        return gyroscopeOffsets;
    }

    public void setGyroscopeOffsets(float[] gyroscopeOffsets) {
        this.gyroscopeOffsets = gyroscopeOffsets;
    }

    public float[] getAccelerometerOffsets() {
        return accelerometerOffsets;
    }

    public void setAccelerometerOffsets(float[] accelerometerOffsets) {
        this.accelerometerOffsets = accelerometerOffsets;
    }

    public float[] getMagnetometerOffsets() {
        return magnetometerOffsets;
    }

    public void setMagnetometerOffsets(float[] magnetometerOffsets) {
        this.magnetometerOffsets = magnetometerOffsets;
    }

    public float getMagnetometerDeclination() {
        return magnetometerDeclination;
    }

    public void setMagnetometerDeclination(float magnetometerDeclination) {
        this.magnetometerDeclination = magnetometerDeclination;
    }

    public float[] getRollMasterPID() {
        return rollMasterPID;
    }

    public void setRollMasterPID(float[] rollMasterPID) {
        this.rollMasterPID = rollMasterPID;
    }

    public float[] getRollSlavePID() {
        return rollSlavePID;
    }

    public void setRollSlavePID(float[] rollSlavePID) {
        this.rollSlavePID = rollSlavePID;
    }

    public float[] getPitchMasterPID() {
        return pitchMasterPID;
    }

    public void setPitchMasterPID(float[] pitchMasterPID) {
        this.pitchMasterPID = pitchMasterPID;
    }

    public float[] getPitchSlavePID() {
        return pitchSlavePID;
    }

    public void setPitchSlavePID(float[] pitchSlavePID) {
        this.pitchSlavePID = pitchSlavePID;
    }

    public float[] getYawMasterPID() {
        return yawMasterPID;
    }

    public void setYawMasterPID(float[] yawMasterPID) {
        this.yawMasterPID = yawMasterPID;
    }

    public float[] getYawSlavePID() {
        return yawSlavePID;
    }

    public void setYawSlavePID(float[] yawSlavePID) {
        this.yawSlavePID = yawSlavePID;
    }

    public float getMinPIDvalue() {
        return minPIDvalue;
    }

    public void setMinPIDvalue(float minPIDvalue) {
        this.minPIDvalue = minPIDvalue;
    }

    public float getMaxPIDvalue() {
        return maxPIDvalue;
    }

    public void setMaxPIDvalue(float maxPIDvalue) {
        this.maxPIDvalue = maxPIDvalue;
    }

    public int getMotorMinSignal() {
        return motorMinSignal;
    }

    public void setMotorMinSignal(int motorMinSignal) {
        this.motorMinSignal = motorMinSignal;
    }

    public int getMotorMaxSignal() {
        return motorMaxSignal;
    }

    public void setMotorMaxSignal(int motorMaxSignal) {
        this.motorMaxSignal = motorMaxSignal;
    }
}
