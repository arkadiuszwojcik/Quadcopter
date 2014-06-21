#ifndef StorageEEPROM_h
#define StorageEEPROM_h

#include <EEPROM.h>

class StorageEEPROM
{
public:
	StorageEEPROM(uint16_t maxBytes)
	: m_maxBytes(maxBytes)
	{ }
	
	uint16_t getMaxBytes()
	{
		return m_maxBytes;
	}
	
	void read(uint16_t address, uint16_t size, void* outData)
	{
		uint8_t* p = (uint8_t*)outData;
		for (uint16_t i=0; i<size; i++)
		{
			*p = EEPROM.read(address + i);
			p++;
		}
	}
	
	void write(uint16_t address, uint16_t size, const void* inData)
	{
		const uint8_t* p = (const uint8_t*)inData;
		for (uint16_t i=0; i<size; i++)
		{
			EEPROM.write(address + i, *p);
			p++;
		}
	}
	
	void format()
	{
		for (uint16_t i=0; i<m_maxBytes; i++) EEPROM.write(i, 0);
	}
	
private:
	uint16_t m_maxBytes;
};

#endif