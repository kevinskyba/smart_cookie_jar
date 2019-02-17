#ifndef BATTERY_H
#define BATTERY_H

#include <Arduino.h>

void battery_init(int pin);
float battery_read_voltage(int pin, float R1, float R2);

#endif