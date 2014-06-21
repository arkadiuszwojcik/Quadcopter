#ifndef Sensors_h
#define Sensors_h

class IAccelerometer
{
public:
  virtual void init() = 0;
  virtual void setOffsets(float x, float y, float z) = 0;
  virtual void readAccelerationSI(float& x, float& y, float& z) = 0;
};

class IGyroscope
{
public:
  virtual void init() = 0;
  virtual void setOffsets(float x, float y, float z) = 0;
  virtual void readGyroscopeSI(float& x, float& y, float& z) = 0;
};

class IMagnetometer
{
public:
  virtual void init() = 0;
  virtual void setOffsets(float x, float y, float z) = 0;
  virtual void readMagnetometerSI(float& x, float& y, float& z) = 0;
};

class Sensors
{
public:
	Sensors(IAccelerometer* acc, IGyroscope* gyro, IMagnetometer* mag);
	
private:
	IGyroscope* m_gyroscope;
	IMagnetometer* m_magnetometer;
	IAccelerometer* m_accelerometer;
};

#endif