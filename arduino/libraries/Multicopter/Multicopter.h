#ifndef Multicopter_h
#define Multicopter_h

class Multicopter
{
 public:
  // throttle range 0.0 to 1.0
  void setThrottle(float_t value);
  void setMotorsThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec);

 private:
  float_t throttle;
};

class IMulticopterConfiguration
{
 public:
  virtual void arm()=0;
  virtual void setMotorThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec)=0;
  virtual void update(float_t throttle, uint16_t rollPID, uint16_t pitchPID, uint16_t yawPID)=0;
};

#define MOTORS_COUNT 4
#define FRONT_MOTOR  0
#define REAR_MOTOR   1
#define RIGHT_MOTOR  2
#define LEFT_MOTOR   3

class Quadcopter : public IMulticopterConfiguration
{
 public:
  Quadcopter(uint8_t frontMotorPin, uint8_t rearMotorPin, uint8_t rightMotorPin, uint8_t leftMotorPin);

  void arm();
  void update(float_t throttle, uint16_t rollPID, uint16_t pitchPID, uint16_t yawPID);

  void setMotorThrottleRange(uint16_t minMicroSec, uint16_t maxMicroSec)
  {
    this->minMotorMicroSec = minMicroSec;
    this->maxMotorMicroSec = maxMicroSec;
  }

 private:
  uint16_t getThrottleInMicroSec(float throttle)
  {
    throttle = MathEx::truncate(throttle, 0.0, 1.0);
    return throttle * (float)(this->maxMotorMicro - this->minMotorMicro) + this->minMotorMicroSec;
  }

 private:
  Servo motors[MOTORS_COUNT];
  uint8_t pins[MOTORS_COUNT];
  uint16_t minMotorMicroSec;
  uint16_t maxMotorMicroSec;
};

#endif
