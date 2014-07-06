#include "../MulticopterSetup/MulticopterSetup.h"
#include "../ControlSystem/ControlSystem.h"
#include "Multicopter.h"

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
