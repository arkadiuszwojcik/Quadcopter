#include <Arduino.h>
#include <Wire.h>
#include <Math.h>
#include <Servo.h>
#include <Process.h>
#include <I2C.h>
#include <BMA180.h>
#include <ITG3200.h>
#include <HMC5883L.h>
#include <BMA180Accelerometer.h>
#include <ITG3200Gyroscope.h>
#include <HMC5883LMagnetometer.h>
#include <IMU.h>
#include <PID.h>
#include <MulticopterData.h>
#include <MulticopterSetup.h>
#include <Quadcopter.h>
#include <AxisCascadeControl.h>
#include <ControlSystem.h>
#include <Multicopter.h>

I2C i2c;
ITG3200Gyroscope gyroscope(i2c);
BMA180Accelerometer accelerometer(i2c);
HMC5883LMagnetometer magnetometer(i2c);
FreeIMU imu(accelerometer, gyroscope, magnetometer);

MulticopterData data;
Quadcopter quadcopter(12,11,10,9);
Multicopter multicopter(quadcopter, data, imu);

void setup()
{
  data.load();
}

void loop()
{
}