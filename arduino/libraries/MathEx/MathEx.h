#ifndef MathEx_h
#define MathEx_h

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

	static float truncate(float val, float min, float max)
	{
	  if (val > max) return max;
      if (val < min) return min;
      return val;
	}

	static int32_t maxEx(int32_t a, int32_t b)
	{
	  if (a > b) return a;
	  return b;
	}

	static int32_t arrayMax(int32_t* table, int32_t len)
	{
	  //assert(len > 0);
	  int32_t max = table[0];

	  for (int i = 1; i < len; i++)
		{
		  if (table[i] > max) max = table[i];
		}

	  return max;
	}

	static int32_t minEx(int32_t* table, int32_t len)
	{
	  //assert(len > 0);
	  int32_t min = table[0];

	  for (int i = 1; i < len; i++)
		{
		  if (table[i] < min) min = table[i];
		}

	  return min;
	}

	static void rotateZ45(float* x, float* y, float* z)
	{
		float p = 0.70710678;
		float tmpX = p* (*x) - p* (*y);
		float tmpY = p* (*x) + p* (*y);
		float tmpZ = *z;
		*x = tmpX;
		*y = tmpY;
		*z = tmpZ;
	}
};

#endif
