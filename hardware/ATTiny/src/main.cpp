#include <Arduino.h>
#include "config.h"

#include "i2c.h"
#include "scale.h"
#include "sleep.h"


void power_esp_on();
void power_esp_off();

void power_hx711_on();
void power_hx711_off();

float weight;
bool espDone;

bool _espWaitState;
unsigned long start;

void onI2CRequest() {
    i2c_send_float(weight);
}

void onI2CReceive(uint8_t data) {
    if (data == 1) {
        espDone = true;
    }
}

void setup() {
    pinMode(POWER_ESP, OUTPUT);
    power_esp_off();

    scale_begin(SCALE_DT, SCALE_CLK);

    i2c_start(I2C_ADDRESS);
    i2c_on_request(onI2CRequest);
    i2c_on_receive(onI2CReceive);
}

void loop() {

    reset_sleeping();

    power_hx711_on();
    float newWeight = read_scale_weight();
    power_hx711_off();

    bool exceedsOffset = abs(newWeight - weight) > REQUIRED_WEIGHT_OFFSET;
    if (exceedsOffset) {
        weight = newWeight;

        // Wake up ESP
        espDone = false;
        _espWaitState = true;
        start = millis();

        power_esp_on();

        while (!espDone && start + MAX_WAIT_FOR_ESP > millis()) {
            i2c_loop();
        }

        espDone = false;
        power_esp_off();
    }

    setup_sleep();
    go_sleep();
}

void power_esp_on() {
    digitalWrite(POWER_ESP, HIGH);
}

void power_esp_off() {
    digitalWrite(POWER_ESP, LOW);
}

void power_hx711_on() {
    scale_power_on();
}

void power_hx711_off() {
    scale_power_down();
}