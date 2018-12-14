#ifndef CONFIG_H
#define CONFIG_H

#include <Arduino.h>

#define POWER_ESP                   1

#define MAX_WAIT_FOR_ESP            20000UL

#define SLEEP_CYCLES                2
#define SLEEP_MS                    16000UL
#define REQUIRED_WEIGHT_OFFSET      25    

#define I2C_ADDRESS                 1

#define SCALE_DT                    3
#define SCALE_CLK                   4

#define SCALE_CALIBRATION           161
#define SCALE_OFFSET                6391895.0
#define SCALE_WEIGHT_REPETITIONS    2
#define SCALE_MEASUREMENT_DELAY     1000
#define SCALE_MAX_OFFSET            25

#endif