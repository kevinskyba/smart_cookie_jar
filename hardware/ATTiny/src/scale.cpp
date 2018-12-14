#include "scale.h"
#include "config.h"
#include <HX711.h>

HX711* scale;

void scale_begin(int dtPin, int clkPin) {
    scale = new HX711(dtPin, clkPin);
    scale->set_scale(SCALE_CALIBRATION);
    scale->set_offset(SCALE_OFFSET);
}

float read_scale_weight() {
    float measurement = scale->get_units();
    return measurement;
}

float read_delayed_scale_weight_average() {
    while(true) {
        float measurements[SCALE_WEIGHT_REPETITIONS];
        for (int i = 0; i < SCALE_WEIGHT_REPETITIONS; i++) {
            scale->power_up();
            measurements[i] = scale->get_units();
            scale->power_down();
            if (i != SCALE_WEIGHT_REPETITIONS - 1) {
                delay(SCALE_MEASUREMENT_DELAY);
            }
        }  

        float firstMeasurement = measurements[0];
        float average = firstMeasurement;
        bool error = false;
        for (int i = 1; i < SCALE_WEIGHT_REPETITIONS; i++) {
            if (abs(firstMeasurement - measurements[i]) > SCALE_MAX_OFFSET) {
                error = true;
                break;
            } else {
                average += measurements[i];
            }
        }

        if (error) continue;
        average = average / SCALE_WEIGHT_REPETITIONS;
        return average;
    }
}

void scale_power_on() {
    scale->power_up();
}

void scale_power_down() {
    scale->power_down();
}