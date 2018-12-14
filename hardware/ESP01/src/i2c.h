#ifndef I2C_H
#define I2C_H

#include <Arduino.h>

void i2c_start(int sdaPin, int sdlPin);
float i2c_request_float_from_address(uint8_t address, int &error);
void i2c_send(uint8_t address, uint8_t data);

#endif