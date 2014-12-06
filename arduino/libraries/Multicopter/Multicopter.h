#ifndef Multicopter_h
#define Multicopter_h

class Multicopter
{
 public:
  Multicopter(MulticopterSetup& setup, Storage& storage, FreeIMU& imu)
  : data(storage.getData())
  , multicopterSetup(setup)
  , imu(imu)
  , controlSystem(storage.getData().controlSystemData)
  {}

  void arm()
  {
    this->multicopterSetup.arm();
  }

  void disarm()
  {
    this->multicopterSetup.disarm();
  }
  
  // throttle range 0.0 to 1.0
  void setThrottle(float value)
  {
    this->throttle = value;
  }

  void update(float dt, float* pids, float* angles, uint16_t* motorData)
  {
    float ypr[3];
    float yawPID, rollPID, pitchPID;
    yawPID = rollPID = pitchPID = 0;

    this->imu.getYawPitchRoll(ypr);
	#ifdef QUADCOPTER_PLUS
    MathEx::rotateZ45(&ypr[2], &ypr[1], &ypr[0]);
	#endif

    this->controlSystem.update(dt, ypr[2], ypr[1], ypr[0], &yawPID, &rollPID, &pitchPID);
    this->multicopterSetup.setMotorThrottleRange(this->data.motorMinSignal, this->data.motorMaxSignal);
    this->multicopterSetup.update(this->throttle, rollPID, pitchPID, yawPID, motorData);

    pids[0] = yawPID;
    pids[1] = rollPID;
    pids[2] = pitchPID;

    angles[0] = ypr[0];
    angles[1] = ypr[1];
    angles[2] = ypr[2];
  }

 private:
  float throttle;
  FreeIMU& imu;
  FPersistentData& data;
  MulticopterSetup& multicopterSetup;
  ControlSystem controlSystem;
};

#endif
