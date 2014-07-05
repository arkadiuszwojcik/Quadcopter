#ifndef MulticopterData_h
#define MulticopterData_h

#include <avr/eeprom.h>

#define STORAGE_DATA_ADDR 0x00

struct FPersistentData
{
  float gyroscopeOffsets[3];
  float accelerometerOffsets[3];
  float magnetometerOffsets[3];
  float magnetometerDeclination;
  float rollPID[3];
  float pitchPID[3];
  float yawPID[3];
  uint16_t motorMinSig;
  uint16_t motorMaxSig;
};

class MulticopterData
{
 public:
  void load()
  {
    uint16_t address = STORAGE_DATA_ADDR;
    eeprom_read_block((void*)&this->data, (const uint8_t*)address, sizeof(FPersistentData));
  }

  void save()
  {
    uint16_t address = STORAGE_DATA_ADDR;
    eeprom_write_block((void*)&this->data, (const uint8_t*), sizeof(FPersistentData));
  }

  void FPersistentData& getData()
  {
    return this->data;
  }

 private:
  FPersistenData data;
};

#endif
