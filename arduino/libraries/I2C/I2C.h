#ifndef I2C_h
#define I2C_h

#include <inttypes.h>

class I2C
{
public:
    I2C();
    uint8_t scanBus(uint8_t nBytes, uint8_t buff[]);
	
    void write(uint8_t devAddr, uint8_t writeAddr, uint8_t val);
    void read(uint8_t devAddr, uint8_t readAddr, uint8_t nBytes, uint8_t buff[]);
};

#endif
