#include <inttypes.h>
#include "../I2C/I2C.h"
#include "ITG3200.h"

ITG3200::ITG3200(I2C& i2c) : 
	m_i2c(i2c)
{}

void ITG3200::init()
{
  init(RANGE2000, F256HZ);
}

void ITG3200::init(RANGE range, FILTER filter)
{
  setRange(range);
  setFilter(filter);
}

void ITG3200::setRange(RANGE range)
{
  writeRegister(ITG3200_REG_DLPF_FS, range << 3, ITG3200_BMSK_FS_SEL);
}

void ITG3200::setFilter(FILTER filter)
{
  writeRegister(ITG3200_REG_DLPF_FS, filter, ITG3200_BMSK_DLPF_CFG);
}

void ITG3200::readGyroscopeRaw(int16_t *gyroX, int16_t *gyroY, int16_t *gyroZ)
{
  uint8_t buff[6];
  m_i2c.read(ITG3200_I2C_ADDRESS, ITG3200_REG_GYRO_XOUT, 6, buff);
  
  *gyroX = buff[0] << 8 | buff[1];
  *gyroY = buff[2] << 8 | buff[3]; 
  *gyroZ = buff[4] << 8 | buff[5];
}

void ITG3200::readGyroscopeSI(float *gyroX, float *gyroY, float *gyroZ)
{
  int16_t x, y, z;
  readGyroscopeRaw(&x, &y, &z);
  
  *gyroX = (float)x / ITG3200_SENS_SCALE_FACT_RAD_S;
  *gyroY = (float)y / ITG3200_SENS_SCALE_FACT_RAD_S;
  *gyroZ = (float)z / ITG3200_SENS_SCALE_FACT_RAD_S;
}

/*
void ITG3200::zeroCalibrate(uint16_t totSamples, uint16_t sampleDelayMS)
{
  float tmpOffsets[] = {0,0,0};
  int16_t xyz[3];

  for (int i = 0; i < totSamples; i++)
  {
    delay(sampleDelayMS);
    readGyroRaw(xyz);
    tmpOffsets[0] += xyz[0];
    tmpOffsets[1] += xyz[1];
    tmpOffsets[2] += xyz[2];
  }
  
  setOffsets(-tmpOffsets[0] / totSamples + 0.5, -tmpOffsets[1] / totSamples + 0.5, -tmpOffsets[2] / totSamples + 0.5);
}
*/

uint8_t ITG3200::readRegister(uint8_t regAddr)
{
  uint8_t val;
  m_i2c.read(ITG3200_I2C_ADDRESS, regAddr, 1, &val);
  return val;
}

void ITG3200::writeRegister(uint8_t regAddr, uint8_t val, uint8_t preserveMask)
{
  uint8_t preserve = readRegister(regAddr);
  uint8_t orgval = preserve & preserveMask;
  m_i2c.write(ITG3200_I2C_ADDRESS, regAddr, orgval | val);
}