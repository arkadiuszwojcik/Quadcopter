#ifndef Storage_h
#define Storage_h

#include <avr/eeprom.h>

#define PERSISTENT_DATA_ADDR 0x00

struct ControlSystemData
{
  float rollPID[3];
  float pitchPID[3];
  float yawPID[3];
  float minPidValue;
  float maxPidValue;
  bool isYawControlEnabled;
  bool isPitchControlEnabled;
  bool isRollControlEnabled;
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
