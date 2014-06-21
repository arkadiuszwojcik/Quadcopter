#ifndef Process_h
#define Process_h

#include "Arduino.h"

class Process
{
public:
	static uint32_t micros()
	{
		return ::micros();
	}
	
	static void delay(uint32_t ms)
	{
		::delay(ms);
	}
};

#endif
