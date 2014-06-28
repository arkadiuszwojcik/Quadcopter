#include "Multicopter.h"

void Quadcopter::arm()
{
}

void Quadcopter::update(float throttle, uint16_t rollPID, uint16_t pitchPID, uint16_t yawPID)
{
  uint16_t newThrottle = this->getThrottleInMicroSec(throttle);

  int32_t motorsThrottle[MOTORS_COUNT];
  motorsthrottle[FRONT_MOTOR] = newThrottle + pitchPID - yawPID;
  motorsThrottle[REAR_MOTOR]  = newThrottle - pitchPID - yawPID;
  motorsThrottle[RIGHT_MOTOR] = newThrottle + rollPID + yawPID;
  motorsThrottle[LEFT_MOTOR]  = newThrottle - rollPID + yawPID;

  int32_t highestThrottle = MathEx::max(motorsThrottle, MOTORS_COUNT);
  int32_t throttleOutpass = MathEx::max(0, highestThrottle - this->maxMotorMicroSec);

  motorsThrottle[FRONT_MOTOR] -= throttleOutpass;
  motorsThrottle[REAR_MOTOR]  -= throttleOutpass;
  motorsThrottle[RIGHT_MOTOR] -= throttleOutpass;
  motorsThrottle[LEFT_MOTOR]  -= throttleOutpass;

  this->motors[FRONT_MOTOR].writeMicroseconds(motorsThrottle[FRONT_MOTOR]);
  this->motors[REAR_MOTOR].writeMicroseconds(motorsThrottle[REAR_MOTOR]);
  this->motors[RIGHT_MOTOR].writeMicroseconds(motorsThrottle[RIGHT_MOTOR]);
  this->motors[LEFT_MOTOR].writeMicroseconds(motorsThrottle[LEFT_MOTOR]);
}
