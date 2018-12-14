#ifndef SLEEP_H
#define SLEEP_H

#include <Arduino.h>

bool was_sleeping();
void reset_sleeping();
void setup_sleep();
void go_sleep();

#endif