#ifndef CONFIG_H
#define CONFIG_H

//#define MODE_CONFIGURATION          1

#define MQTT_VERSION 3

#define DATAOUT                     0
#define CLK                         2

#define SCALE_CALIBRATION_FACTOR    161
#define SCALE_WEIGHT_REPETITIONS    2
#define SCALE_MEASUREMENT_DELAY     2500
#define SCALE_MAX_OFFSET            25

#define SLEEP_TIME                  15
#define WEIGHT_DIFFERENCE_TO_SEND   50

#define WIFI_TIME_TO_CONNECT        15000

#include "secret.h"

#endif