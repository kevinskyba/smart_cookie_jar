#include "battery.h"
#include <Wire.h>
#include <Esp.h>

void battery_init(int pin) {
}

float battery_read_voltage(int pin, float R1, float R2) {
    int value = analogRead(pin);
    float analogValue = value / 1024.0;
    return analogValue / (R2 / (R1 + R2));
}