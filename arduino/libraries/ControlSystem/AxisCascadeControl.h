#ifndef AxisCascadeControl_h
#define AxisCascadeControl_h

class AxisCascadeControl
{
 public:
  AxisCascadeControl(float* actualAngle, float* actualGyroRate, float* desiredAngle, float* outPid)
    : stabilisePID(actualAngle, &this->desiredGyroRate, desiredAngle, 0, 0, 0, 0),
      ratePID(actualGyroRate, outPid, &this->desiredGyroRate,0 ,0, 0, 0)
      {
        this->stabilisePID.setOutputLimits(-8.0, 8.0);
      }

  inline void update(float dt)
  {
    this->stabilisePID.update(dt);
    this->ratePID.update(dt);
  }
  
  inline void setStabiliseTunings(float p, float i, float d, float maxI)
  {
    this->stabilisePID.setTunings(p, i, d, maxI);
  }
  
  inline void setRateTunings(float p, float i, float d, float maxI)
  {
    this->ratePID.setTunings(p, i, d, maxI);
  }
  
  inline void setOutputLimits(float min, float max)
  {
    this->ratePID.setOutputLimits(min, max);
  }

 private:
  // stabilise PID - outer loop
  PID stabilisePID;
  // rate PID - inner loop
  PID ratePID;
  float desiredGyroRate;
};

#endif
