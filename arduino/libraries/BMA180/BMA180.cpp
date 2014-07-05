#include <inttypes.h>
#include "../I2C/I2C.h"
#include "BMA180.h"

BMA180::BMA180(I2C& i2c) : 
	m_i2c(i2c),
	m_gSensitivity(G2)
{}

uint8_t BMA180::readRegister(uint8_t regAddr)
{
  uint8_t val;
  m_i2c.read(BMA180_I2C_ADDRESS, regAddr, 1, &val);
  return val;
}

void BMA180::writeRegister(uint8_t regAddr, uint8_t val, uint8_t preserveMask)
{
  uint8_t preserve = readRegister(regAddr);
  uint8_t orgval = preserve & preserveMask;
  m_i2c.write(BMA180_I2C_ADDRESS, regAddr, orgval | val);
}

void BMA180::readAccelerationRaw(int16_t *accX, int16_t *accY, int16_t *accZ)
{
  uint8_t buff[6];
  m_i2c.read(BMA180_I2C_ADDRESS, BMA180_REG_ACC, 6, buff);
  
  int16_t x = (buff[1] << 6) + (buff[0] >> 2);
  int16_t y = (buff[3] << 6) + (buff[2] >> 2);
  int16_t z = (buff[5] << 6) + (buff[4] >> 2);
  
  // set U2 complement 
  if (x & 0x2000) x |= 0xc000;
  if (y & 0x2000) y |= 0xc000;
  if (z & 0x2000) z |= 0xc000;

  *accX = x; *accY = y; *accZ = z;
}

void BMA180::setGSensitivity(GSENSITIVITY maxg)
{
  writeRegister(BMA180_REG_RANGE, maxg << 1, BMA180_BMSK_RANGE);
  m_gSensitivity = maxg;
}

void BMA180::setFilter(FILTER f)
{
  writeRegister(BMA180_REG_BW, f << 4, BMA180_BMSK_BW);
}

float BMA180::getGSensitivitySI()
{
  switch(m_gSensitivity)
    {
        case G1: return 1.0;
        case G15: return 1.5;
        case G2: return 2.0;
        case G3: return 3.0;
        case G4: return 4.0;
        case G8: return 8.0;
        case G16: return 16.0;
    }
}

void BMA180::readAccelerationSI(float *accX, float *accY, float *accZ)
{
  float gSens = getGSensitivitySI();
	
  int16_t x,y,z;
	
  readAccelerationRaw(&x, &y, &z);
	
  *accX = (float)x/8191.0 * gSens;
  *accY = (float)y/8191.0 * gSens;
  *accZ = (float)z/8191.0 * gSens;
}
