#include <inttypes.h>
#include "../Storage/Storage.h"
#include "../Sensors/Sensors.h"
#include "../PID/PID.h"
#include "AxisCascadeControl.h"
#include "ControlSystem.h"

void ControlSystem::update(float dt, float roll, float pitch, float yaw, float* yawPid, float* rollPid, float* pitchPid)
{
  this->yawController.setTunings(this->data.yawPID[0], this->data.yawPID[1], this->data.yawPID[2]);
  this->rollController.setTunings(this->data.rollPID[0], this->data.rollPID[1], this->data.rollPID[2]);
  this->pitchController.setTunings(this->data.pitchPID[0], this->data.pitchPID[1], this->data.pitchPID[2]);

  this->yawController.setOutputLimits(this->data.minPidValue, this->data.maxPidValue);
  this->rollController.setOutputLimits(this->data.minPidValue, this->data.maxPidValue);
  this->pitchController.setOutputLimits(this->data.minPidValue, this->data.maxPidValue);

  this->yawAngle = yaw;
  this->pitchAngle = pitch;
  this->rollAngle = roll;

  //if (this->data.isYawControlEnabled) this->yawController.update(dt);
  //if (this->data.isRollControlEnabled) this->rollController.update(dt);
  //if (this->data.isPitchControlEnabled) this->pitchController.update(dt);

  this->yawController.update(dt);
  this->rollController.update(dt);
  this->pitchController.update(dt);

  //*yawPid = this->yawPid;
  *yawPid = 0;
  *rollPid = this->rollPid;
  *pitchPid = 0;
}


