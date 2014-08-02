#ifndef ControlSystem_h
#define ControlSystem_h

class ControlSystem
{
 public:
  ControlSystem(ControlSystemData& data)
   : data(data),
   //rollController(&rollAngle, &gyroRollRate, &desiredRollAngle, &rollPid),
   //pitchController(&pitchAngle, &gyroPitchRate, &desiredPitchAngle, &pitchPid)
   yawController(&yawAngle, &yawPid, &desiredYawAngle, 0, 0, 0),
   rollController(&rollAngle, &rollPid, &desiredRollAngle, 0, 0, 0),
   pitchController(&pitchAngle, &pitchPid, &desiredPitchAngle, 0, 0, 0)
  {
    this->desiredYawAngle = 0;
    this->desiredPitchAngle = 0;
    this->desiredRollAngle = 0;

    this->yawAngle = 0;
    this->pitchAngle = 0;
    this->rollAngle = 0;
    //this->gyroRollRate = 0;
    //this->gyroPitchRate = 0;
    this->yawPid = 0;
    this->rollPid = 0;
    this->pitchPid = 0;
  }

  void update(float dt, float roll, float pitch, float yaw, float* yawPid, float* rollPid, float* pitchPid);
  void debug(float* outRollAngle, float* outPitchAngle, float* outYawAngle)
  {
    *outRollAngle = this->rollAngle;
    *outPitchAngle = this->pitchAngle;
    *outYawAngle = this->yawAngle;
  }

 private:
  //AxisCascadeControl rollController;
  //AxisCascadeControl pitchController;
  PID yawController;
  PID pitchController;
  PID rollController;

  ControlSystemData& data;

  float yawAngle;
  float pitchAngle;
  float rollAngle;

  //float gyroRollRate;
  //float gyroPitchRate;
  
  float desiredYawAngle;
  float desiredPitchAngle;
  float desiredRollAngle;

  float yawPid;
  float rollPid;
  float pitchPid;
};

#endif
