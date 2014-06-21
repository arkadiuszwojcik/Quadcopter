#include <inttypes.h>
#include "Sensors.h"

Sensors::Sensors(IAccelerometer* acc, IGyroscope* gyro, IMagnetometer* mag)
: m_accelerometer(acc)
, m_gyroscope(gyro)
, m_magnetometer(mag)
{}