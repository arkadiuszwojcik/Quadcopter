#ifndef MulticopterSetup_h
#define MulticopterSetup_h

class MulticopterSetup
{
 public:
  virtual void arm()=0;
  virtual void disarm()=0;
  virtual void update(float throttle, float rollPID, float pitchPID, float yawPID)=0;
};

#endif
