#ifndef CONFIG_H
#define CONFIG_H

//#define MODE_CONFIGURATION          1

#define MQTT_VERSION 3

#define DATAOUT                     4
#define CLK                         5

#define SCALE_CALIBRATION_FACTOR    161
#define SCALE_WEIGHT_REPETITIONS    5
#define SCALE_MEASUREMENT_DELAY     1000
#define SCALE_MAX_OFFSET            25

#define SLEEP_TIME                  1.5e7

#define WIFI_TIME_TO_CONNECT        15000

#include "secret.h"

#endif