#ifndef SCALE_H
#define SCALE_H

void scale_begin(int dtPin, int clkPin);
float read_scale_weight();
float read_delayed_scale_weight_average();

void scale_power_on();
void scale_power_down();

#endif