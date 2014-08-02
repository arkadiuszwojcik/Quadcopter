#ifndef Stopwatch_h
#define Stopwatch_h

class Stopwatch
{
public:
  Stopwatch(float miliSec)
  : miliSec(miliSec)
  {}

  void update()
  {
    this->currMS = Process::millis();
    this->diff = (float)(this->currMS - this->lastMS);
  }

  bool elapsed()
  {
    return this->diff >= miliSec;
  }

  float getDiff()
  {
    return this->diff;
  }

  void reset()
  {
    this->diff = 0;
    this->lastMS = this->currMS;
  }

private:
  float miliSec;
  float diff;
  uint32_t currMS;
  uint32_t lastMS;
};

#endif