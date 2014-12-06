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

I2C i2c;
ITG3200Gyroscope gyroscope(i2c);
BMA180Accelerometer accelerometer(i2c);
HMC5883LMagnetometer magnetometer(i2c);
FreeIMU imu(accelerometer, gyroscope, magnetometer);

SerialCommand cmd;
Storage storage;
//Quadcopter quadcopter(9,7,5,4);
Quadcopter quadcopter(4,5,7,9);
Multicopter multicopter(quadcopter, storage, imu);

#define UPDATE_INTERVAL_MILI_SEC 10
uint32_t lastMicros;
Stopwatch stopwatch(UPDATE_INTERVAL_MILI_SEC);
Stopwatch stopwatch2(1000);

bool armed;

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
  cmd.addCommand("SETPID", setPID);
  cmd.addCommand("SETMTR", setMotor);
  cmd.addCommand("PIDLIM", setPidLimit);
  cmd.addCommand("SAVE", save);
  cmd.addCommand("GET", get);
  cmd.addDefaultHandler(unrecognized);
}

void arming()
{
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

void get()
{
  ControlSystemData& data = storage.getData().controlSystemData;
  sendPID1("R",data.rollPID[0], data.rollPID[1], data.rollPID[2]);
  sendPID1("P", data.pitchPID[0], data.pitchPID[1], data.pitchPID[2]);
  sendPID1("Y", data.yawPID[0], data.yawPID[1], data.yawPID[2]);
  sendPID2(data.minPidValue, data.maxPidValue);
  sendPID3(storage.getData().motorMinSignal, storage.getData().motorMaxSignal);
}

void sendPID1(char* controller, float p, float i, float d)
{
  Serial.print("PID "); Serial.print(controller); Serial.print(" P="); Serial.print(p,4);
  Serial.print(" I="); Serial.print(i,4); Serial.print(" D="); Serial.println(d,4);
}

void sendPID2(float min, float max)
{
  Serial.print("PID X MIN="); Serial.print(min); Serial.print(" MAX="); Serial.println(max);
}

void sendPID3(uint16_t min, uint16_t max)
{
  Serial.print("MTR MIN="); Serial.print(min); Serial.print(" MAX="); Serial.println(max);
}

uint16_t toPIDIndex(char v)
{
  switch (v)
  {
    case 'P': return 0;
    case 'I': return 1;
    case 'D': return 2;
  }
}

void setPID()
{
  char *arg = cmd.next();
  if (arg == NULL) return;
  char controller = *arg;
  arg = cmd.next();
  if (arg == NULL) return;
  char coefficient = *arg;
  arg = cmd.next();
  if (arg == NULL) return;
  float value = atof(arg);
  
  uint16_t index = toPIDIndex(coefficient);
  switch (controller)
  {
    case 'Y': 
      storage.getData().controlSystemData.yawPID[index] = value;
      return;
    case 'R':
      storage.getData().controlSystemData.rollPID[index] = value;
      return;
    case 'P':
      storage.getData().controlSystemData.pitchPID[index] = value;
      return;
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

void unrecognized()
{
  Serial.println("What?"); 
}

void DebugVector(char* title, float* table, uint16_t count)
{
  Serial.print(title);
  for (int i=0; i<count; i++)
  {
    Serial.print(" ");
    Serial.print(table[i], 4);
  }
  Serial.print("\n");
}

void DebugVector(char* title, uint16_t* table, uint16_t count)
{
  Serial.print(title);
  for (int i=0; i<count; i++)
  {
    Serial.print(" ");
    Serial.print(table[i]);
  }
  Serial.print("\n");
}

void loop()
{
  cmd.readSerial();
  //stopwatch2.update();
  
  uint32_t now = Process::micros();
  float dt = (now - lastMicros) / 1000000.0;
  lastMicros = now;
  
  
  if (armed == true)
  {
    float pids[3];
    float angles[3];
    uint16_t motorData[4];
    multicopter.update(dt, pids, angles, motorData);
    /*
    if (stopwatch2.elapsed())
    {
      Serial.print("dt "); Serial.println(dt);
      DebugVector("pids", pids, 3);
      DebugVector("mtr data", motorData, 4);
      DebugVector("angles", angles, 3);
      stopwatch2.reset();
    }
    */
  }
}
