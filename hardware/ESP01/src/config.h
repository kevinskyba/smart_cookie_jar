#ifndef CONFIG_H
#define CONFIG_H

#define MQTT_SOCKET_TIMEOUT         5

#define ATTINY_I2C_ADDRESS          1

#define I2C_SDA_PIN                 0
#define I2C_SDL_PIN                 2
#define I2C_RETRIES                 3

#define BATTERY_PIN                 3
#define BATTERY_R1                  100000.0
#define BATTERY_R2                  22000.0

#define WIFI_TIME_TO_CONNECT        15000
#define WIFI_TIMZONE                1

#include "secrets.h"

#endif