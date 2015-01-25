#ifndef IMU_h
#define IMU_h

#include "../Sensors/Sensors.h"

#define twoKpDef  (2.0f * 0.5f) // 2 * proportional gain
#define twoKiDef  (2.0f * 0.1f) // 2 * integral gain

/*
 Based on FreeIMU project
*/
class FreeIMU
{
public:
  FreeIMU(IAccelerometer& acc, IGyroscope& gyro, IMagnetometer& magn);

  void reset();
  void getEuler(float * angles);
  void getYawPitchRoll(float * ypr);
  void getEulerRad(float * angles);
  void getYawPitchRollRad(float * ypr);
  void getGyroYawPitchRollRates(float * ypr);

private:
  void getQuaternion(float * q);
  void getSensorsValues(float * values);
  void updateAHRS(float gx, float gy, float gz, float ax, float ay, float az, float mx, float my, float mz);

  IAccelerometer& accelerometer;
  IGyroscope& gyroscope;
  IMagnetometer& magnetometer;

  float iq0, iq1, iq2, iq3;
  float exInt, eyInt, ezInt;  // scaled integral error
  volatile float twoKp;      // 2 * proportional gain (Kp)
  volatile float twoKi;      // 2 * integral gain (Ki)
  volatile float q0, q1, q2, q3; // quaternion of sensor frame relative to auxiliary frame
  volatile float integralFBx,  integralFBy, integralFBz;
  unsigned long lastUpdate, now; // sample period expressed in milliseconds
  float sampleFreq; // half the sample period expressed in seconds
  float gyroYawRate, gyroPitchRate, gyroRollRate;
};

#endif