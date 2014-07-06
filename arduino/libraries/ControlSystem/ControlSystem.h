#ifndef ControlSystem_h
#define ControlSystem_h

class ControlSystem
{
 public:
 ControlSystem(MulticopterData& data, FreeIMU& imu, MulticopterSetup& multicopter)
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
  MulticopterSetup& multicopter;

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
