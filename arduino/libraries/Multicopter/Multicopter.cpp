#include "Multicopter.h"

Multicopter::Multicopter(IMulticopterSetup& setup)
  : multicopterSetup(setup)
{}

void Multicopter::arm()
{
  this->multicopterSetup.arm();
}

void Multicopter::setThrottle(float value)
{
  this->throttle = value;
}

void Multicopter::setMotorsThrottleRange(uint16_t minMicroSec, unit16_t maxMicroSec)
{
  this->multicopterSetup.setMotorsThrottleRange(minMicroSec, maxMicroSec);
}
