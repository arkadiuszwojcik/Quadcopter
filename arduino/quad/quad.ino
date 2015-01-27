#include <Arduino.h>
#include <Wire.h>
#include <Math.h>
#include <Servo.h>
#include <Process.h>
#include <MathEx.h>
#include <I2C.h>
#include <BMA180.h>
#include <ITG3200.h>
#include <HMC5883L.h>
#include <BMA180Accelerometer.h>
#include <ITG3200Gyroscope.h>
#include <HMC5883LMagnetometer.h>
#include <IMU.h>
#include <PID.h>
#include <Storage.h>
#include <MulticopterSetup.h>
#include <Quadcopter.h>
#include <AxisCascadeControl.h>
#include <ControlSystem.h>
#include <Multicopter.h>
#include <SerialCommand.h>
#include <Stopwatch.h>

#define QUADCOPTER_SETUP
#define BMA180_ACCELEROMETER
#define ITG3200_GYROSCOPE
#define HMC5883L_MAGNETOMETER
#define ENABLE_TIMER_POWER_CUT
#define TIMER_POWER_CUT_INTERVAL 60000

I2C i2c;

#ifdef BMA180_ACCELEROMETER
BMA180Accelerometer accelerometer(i2c);
#endif

#ifdef ITG3200_GYROSCOPE
ITG3200Gyroscope gyroscope(i2c);
#endif

#ifdef HMC5883L_MAGNETOMETER
HMC5883LMagnetometer magnetometer(i2c);
#endif

FreeIMU imu(accelerometer, gyroscope, magnetometer);

#ifdef ENABLE_TIMER_POWER_CUT
Stopwatch power_cut_timer(TIMER_POWER_CUT_INTERVAL);
#endif

#ifdef QUADCOPTER_SETUP
Quadcopter frameSetup(4,5,7,9);
#endif

SerialCommand cmd;

Storage storage;

Multicopter multicopter(frameSetup, storage, imu);

Stopwatch stopwatch(500);
volatile uint32_t lastMicros;
volatile bool armed;

void setup()
{
  Serial.begin(9600);
  armed = false;
  
  i2c.init();
  gyroscope.init();
  magnetometer.init();
  accelerometer.init();
  storage.load();
  
  cmd.addCommand("ARM", arming);
  cmd.addCommand("DARM", disarm);
  cmd.addCommand("TH", throttle);
  cmd.addCommand("SF", cmdSetFloat);
  cmd.addCommand("SS", cmdSetShort);
  cmd.addCommand("GF", cmdGetFloat);
  cmd.addCommand("GS", cmdGetShort);
  cmd.addCommand("SAVE", save);
  //cmd.addCommand("SETPID", setPID);
  //cmd.addCommand("SETMTR", setMotor);
  //cmd.addCommand("PIDLIM", setPidLimit);
  //cmd.addCommand("GET", get);
  cmd.addDefaultHandler(unrecognized);
}

void arming()
{
#ifdef ENABLE_TIMER_POWER_CUT
  power_cut_timer.reset();
#endif

  multicopter.arm();
  multicopter.setThrottle(0);
  armed = true;
  Serial.println("Armed");
  lastMicros = Process::micros();
  imu.reset();
}

void disarm()
{
  multicopter.disarm();
  armed = false;
  Serial.println("Disarmed");
}

void throttle()
{
  char *arg = cmd.next();
  if (arg != NULL)
  {
    uint32_t number = atoi(arg);
    multicopter.setThrottle(number / 180.0f);
    Serial.println(number / 180.0f);
  }
}

void save()
{
  storage.save();
  Serial.println("Configuration saved");
}

void cmdGetFloat()
{
  uint16_t offset;
  char *arg = cmd.next();
  
  if (arg == NULL) return;
  
  serialSendFloat(atoi(arg));
}

void cmdGetShort()
{
  uint16_t offset;
  char *arg = cmd.next();
  
  if (arg == NULL) return;
  
  serialSendShort(atoi(arg));
}

void serialSendFloat(uint16_t offset)
{
  if (offset + sizeof(float) > sizeof(FPersistentData)) return;
  float value = *((float*)(&((byte*)&storage.getData())[offset]));
  Serial.print("FV "); Serial.print(offset); Serial.print(" "); Serial.println(value, 5);
}

void serialSendShort(uint16_t offset)
{
  if (offset + sizeof(int16_t) > sizeof(FPersistentData)) return;
  int16_t value = *((int16_t*)(&((byte*)&storage.getData())[offset]));
  Serial.print("SV "); Serial.print(offset); Serial.print(" "); Serial.println(value);
}

void cmdSetFloat()
{
  uint16_t offset;
  char *arg = cmd.next();
  
  if (arg == NULL) return;
  
  offset = atoi(arg);
  arg = cmd.next();
  
  if (arg == NULL) return;
  
  setFloat(offset, atof(arg));
}

void cmdSetShort()
{
  uint16_t offset;
  char *arg = cmd.next();
  
  if (arg == NULL) return;
  
  offset = atoi(arg);
  arg = cmd.next();
  
  if (arg == NULL) return;
  
  setShort(offset, atoi(arg));
}

