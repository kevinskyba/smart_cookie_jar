#include "i2c.h"
#include <Wire.h>

float bytes_to_float(byte* bytes);

void i2c_start(int sdaPin, int sdlPin) {
    Wire.begin(sdaPin, sdlPin);
}

float i2c_request_float_from_address(uint8_t address, int &error) {
    Wire.setTimeout(5000);
    Wire.requestFrom(address, 4);

    if (!Wire.available()) {
        error = 70;
        return 0;
    }

    uint8 bytes[4];

    int received = 0;
    for (int i = 0; i < 4; i++) {
        if (Wire.available()) {
            received++;
            bytes[i] = Wire.read();
        } else {
            error = 70 + received;
            return 0;
        }
    }

    if (Wire.available()) { 
        error = 1;
        return 0;
    }

    return bytes_to_float(bytes);
}

void i2c_send(uint8_t address, uint8_t data) {
    Wire.beginTransmission(address);
    Wire.write(data);
    Wire.endTransmission();
}

float bytes_to_float(byte* bytes) {
    float f;
    memcpy(&f, bytes, 4);
    return f;
}