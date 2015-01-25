#include <inttypes.h>
#include "../Storage/Storage.h"
#include "../Sensors/Sensors.h"
#include "../PID/PID.h"
#include "AxisCascadeControl.h"
#include "ControlSystem.h"

void ControlSystem::update(
    float dt, float roll, float pitch, float yaw,
    float gyroRollRate, float gyroPitchRate, float gyroYawRate,
    float* outYawPid, float* outRollPid, float* outPitchPid)
{
#ifdef CASCADE_CONTROL
  this->yawController.setStabiliseTunings(this->data.outerYawPID[0], this->data.outerYawPID[1], this->data.outerYawPID[2], this->data.outerYawPID[3]);
  this->rollController.setStabiliseTunings(this->data.outerRollPID[0], this->data.outerRollPID[1], this->data.outerRollPID[2], this->data.outerRollPID[3]);
  this->pitchController.setStabiliseTunings(this->data.outerPitchPID[0], this->data.outerPitchPID[1], this->data.outerPitchPID[2], this->data.outerPitchPID[3]);
  
  this->yawController.setRateTunings(this->data.innerYawPID[0], this->data.innerYawPID[1], this->data.innerYawPID[2], this->data.innerYawPID[3]);
  this->rollController.setRateTunings(this->data.innerRollPID[0], this->data.innerRollPID[1], this->data.innerRollPID[2], this->data.innerRollPID[3]);
  this->pitchController.setRateTunings(this->data.innerPitchPID[0], this->data.innerPitchPID[1], this->data.innerPitchPID[2], this->data.innerPitchPID[3]);
#elif
  this->yawController.setTunings(this->data.outerYawPID[0], this->data.outerYawPID[1], this->data.outerYawPID[2], this->data.outerYawPID[3]);
  this->rollController.setTunings(this->data.outerRollPID[0], this->data.outerRollPID[1], this->data.outerRollPID[2], this->data.outerRollPID[3]);
  this->pitchController.setTunings(this->data.outerPitchPID[0], this->data.outerPitchPID[1], this->data.outerPitchPID[2], this->data.outerPitchPID[3]);
#endif

  this->yawController.setOutputLimits(this->data.minPidValue, this->data.maxPidValue);
  this->rollController.setOutputLimits(this->data.minPidValue, this->data.maxPidValue);
  this->pitchController.setOutputLimits(this->data.minPidValue, this->data.maxPidValue);

  this->yawAngle = yaw;
  this->pitchAngle = pitch;
  this->rollAngle = roll;
  
#ifdef CASCADE_CONTROL
  this->gyroRollRate = gyroRollRate;
  this->gyroPitchRate = gyroPitchRate;
  this->gyroYawRate = gyroYawRate;
#endif

  this->yawController.update(dt);
  this->rollController.update(dt);
  this->pitchController.update(dt);

  *outYawPid = 0;
  *outRollPid = this->outRollPid;
  *outPitchPid = 0;
}


