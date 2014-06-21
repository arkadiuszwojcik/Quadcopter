#ifndef BMA180_h
#define BMA180_h

/* I2C */
#define BMA180_I2C_ADDRESS  0x40

/* Registers */
#define BMA180_REG_RANGE    0x35
#define BMA180_REG_BW       0x20
#define BMA180_REG_ACC      0x02

/* Bitmasks */
#define BMA180_BMSK_RANGE   0xF1	// 11110001
#define BMA180_BMSK_BW      0x0F	// 00001111

class BMA180
{
public:
  typedef enum { F10HZ=0,F20HZ=1,F40HZ,F75HZ,F150HZ,F300HZ,F600HZ,F1200HZ,HIGHPASS,BANDPASS } FILTER;
  typedef enum { G1=0,G15=1,G2,G3,G4,G8,G16 } GSENSITIVITY;
  
public:
  BMA180(I2C& i2c);
  
  void setGSensitivity(GSENSITIVITY maxg);
  void setFilter(FILTER f);
  
  void readAccelerationSI(float *accX, float *accY, float *accZ);
  void readAccelerationRaw(int16_t *accX, int16_t *accY, int16_t *accZ);
  
protected:
  uint8_t readRegister(uint8_t regAddr);
  void writeRegister(uint8_t regAddr, uint8_t val, uint8_t preserveMask);
  
  float getGSensitivitySI();
  
private:
  I2C& m_i2c;
  GSENSITIVITY m_gSensitivity;
};

#endif