#ifndef Storage_h
#define Storage_h

#include <avr/eeprom.h>

#define PERSISTENT_DATA_ADDR 0x00

struct ControlSystemData
{
  // [0] = P, [1] = I, [2] = D, [3] = IMax
  float outerRollPID[4];
  float innerRollPID[4];
  float outerPitchPID[4];
  float innerPitchPID[4];
  float outerYawPID[4];
  float innerYawPID[4];
  float minPidValue;
  float maxPidValue;
};

struct FPersistentData
{
  float gyroscopeOffsets[3];
  float accelerometerOffsets[3];
  float magnetometerOffsets[3];
  float magnetometerDeclination;
  ControlSystemData controlSystemData;
  uint16_t motorMinSignal;
  uint16_t motorMaxSignal;
};

class Storage
{
 public:
  void load()
  {
    uint16_t address = PERSISTENT_DATA_ADDR;
    eeprom_read_block((void*)&this->data, (const void*)address, sizeof(FPersistentData));
  }

  void save()
  {
    uint16_t address = PERSISTENT_DATA_ADDR;
    eeprom_write_block((const void*)&this->data, (void*)address, sizeof(FPersistentData));
  }

  FPersistentData& getData()
  {
    return this->data;
  }

 private:
  FPersistentData data;
};

#endif
