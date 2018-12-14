#include "i2c.h"
#include <TinyWireS.h>

void i2c_start(uint8_t address) {
    TinyWireS.begin(address);
}

void i2c_loop() {
    TinyWireS_stop_check();
}

void i2c_on_request(void (*function)(void)) {
    TinyWireS.onRequest(function);
}

void i2c_on_receive(void (*function)(uint8_t)) {
    TinyWireS.onReceive(function);
}

void i2c_send_float(float data) {
    for (int i = 0; i < 4; i++) {
        TinyWireS.send(*((byte*)(&data)+i));
    }
}
