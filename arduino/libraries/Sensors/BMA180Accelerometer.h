#ifndef BMA180Accelerometer_h
#define BMA180Accelerometer_h

#include "../I2C/I2C.h"
#include "../Sensors/Sensors.h"
#include "../BMA180/BMA180.h"

class BMA180Accelerometer : public IAccelerometer
{
public:
	BMA180Accelerometer(I2C& i2c)
		: bma180(i2c)
	{
		this->offsets[0] = 0;
		this->offsets[1] = 0;
		this->offsets[2] = 0;
	}

	void init()
	{}

	void readAccelerationSI(float& x, float& y, float& z)
	{
		float tmpX, tmpY, tmpZ;

		this->bma180.readAccelerationSI(&tmpX, &tmpY, &tmpZ);

		x = tmpX + this->offsets[0];
		y = tmpY + this->offsets[1];
		z = tmpZ + this->offsets[2];
	}

    void setOffsets(float x, float y, float z)
	{
		this->offsets[0] = x;
		this->offsets[1] = y;
		this->offsets[2] = z;
	}

private:
	BMA180 bma180;
	float offsets[3];
};

#endif