#ifndef PID_h
#define PID_h

typedef scalar_t float;

class PID
{
public:
	PID(scalar_t* input, scalar_t* output, scalar_t* setpoint,
		scalar_t kp, scalar_t ki, scalar_t kd);
		
	void setTunings(scalar_t kp, scalar_t ki, scalar_t kd);
	void update(scalar_t dt);
	void setOutputLimits(scalar_t min, scalar_t max);
	
private:
	scalar_t kp, ki, kd;
	scalar_t outMin, outMax;
	
	scalar_t* input;
	scalar_t* output;
	scalar_t* setpoint;
	
	scalar_t iAccumulated;
	scalar_t lastInput;
};

#endif