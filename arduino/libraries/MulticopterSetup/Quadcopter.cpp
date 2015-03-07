#include <Math.h>
#include <Servo.h>
#include "../Storage/Storage.h"
#include "../MathEx/MathEx.h"
#include "../Process/Process.h"
#include "MulticopterSetup.h"
#include "Quadcopter.h"

void Quadcopter::arm()
{
  this->motors[FRONT_MOTOR].write(0);
  this->motors[REAR_MOTOR].write(0);
  this->motors[RIGHT_MOTOR].write(0);
  this->motors[LEFT_MOTOR].write(0);

  this->motors[FRONT_MOTOR].attach(this->pins[FRONT_MOTOR]);
  Process::delay(1);
  this->motors[REAR_MOTOR].attach(this->pins[REAR_MOTOR]);
  Process::delay(1);
  this->motors[RIGHT_MOTOR].attach(this->pins[RIGHT_MOTOR]);
  Process::delay(1);
  this->motors[LEFT_MOTOR].attach(this->pins[LEFT_MOTOR]);
  Process::delay(1);

  Process::delay(400);
  this->motors[FRONT_MOTOR].write(0);
  this->motors[REAR_MOTOR].write(0);
  this->motors[RIGHT_MOTOR].write(0);
  this->motors[LEFT_MOTOR].write(0);
  Process::delay(2000);
}

void Quadcopter::disarm()
{
  this->motors[FRONT_MOTOR].write(0);
  this->motors[REAR_MOTOR].write(0);
  this->motors[RIGHT_MOTOR].write(0);
  this->motors[LEFT_MOTOR].write(0);

  Process::delay(100);
  
  this->motors[FRONT_MOTOR].detach();
  this->motors[REAR_MOTOR].detach();
  this->motors[RIGHT_MOTOR].detach();
  this->motors[LEFT_MOTOR].detach();
}

void Quadcopter::update(float throttle, float fRollPID, float fPitchPID, float fYawPID)
{
  uint16_t newThrottle = this->getThrottleInMicroSec(throttle);

  int32_t motorsThrottle[MOTORS_COUNT];
  
  uint16_t yawPID = map(fYawPID, 0.0, 1.0, this->data.minPidValue, this->data.maxPidValue);
  uint16_t rollPID = map(fRollPID, 0.0, 1.0, this->data.minPidValue, this->data.maxPidValue);
  uint16_t pitchPID = map(fPitchPID, 0.0, 1.0, this->data.minPidValue, this->data.maxPidValue);
  
  #ifdef QUADCOPTER_PLUS
  motorsThrottle[RIGHT_MOTOR] = newThrottle + pitchPID - yawPID;
  motorsThrottle[LEFT_MOTOR]  = newThrottle - pitchPID - yawPID;
  motorsThrottle[FRONT_MOTOR] = newThrottle + rollPID + yawPID;
  motorsThrottle[REAR_MOTOR]  = newThrottle - rollPID + yawPID;
  #else // QUADCOPTER_X
  motorsThrottle[LEFT_MOTOR] = newThrottle + pitchPID - rollPID - yawPID;
  motorsThrottle[RIGHT_MOTOR]  = newThrottle - pitchPID + rollPID - yawPID;
  motorsThrottle[REAR_MOTOR] = newThrottle + pitchPID - rollPID + yawPID;
  motorsThrottle[FRONT_MOTOR]  = newThrottle - pitchPID + rollPID + yawPID;
  #endif

  int32_t highestThrottle = MathEx::arrayMax(motorsThrottle, MOTORS_COUNT);
  int32_t throttleOutpass = MathEx::maxEx(0, (int32_t)(highestThrottle - this->data.motorMaxSignal));

  motorsThrottle[FRONT_MOTOR] -= throttleOutpass;
  motorsThrottle[REAR_MOTOR]  -= throttleOutpass;
  motorsThrottle[RIGHT_MOTOR] -= throttleOutpass;
  motorsThrottle[LEFT_MOTOR]  -= throttleOutpass;

  if (motorsThrottle[FRONT_MOTOR] < this->data.motorMinSignal) motorsThrottle[FRONT_MOTOR] = this->data.motorMinSignal;
  if (motorsThrottle[REAR_MOTOR] < this->data.motorMinSignal) motorsThrottle[REAR_MOTOR] = this->data.motorMinSignal;
  if (motorsThrottle[RIGHT_MOTOR] < this->data.motorMinSignal) motorsThrottle[RIGHT_MOTOR] = this->data.motorMinSignal;
  if (motorsThrottle[LEFT_MOTOR] < this->data.motorMinSignal) motorsThrottle[LEFT_MOTOR] = this->data.motorMinSignal;

  this->motors[FRONT_MOTOR].writeMicroseconds(motorsThrottle[FRONT_MOTOR]);
  this->motors[REAR_MOTOR].writeMicroseconds(motorsThrottle[REAR_MOTOR]);
  this->motors[RIGHT_MOTOR].writeMicroseconds(motorsThrottle[RIGHT_MOTOR]);
  this->motors[LEFT_MOTOR].writeMicroseconds(motorsThrottle[LEFT_MOTOR]);
}


uint16_t Quadcopter::getThrottleInMicroSec(float throttle)
{
  throttle = MathEx::truncate(throttle, 0.0, 1.0);
  return throttle * (float)(this->data.motorMaxSignal - this->data.motorMinSignal) + this->data.motorMinSignal;
}