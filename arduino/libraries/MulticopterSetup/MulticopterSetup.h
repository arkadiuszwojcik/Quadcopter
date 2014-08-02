#ifndef MulticopterSetup_h
#define MulticopterSetup_h

class MulticopterSetup
{
 public:
  virtual void arm()=0;
  virtual void disarm()=0;
  virtual void setMotorThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec)=0;
  virtual void update(float throttle, uint16_t rollPID, uint16_t pitchPID, uint16_t yawPID, int32_t* out)=0;
};

#endif
