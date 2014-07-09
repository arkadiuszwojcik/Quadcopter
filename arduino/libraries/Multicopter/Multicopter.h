#ifndef Multicopter_h
#define Multicopter_h

class Multicopter
{
 public:
  Multicopter(MulticopterSetup& setup, MulticopterData& data, FreeIMU& imu)
	: multicopterSetup(setup)
	, multicopterData(data)
	, controlSystem(data, imu, setup)
	{}

  void arm()
  {
	this->multicopterSetup.arm();
  }
  
  // throttle range 0.0 to 1.0
  void setThrottle(float value)
  {
	this->throttle = value;
  }
  
  void setMotorsThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec)
  {
	this->multicopterSetup.setMotorThrottleRange(minMicroSec, maxMicroSec);
  }

 private:
  float throttle;
  MulticopterSetup& multicopterSetup;
  MulticopterData& multicopterData;
  ControlSystem controlSystem;
};

#endif
