#include <inttypes.h>
#include "../MathEx/MathEx.h"
#include "../Sensors/Sensors.h"
#include "../Process/Process.h"
#include "IMU.h"

FreeIMU::FreeIMU(IAccelerometer& acc, IGyroscope& gyro, IMagnetometer& magn)
	: accelerometer(acc)
	, gyroscope(gyro)
	, magnetometer(magn)
{
	// initialize quaternion
	q0 = 1.0f;
	q1 = 0.0f;
	q2 = 0.0f;
	q3 = 0.0f;
	exInt = 0.0;
	eyInt = 0.0;
	ezInt = 0.0;
	twoKp = twoKpDef;
	twoKi = twoKiDef;
	integralFBx = 0.0f,  integralFBy = 0.0f, integralFBz = 0.0f;
	lastUpdate = now = 0;
}

void FreeIMU::reset()
{
	lastUpdate = Process::micros();
}

void FreeIMU::getEuler(float * angles)
{
	this->getEulerRad(angles);
	angles[0] *= 180/M_PI;
	angles[1] *= 180/M_PI; 
	angles[2] *= 180/M_PI; 
}

void FreeIMU::getYawPitchRoll(float * ypr)
{
	this->getYawPitchRollRad(ypr);
	ypr[0] *= 180/M_PI;
	ypr[1] *= 180/M_PI; 
	ypr[2] *= 180/M_PI; 
}

void FreeIMU::getEulerRad(float * angles)
{
	float q[4]; // quaternion
	this->getQuaternion(q);
	
	angles[0] = atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0]*q[0] + 2 * q[1] * q[1] - 1); // psi
	angles[1] = -asin(2 * q[1] * q[3] + 2 * q[0] * q[2]); // theta
	angles[2] = atan2(2 * q[2] * q[3] - 2 * q[0] * q[1], 2 * q[0] * q[0] + 2 * q[3] * q[3] - 1); // phi
}

void FreeIMU::getYawPitchRollRad(float * ypr)
{
	float q[4]; // quaternion
	float gx, gy, gz; // estimated gravity direction
	this->getQuaternion(q);

	gx = 2 * (q[1]*q[3] - q[0]*q[2]);
	gy = 2 * (q[0]*q[1] + q[2]*q[3]);
	gz = q[0]*q[0] - q[1]*q[1] - q[2]*q[2] + q[3]*q[3];

	ypr[0] = atan2(2 * q[1] * q[2] - 2 * q[0] * q[3], 2 * q[0]*q[0] + 2 * q[1] * q[1] - 1);
	ypr[1] = atan(gx / sqrt(gy*gy + gz*gz));
	ypr[2] = atan(gy / sqrt(gx*gx + gz*gz));
}

void FreeIMU::getQuaternion(float* q)
{
	float val[9];
	this->getSensorsValues(val);
	
	this->now = Process::micros();
	this->sampleFreq = 1.0 / ((this->now - this->lastUpdate) / 1000000.0);
	this->lastUpdate = this->now;
	
	this->updateAHRS(val[3], val[4], val[5], val[0], val[1], val[2], val[6], val[7], val[8]);
	
	q[0] = q0;
	q[1] = q1;
	q[2] = q2;
	q[3] = q3;
}

void FreeIMU::getSensorsValues(float * values)
{
	this->accelerometer.readAccelerationSI(values[0], values[1], values[2]);
	this->gyroscope.readGyroscopeSI(values[3], values[4], values[5]);
	this->magnetometer.readMagnetometerSI(values[6], values[7], values[8]);
}