void setFloat(uint16_t offset, float value)
{
  if (offset + sizeof(float) > sizeof(FPersistentData)) return;
  ((float*)(&((byte*)&storage.getData())[offset]))[0] = value; 
}

void setShort(uint16_t offset, int16_t value)
{
  if (offset + sizeof(int16_t) > sizeof(FPersistentData)) return;
  ((int16_t*)(&((byte*)&storage.getData())[offset]))[0] = value; 
}

/*
void get()
{
  ControlSystemData& data = storage.getData().controlSystemData;
  sendPID("A",data.outerRollPID[0], data.outerRollPID[1], data.outerRollPID[2], data.outerRollPID[3]);
  sendPID("B", data.outerPitchPID[0], data.outerPitchPID[1], data.outerPitchPID[2], data.outerPitchPID[3]);
  sendPID("C", data.outerYawPID[0], data.outerYawPID[1], data.outerYawPID[2], data.outerYawPID[3]);
  sendPID("D",data.innerRollPID[0], data.innerRollPID[1], data.innerRollPID[2], data.innerRollPID[3]);
  sendPID("E", data.innerPitchPID[0], data.innerPitchPID[1], data.innerPitchPID[2], data.innerPitchPID[3]);
  sendPID("F", data.innerYawPID[0], data.innerYawPID[1], data.innerYawPID[2], data.innerYawPID[3]);
  sendPID2(data.minPidValue, data.maxPidValue);
  sendPID3(storage.getData().motorMinSignal, storage.getData().motorMaxSignal);
}

void sendPID(char* controller, float p, float i, float d, float maxI)
{
  Serial.print("PID "); Serial.print(controller); Serial.print(" "); Serial.print(p,4);
  Serial.print(" "); Serial.print(i,4); Serial.print(" "); Serial.print(d,4); Serial.print(" "); Serial.println(maxI,4); 
}

void sendPID2(float min, float max)
{
  Serial.print("PID X MIN="); Serial.print(min); Serial.print(" MAX="); Serial.println(max);
}

void sendPID3(uint16_t min, uint16_t max)
{
  Serial.print("MTR MIN="); Serial.print(min); Serial.print(" MAX="); Serial.println(max);
}

void readFloatArray(float * outArray, int size)
{
  for (int i = 0; i < size; i++)
  {
    char *arg = cmd.next();
    if (arg == NULL) return;
    outArray[i] = atof(arg);
  } 
}

void setPID()
{
  char *arg = cmd.next();
  if (arg == NULL) return;
  char controller = *arg;
  
  float pidl[4];
  readFloatArray(pidl, 4);
  
  for (int i=0; i<4; i++)
  {
    switch (controller)
    {
      case 'A':
        storage.getData().controlSystemData.outerRollPID[i] = pidl[i];
        return;
      case 'B':
        storage.getData().controlSystemData.outerPitchPID[i] = pidl[i];
        return;
      case 'C': 
        storage.getData().controlSystemData.outerYawPID[i] = pidl[i];
        return;
      case 'D':
        storage.getData().controlSystemData.innerRollPID[i] = pidl[i];
        return;
      case 'E':
        storage.getData().controlSystemData.innerPitchPID[i] = pidl[i];
        return;
      case 'F': 
        storage.getData().controlSystemData.innerYawPID[i] = pidl[i];
        return;
    }
  }
}

void setMotor()
{
  char *arg = cmd.next();
  
  if (arg == NULL) return;
  char minMaxOption = *arg;
  arg = cmd.next();
  if (arg == NULL) return;
  uint16_t minMax = atoi(arg);
  
  if (minMaxOption == 'I')
  {
    storage.getData().motorMinSignal = minMax;
  }
  else
  {
    storage.getData().motorMaxSignal = minMax;
  }
}

void setPidLimit()
{
  char *arg = cmd.next();
  
  if (arg == NULL) return;
  char minMaxOption = *arg;
  arg = cmd.next();
  if (arg == NULL) return;
  float minMax = atof(arg);
  
  if (minMaxOption == 'I')
  {
    storage.getData().controlSystemData.minPidValue = minMax;
  }
  else
  {
    storage.getData().controlSystemData.maxPidValue = minMax;
  }
}
*/

void unrecognized()
{
  Serial.println("What?"); 
}

void loop()
{

#ifdef ENABLE_TIMER_POWER_CUT
	power_cut_timer.update();
	if (power_cut_timer.elapsed() && armed == true)
	{
		disarm();
	}
#endif


  cmd.readSerial();
  
  uint32_t now = Process::micros();
  float dt = (now - lastMicros) / 1000000.0;
  lastMicros = now;
  
  /*
  stopwatch.update();
  if (stopwatch.elapsed())
  {
  float gyroYPR[3];
  imu.getGyroYawPitchRollRates(gyroYPR);
  Serial.println(gyroYPR[2]);
  stopwatch.reset();
  }*/
  
  if (armed == true)
  {
    float pids[3];
    float angles[3];
    uint16_t motorData[4];
    multicopter.update(dt, pids, angles, motorData);
  }
}
