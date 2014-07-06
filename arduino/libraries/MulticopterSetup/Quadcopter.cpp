#include <Math.h>
#include <Servo.h>
#include "../MathEx/MathEx.h"
#include "MulticopterSetup.h"
#include "Quadcopter.h"

void Quadcopter::arm()
{
  this->motors[FRONT_MOTOR].attach(this->pins[FRONT_MOTOR]);
  this->motors[FRONT_MOTOR].write(0);

  this->motors[REAR_MOTOR].attach(this->pins[REAR_MOTOR]);
  this->motors[REAR_MOTOR].write(0);

  this->motors[RIGHT_MOTOR].attach(this->pins[RIGHT_MOTOR]);
  this->motors[RIGHT_MOTOR].write(0);

  this->motors[LEFT_MOTOR].attach(this->pins[LEFT_MOTOR]);
  this->motors[LEFT_MOTOR].write(0);
}

void Quadcopter::update(float throttle, uint16_t rollPID, uint16_t pitchPID, uint16_t yawPID)
{
  uint16_t newThrottle = this->getThrottleInMicroSec(throttle);

  int32_t motorsThrottle[MOTORS_COUNT];
  motorsThrottle[FRONT_MOTOR] = newThrottle + pitchPID - yawPID;
  motorsThrottle[REAR_MOTOR]  = newThrottle - pitchPID - yawPID;
  motorsThrottle[RIGHT_MOTOR] = newThrottle + rollPID + yawPID;
  motorsThrottle[LEFT_MOTOR]  = newThrottle - rollPID + yawPID;

  int32_t highestThrottle = MathEx::arrayMax(motorsThrottle, MOTORS_COUNT);
  int32_t throttleOutpass = MathEx::max(0, (int32_t)(highestThrottle - this->maxMotorMicroSec));

  motorsThrottle[FRONT_MOTOR] -= throttleOutpass;
  motorsThrottle[REAR_MOTOR]  -= throttleOutpass;
  motorsThrottle[RIGHT_MOTOR] -= throttleOutpass;
  motorsThrottle[LEFT_MOTOR]  -= throttleOutpass;

  this->motors[FRONT_MOTOR].writeMicroseconds(motorsThrottle[FRONT_MOTOR]);
  this->motors[REAR_MOTOR].writeMicroseconds(motorsThrottle[REAR_MOTOR]);
  this->motors[RIGHT_MOTOR].writeMicroseconds(motorsThrottle[RIGHT_MOTOR]);
  this->motors[LEFT_MOTOR].writeMicroseconds(motorsThrottle[LEFT_MOTOR]);
}


uint16_t Quadcopter::getThrottleInMicroSec(float throttle)
{
	throttle = MathEx::truncate(throttle, 0.0, 1.0);
	return throttle * (float)(this->maxMotorMicroSec - this->minMotorMicroSec) + this->minMotorMicroSec;
}