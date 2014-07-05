#ifndef AxisCascadeControl_h
#define AxisCascadeControl_h

class AxisCascadeControl
{
 public:
  AxisCascadeControl(float* actualAngle, float* actualGyroRate, float* desiredAngle, float* motorOutput)
    : stabilisePID(actualAngle, &this->desiredGyroRate, desiredAngle, 0, 0, 0),
      ratePID(actualGyroRate, motorOutput, &this->desiredGyroRate,0 ,0, 0)
      {}

  void update(float dt)
  {
    this->stabiliserPID.update(dt);
    this->ratePID.update(dt);
  }

 private:
  PID stabilisePID;
  PID ratePID;
  float desiredGyroRate;
};

#endif
