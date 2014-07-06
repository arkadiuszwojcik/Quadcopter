#ifndef Quadcopter_h
#define Quadcopter_h

#define MOTORS_COUNT 4
#define FRONT_MOTOR  0
#define REAR_MOTOR   1
#define RIGHT_MOTOR  2
#define LEFT_MOTOR   3

class Quadcopter : public MulticopterSetup
{
 public:
  Quadcopter(uint8_t frontMotorPin, uint8_t rearMotorPin, uint8_t rightMotorPin, uint8_t leftMotorPin)
  {
	this->pins[FRONT_MOTOR] = frontMotorPin;
	this->pins[REAR_MOTOR]  = rearMotorPin;
	this->pins[RIGHT_MOTOR] = rightMotorPin;
	this->pins[LEFT_MOTOR]  = leftMotorPin;
  }

  void arm();
  void update(float throttle, uint16_t rollPID, uint16_t pitchPID, uint16_t yawPID);

  void setMotorThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec)
  {
    this->minMotorMicroSec = minMicroSec;
    this->maxMotorMicroSec = maxMicroSec;
  }

 private:
  uint16_t getThrottleInMicroSec(float throttle);

 private:
  Servo motors[MOTORS_COUNT];
  uint8_t pins[MOTORS_COUNT];
  uint16_t minMotorMicroSec;
  uint16_t maxMotorMicroSec;
};


#endif
