#ifndef ITG3200Gyroscope_h
#define ITG3200Gyroscope_h

#include "../I2C/I2C.h"
#include "../Sensors/Sensors.h"
#include "../ITG3200/ITG3200.h"

class ITG3200Gyroscope : public IGyroscope
{
public:
	ITG3200Gyroscope(I2C& i2c)
		: itg3200(i2c)
	{
		this->offsets[0] = 0;
		this->offsets[1] = 0;
		this->offsets[2] = 0;
	}
	
	void init()
	{
		this->itg3200.init();
		Process::delay(ITG3200_START_UP_DELAY);
	}
	
	void readGyroscopeSI(float& x, float& y, float& z)
	{
		float tmpX, tmpY, tmpZ;
		
		this->itg3200.readGyroscopeSI(&tmpX, &tmpY, &tmpZ);
		
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
	ITG3200 itg3200;
	float offsets[3];
};

#endif