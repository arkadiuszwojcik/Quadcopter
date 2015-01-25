#ifndef PID_h
#define PID_h

class PID
{
public:
	PID(float* input, float* output, float* setpoint,
		float kp, float ki, float kd, float maxI);
		
	void setTunings(float kp, float ki, float kd, float maxI);
	void update(float dt);
	void setOutputLimits(float min, float max);
	
private:
	float kp, ki, kd, maxI;
	float outMin, outMax;
	
	float* input;
	float* output;
	float* setpoint;
	
	float iAccumulated;
	float lastInput;
};

#endif