void FreeIMU::updateAHRS(float gx, float gy, float gz, float ax, float ay, float az, float mx, float my, float mz)
{
	float recipNorm;
	float q0q0, q0q1, q0q2, q0q3, q1q1, q1q2, q1q3, q2q2, q2q3, q3q3;
	float halfex = 0.0f, halfey = 0.0f, halfez = 0.0f;
	float qa, qb, qc;
	
	// Auxiliary variables to avoid repeated arithmetic
	q0q0 = q0 * q0;
	q0q1 = q0 * q1;
	q0q2 = q0 * q2;
	q0q3 = q0 * q3;
	q1q1 = q1 * q1;
	q1q2 = q1 * q2;
	q1q3 = q1 * q3;
	q2q2 = q2 * q2;
	q2q3 = q2 * q3;
	q3q3 = q3 * q3;
	
	if((mx != 0.0f) && (my != 0.0f) && (mz != 0.0f)) {
		float hx, hy, bx, bz;
		float halfwx, halfwy, halfwz;
		
		// Normalise magnetometer measurement
		recipNorm = MathEx::fastInvSqrt(mx * mx + my * my + mz * mz);
		mx *= recipNorm;
		my *= recipNorm;
		mz *= recipNorm;
		
		// Reference direction of Earth's magnetic field
		hx = 2.0f * (mx * (0.5f - q2q2 - q3q3) + my * (q1q2 - q0q3) + mz * (q1q3 + q0q2));
		hy = 2.0f * (mx * (q1q2 + q0q3) + my * (0.5f - q1q1 - q3q3) + mz * (q2q3 - q0q1));
		bx = sqrt(hx * hx + hy * hy);
		bz = 2.0f * (mx * (q1q3 - q0q2) + my * (q2q3 + q0q1) + mz * (0.5f - q1q1 - q2q2));
		
		// Estimated direction of magnetic field
		halfwx = bx * (0.5f - q2q2 - q3q3) + bz * (q1q3 - q0q2);
		halfwy = bx * (q1q2 - q0q3) + bz * (q0q1 + q2q3);
		halfwz = bx * (q0q2 + q1q3) + bz * (0.5f - q1q1 - q2q2);
		
		// Error is sum of cross product between estimated direction and measured direction of field vectors
		halfex = (my * halfwz - mz * halfwy);
		halfey = (mz * halfwx - mx * halfwz);
		halfez = (mx * halfwy - my * halfwx);
	}
	
	// Compute feedback only if accelerometer measurement valid (avoids NaN in accelerometer normalisation)
	if((ax != 0.0f) && (ay != 0.0f) && (az != 0.0f)) {
		float halfvx, halfvy, halfvz;
		
		// Normalise accelerometer measurement
		recipNorm = MathEx::fastInvSqrt(ax * ax + ay * ay + az * az);
		ax *= recipNorm;
		ay *= recipNorm;
		az *= recipNorm;
		
		// Estimated direction of gravity
		halfvx = q1q3 - q0q2;
		halfvy = q0q1 + q2q3;
		halfvz = q0q0 - 0.5f + q3q3;
	  
		// Error is sum of cross product between estimated direction and measured direction of field vectors
		halfex += (ay * halfvz - az * halfvy);
		halfey += (az * halfvx - ax * halfvz);
		halfez += (ax * halfvy - ay * halfvx);
	}
	
	// Apply feedback only when valid data has been gathered from the accelerometer or magnetometer
    if(halfex != 0.0f && halfey != 0.0f && halfez != 0.0f) {
		// Compute and apply integral feedback if enabled
		if(twoKi > 0.0f) {
		  integralFBx += twoKi * halfex * (1.0f / sampleFreq);  // integral error scaled by Ki
		  integralFBy += twoKi * halfey * (1.0f / sampleFreq);
		  integralFBz += twoKi * halfez * (1.0f / sampleFreq);
		  gx += integralFBx;  // apply integral feedback
		  gy += integralFBy;
		  gz += integralFBz;
		}
		else {
		  integralFBx = 0.0f; // prevent integral windup
		  integralFBy = 0.0f;
		  integralFBz = 0.0f;
		}

		// Apply proportional feedback
		gx += twoKp * halfex;
		gy += twoKp * halfey;
		gz += twoKp * halfez;
    }
	
	// Integrate rate of change of quaternion
	gx *= (0.5f * (1.0f / sampleFreq));   // pre-multiply common factors
	gy *= (0.5f * (1.0f / sampleFreq));
	gz *= (0.5f * (1.0f / sampleFreq));
	qa = q0;
	qb = q1;
	qc = q2;
	q0 += (-qb * gx - qc * gy - q3 * gz);
	q1 += (qa * gx + qc * gz - q3 * gy);
	q2 += (qa * gy - qb * gz + q3 * gx);
	q3 += (qa * gz + qb * gy - qc * gx);

	// Normalise quaternion
	recipNorm = MathEx::fastInvSqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
	q0 *= recipNorm;
	q1 *= recipNorm;
	q2 *= recipNorm;
	q3 *= recipNorm;
}