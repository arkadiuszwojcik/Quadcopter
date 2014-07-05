AxisCascadeControl::AxisCascadeControl(float* actualAngle, float* actualGyroRate, float* desiredAngle, float* motorOutput)
  : stabilisePID(actualAngle, &this->desiredGyroRate, desiredAngle, 0, 0, 0),
    ratePID(actualGyroRate, motorOutput, &this->desiredGyroRate,0 ,0, 0)
{
}

AxisCascadeControl::update(float dt)
{
  this->stabilisePID.update(dt);
  this->ratePID.update(dt);
}


