#ifndef I2C_H
#define I2C_H

#include <Arduino.h>

void i2c_start(uint8_t address);
void i2c_loop();
void i2c_on_request(void (*function)(void));
void i2c_on_receive(void (*function)(uint8_t));
void i2c_send_float(float data);

#endif