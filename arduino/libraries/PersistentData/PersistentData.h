#ifndef PersistentData_h
#define PersistentData_h

#define STORAGE_SENSORS_DATA_ADDRESS 0x00

struct FSensorsData
{
	float gyroscopeOffsets[3];
	float accelerometerOffsets[3];
	float magnetometerOffsets[3];
	float magnetometerDeclination;
};

#endif