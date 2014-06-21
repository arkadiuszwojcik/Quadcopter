#ifndef HMC5883L_h
#define HMC5883L_h

/* I2C */
#define HMC5883L_I2C_ADDRESS    0x1E

/* Registers */
#define HMC5883L_REG_CFG_A   0x00
#define HMC5883L_REG_CFG_B   0x01
#define HMC5883L_REG_MODE    0x02
#define HMC5883L_REG_DATA    0x03

/* Bitmasks */
#define HMC5883L_BMSK_SCALE  0x1F	// 00011111
#define HMC5883L_BMSK_FILTER 0xE3   // 11100011

#define HMC5883L_START_UP_DELAY   67

class HMC5883L
{
public:
	typedef enum { Continuous=0, Single=1, Idle=3 } MODE;
	typedef enum { Ga088=0, Ga13=1, Ga19=2, Ga25=3, Ga40=4, Ga47=5, Ga56=6, Ga81=7 } SCALE;
	typedef enum { F075Hz=0, F1_5Hz=1, F3Hz=2, F7_5Hz=3, F15Hz=4, F30Hz=5, F75Hz=6 } FILTER;
	
public:
	HMC5883L(I2C& i2c);
	
	void setScale(SCALE scale);
	void setMeasurementMode(MODE mode);
	void setFilter(FILTER filter);
	
	void readMagnetometerRaw(int16_t *magX, int16_t *magY, int16_t *magZ);
	void readMagnetometerSI(float *magX, float *magY, float *magZ);
	
protected:
	uint8_t readRegister(uint8_t regAddr);
	void writeRegister(uint8_t regAddr, uint8_t val, uint8_t preserveMask);
	
	// mG/LSb
	float getScaleSI();
	
private:
    I2C& m_i2c;	
	SCALE m_scale;
};

#endif