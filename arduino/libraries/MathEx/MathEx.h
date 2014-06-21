#ifndef MathEx_h
#define MathEx_h

#include <Math.h>

/**
* Fast inverse square root implementation. Compatible both for 32 and 8 bit microcontrollers.
* @see http://en.wikipedia.org/wiki/Fast_inverse_square_root
*/
class MathEx
{
public:
	static float fastInvSqrt(float number) 
	{
		union {
			float f;
			int32_t i;
		} y;

		y.f = number;
		y.i = 0x5f375a86 - (y.i >> 1);
		y.f = y.f * ( 1.5f - ( number * 0.5f * y.f * y.f ) );
		return y.f;
	}
};

#endif