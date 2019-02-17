#include <Arduino.h>
#include "config.h"

#include "i2c.h"
#include "scale.h"
#include "sleep.h"


void power_esp_on();
void power_esp_off();

void power_hx711_on();
void power_hx711_off();

float weight __attribute__((section(".noinit")));
int weightSavedFlag __attribute__((section(".noinit")));

bool espDone;

bool _espWaitState;
unsigned long start;

void onI2CRequest() {
    if (weightSavedFlag != 12345) {
        i2c_send_float(0);
    } else {
        i2c_send_float(weight);
    }
}

void onI2CReceive(uint8_t data) {
    if (data == 1) {
        espDone = true;
    }
}

void setup() {
    setup_sleep();
    
    if (weightSavedFlag != 12345) {
        weight = 0;
    }

    scale_begin(SCALE_DT, SCALE_CLK);

    i2c_on_request(onI2CRequest);
    i2c_on_receive(onI2CReceive);
    
    reset_sleeping();

    power_hx711_on();
    float newWeight = read_scale_weight();
    power_hx711_off();

    bool exceedsOffset = abs(newWeight - weight) > REQUIRED_WEIGHT_OFFSET;
    if (exceedsOffset) {
        weight = newWeight;
        weightSavedFlag = 12345;

        // Wake up ESP
        espDone = false;
        _espWaitState = true;
        start = millis();

        reset_sleeping();
        power_esp_on();
        i2c_start(I2C_ADDRESS);
    }

    go_sleep();
}

void loop() {

}

void power_esp_on() {
    pinMode(0, OUTPUT);
    pinMode(2, OUTPUT);
    digitalWrite(0, HIGH);
    digitalWrite(2, HIGH);
    delay(50);
    pinMode(POWER_ESP, OUTPUT);
    digitalWrite(POWER_ESP, LOW);
    delay(150);
    digitalWrite(POWER_ESP, HIGH);
}

void power_esp_off() {
    //digitalWrite(POWER_ESP, LOW);
}

void power_hx711_on() {
    scale_power_on();
}

void power_hx711_off() {
    scale_power_down();
}