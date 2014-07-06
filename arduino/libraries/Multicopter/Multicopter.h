#ifndef Multicopter_h
#define Multicopter_h

class Multicopter
{
 public:
  Multicopter(MulticopterSetup& setup, MulticopterData& data)
	: multicopterSetup(setup)
	, multicopterData(data)
	{}

  void arm();
  // throttle range 0.0 to 1.0
  void setThrottle(float value);
  void setMotorsThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec);

 private:
  float throttle;
  MulticopterSetup& multicopterSetup;
  MulticopterData& multicopterData;
  ControlSystem controlSystem;
};

#endif
