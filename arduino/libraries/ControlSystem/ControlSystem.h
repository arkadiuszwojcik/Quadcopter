#ifndef ControlSystem_h
#define ControlSystem_h

#define CASCADE_CONTROL

class ControlSystem
{
 public:
  ControlSystem(ControlSystemData& data)
   : data(data),
   
#ifdef CASCADE_CONTROL
   yawController(&yawAngle, &gyroYawRate, &desiredYawAngle, &outYawPid),
   rollController(&rollAngle, &gyroRollRate, &desiredRollAngle, &outRollPid),
   pitchController(&pitchAngle, &gyroPitchRate, &desiredPitchAngle, &outPitchPid)
#elif
   yawController(&yawAngle, &outYawPid, &desiredYawAngle, 0, 0, 0, 0),
   rollController(&rollAngle, &outRollPid, &desiredRollAngle, 0, 0, 0, 0),
   pitchController(&pitchAngle, &outPitchPid, &desiredPitchAngle, 0, 0, 0, 0)
#endif

  {
    this->desiredYawAngle = 0;
    this->desiredPitchAngle = 0;
    this->desiredRollAngle = 0;

    this->yawAngle = 0;
    this->pitchAngle = 0;
    this->rollAngle = 0;
    
#ifdef CASCADE_CONTROL
    this->gyroRollRate = 0;
    this->gyroPitchRate = 0;
    this->gyroYawRate = 0;
#endif
    
    this->outYawPid = 0;
    this->outRollPid = 0;
    this->outPitchPid = 0;
  }
  
  void update(float dt, float roll, float pitch, float yaw,
              float gyroRollRate, float gyroPitchRate, float gyroYawRate,
              float* outYawPid, float* outRollPid, float* outPitchPid);
  
 private:
 
#ifdef CASCADE_CONTROL
  AxisCascadeControl yawController;
  AxisCascadeControl rollController;
  AxisCascadeControl pitchController;
#elif
  PID yawController;
  PID pitchController;
  PID rollController;
#endif

  ControlSystemData& data;

  float yawAngle;
  float pitchAngle;
  float rollAngle;

#ifdef CASCADE_CONTROL
  float gyroRollRate;
  float gyroPitchRate;
  float gyroYawRate;
#endif
  
  float desiredYawAngle;
  float desiredPitchAngle;
  float desiredRollAngle;

  float outYawPid;
  float outRollPid;
  float outPitchPid;
};

#endif
