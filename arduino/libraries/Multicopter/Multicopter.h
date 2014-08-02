#ifndef Multicopter_h
#define Multicopter_h

class Multicopter
{
 public:
  Multicopter(MulticopterSetup& setup, Storage& storage, FreeIMU& imu)
  : data(storage.getData())
  , multicopterSetup(setup)
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

  void update(float dt, float roll, float pitch, float yaw, int32_t* out1, float* out2)
  {
    float yawPID, rollPID, pitchPID;
    yawPID = rollPID = pitchPID = 0;
    this->controlSystem.update(dt, roll, pitch, yaw, &yawPID, &rollPID, &pitchPID);
    this->controlSystem.debug(&out2[0], &out2[1], &out2[2]);
    //out2[0] = pitch;
    //out2[1] = roll;
    //out2[2] = yaw;
    out2[3] = dt;
    this->multicopterSetup.setMotorThrottleRange(this->data.motorMinSignal, this->data.motorMaxSignal);
    this->multicopterSetup.update(this->throttle, rollPID, pitchPID, yawPID, out1);
  }

 private:
  float throttle;
  FPersistentData& data;
  MulticopterSetup& multicopterSetup;
  ControlSystem controlSystem;
};

#endif
