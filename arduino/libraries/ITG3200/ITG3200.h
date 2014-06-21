#ifndef ITG3200_h
#define ITG3200_h

/* I2C */
#define ITG3200_I2C_ADDRESS 0x68

/* Registers */
#define ITG3200_REG_WHO_AM_I     0x00  // RW SETUP: I2C address
#define ITG3200_REG_SMPLRT_DIV   0x15  // RW SETUP: Sample Rate Divider
#define ITG3200_REG_DLPF_FS      0x16  // RW SETUP: Digital Low Pass Filter/ Full Scale range
#define ITG3200_REG_INT_CFG      0x17  // RW Interrupt: Configuration
#define ITG3200_REG_INT_STATUS   0x1A  // R	Interrupt: Status
#define ITG3200_REG_TEMP_OUT     0x1B  // R	SENSOR: Temperature 2bytes
#define ITG3200_REG_GYRO_XOUT    0x1D  // R	SENSOR: Gyro X 2bytes  
#define ITG3200_REG_GYRO_YOUT    0x1F  // R	SENSOR: Gyro Y 2bytes
#define ITG3200_REG_GYRO_ZOUT    0x21  // R	SENSOR: Gyro Z 2bytes
#define ITG3200_REG_PWR_MGM      0x3E  // RW Power Management 

/* Bitmasks */
#define ITG3200_BMSK_FS_SEL      0xE7  // 11100111
#define ITG3200_BMSK_DLPF_CFG    0xF8  // 11111000

#define ITG3200_START_UP_DELAY        70             // 50ms from gyro startup + 20ms register r/w startup
#define ITG3200_SENS_SCALE_FACT_DEG_S 14.375
#define ITG3200_SENS_SCALE_FACT_RAD_S 823.62683144


class ITG3200
{
public:
  typedef enum { RANGE2000=3 } RANGE;
  typedef enum { F256HZ=0, F188HZ=1, F98HZ, F42HZ, F20HZ, F10HZ, F5HZ } FILTER;
  
public:
  ITG3200(I2C& i2c);
  
  void init();
  void init(RANGE range, FILTER filter);
  
  void setRange(RANGE range);
  void setFilter(FILTER filter);
  
  void readGyroscopeRaw(int16_t *gyroX, int16_t *gyroY, int16_t *gyroZ);
  void readGyroscopeSI(float *gyroX, float *gyroY, float *gyroZ);
  
private:
  uint8_t readRegister(uint8_t regAddr);
  void writeRegister(uint8_t regAddr, uint8_t val, uint8_t preserveMask);
  
private:
  I2C& m_i2c;
};

#endif