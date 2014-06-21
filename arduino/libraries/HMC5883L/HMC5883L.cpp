#include <inttypes.h>
#include "../I2C/I2C.h"
#include "HMC5883L.h"

HMC5883L::HMC5883L(I2C& i2c) :
	m_i2c(i2c),
	m_scale(Ga13)
{}

uint8_t HMC5883L::readRegister(uint8_t regAddr)
{
  uint8_t val;
  m_i2c.read(HMC5883L_I2C_ADDRESS, regAddr, 1, &val);
  return val;
}

void HMC5883L::writeRegister(uint8_t regAddr, uint8_t val, uint8_t preserveMask)
{
  uint8_t preserve = readRegister(regAddr);
  uint8_t orgval = preserve & preserveMask;
  m_i2c.write(HMC5883L_I2C_ADDRESS, regAddr, orgval | val);
}

void HMC5883L::setScale(SCALE scale)
{
  writeRegister(HMC5883L_REG_CFG_B, scale << 5, HMC5883L_BMSK_SCALE);
  m_scale = scale;
}

void HMC5883L::setMeasurementMode(MODE mode)
{
  writeRegister(HMC5883L_REG_MODE, (uint8_t)mode, 0);
}

void HMC5883L::setFilter(FILTER filter)
{
  writeRegister(HMC5883L_REG_CFG_A, filter << 2, HMC5883L_BMSK_FILTER);
}

float HMC5883L::getScaleSI()
{
	switch(m_scale)
	{
		case Ga088: return 0.73;
		case Ga13: return 0.92;
		case Ga19: return 1.22;
		case Ga25: return 1.52;
		case Ga40: return 2.27;
		case Ga47: return 2.56;
		case Ga56: return 3.03;
		case Ga81: return 4.35;
	}
}

void HMC5883L::readMagnetometerRaw(int16_t *magX, int16_t *magY, int16_t *magZ)
{
  uint8_t buff[6];
  m_i2c.read(HMC5883L_I2C_ADDRESS, HMC5883L_REG_DATA, 6, buff);
  
  *magX = (buff[0] << 8) | buff[1];
  *magZ = (buff[2] << 8) | buff[3];
  *magY = (buff[4] << 8) | buff[5];
}

void HMC5883L::readMagnetometerSI(float *magX, float *magY, float *magZ)
{
  float scale = getScaleSI();
  int16_t x,y,z;
	
  readMagnetometerRaw(&x, &y, &z);
  
  *magX = x * scale;
  *magY = y * scale;
  *magZ = z * scale;
}