#ifndef ControlSystem_h
#define ControlSystem_h

class ControlSystem
{
 public:
 ControlSystem(PersistentData& data, FreeImu& imu, IMulticopterSetup& multicopter)
   : data(data), imu(imu), multicopter(multicopter)
    {}

  void update(float dt);

 private:
  AxisCascadeControl rollController;
  AxisCascadeControl pitchController;
  AxisCascadeControl yawController;

  FreeImu& imu;
  PersistentData& data;
  IMulticopterSetup& multicopter;

  float rollAngle;
  float pitchAngle;
  float yawAngle;

  float gyroRollRate;
  float gyroPitchRate;
  float gyroYawRate;

  int* rollPid;
  int* pitchPid;
};

#endif
