#ifndef PID_h
#define PID_h

class PID
{
public:
	PID(float* input, float* output, float* setpoint,
		float kp, float ki, float kd);
		
	void setTunings(float kp, float ki, float kd);
	void update(float dt);
	void setOutputLimits(float min, float max);
	
private:
	float kp, ki, kd;
	float outMin, outMax;
	
	float* input;
	float* output;
	float* setpoint;
	
	float iAccumulated;
	float lastInput;
};

#endif