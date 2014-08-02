#include <inttypes.h>
#include "..\Wire\Wire.h"
#include "I2C.h"

void I2C::init()
{
  Wire.begin();
}

uint8_t I2C::scanBus(uint8_t nBytes, uint8_t buff[])
{
  uint8_t address, error;
  uint8_t nDevices = 0;
  
  for (address = 1; address < 127; address++)
  {
	Wire.beginTransmission(address);
	error = Wire.endTransmission();
	
	if (error == 0)
	{
	  if (nDevices < nBytes) buff[nDevices] = address;
	  nDevices++;
	}
  }
  
  return nDevices;
}

void I2C::write(uint8_t devAddr, uint8_t writeAddr, uint8_t val) 
{
  Wire.beginTransmission(devAddr);
  Wire.write(writeAddr);
  Wire.write(val);
  Wire.endTransmission();
}

void I2C::read(uint8_t devAddr, uint8_t readAddr, uint8_t nBytes, uint8_t buff[]) 
{
  Wire.beginTransmission(devAddr); 
  Wire.write(readAddr);
  Wire.endTransmission();
  
  Wire.beginTransmission(devAddr); 
  Wire.requestFrom(devAddr, nBytes);
  
  uint8_t i = 0; 
  while (Wire.available()) 
  {
    buff[i] = Wire.read();
    i++;
  }
  
  Wire.endTransmission();
}
