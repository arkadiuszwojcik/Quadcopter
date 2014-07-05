#ifndef ControlSystem_h
#define ControlSystem_h

#include "../IMU/IMU.h"
#include "AxisCascadeControl.h"

class ControlSystem
{
 public:
 ControlSystem(MulticopterData& data, FreeIMU& imu, IMulticopterSetup& multicopter)
   : data(data), imu(imu), multicopter(multicopter),
   rollController(&rollAngle, &gyroRollRate, &desiredRollAngle, &rollPid),
   pitchController(&pitchAngle, &gyroPitchRate, &desiredPitchAngle, &pitchPid)
    {}

  void update(float dt);

 private:
  AxisCascadeControl rollController;
  AxisCascadeControl pitchController;

  FreeIMU& imu;
  MulticopterData& data;
  IMulticopterSetup& multicopter;

  float rollAngle;
  float pitchAngle;

  float gyroRollRate;
  float gyroPitchRate;
  
  float desiredRollAngle;
  float desiredPitchAngle;

  float rollPid;
  float pitchPid;
};

#endif
