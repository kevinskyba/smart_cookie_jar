#include <Arduino.h>
#include "i2c.h"
#include "config.h"
#include "wifi.h"
#include "mqtt.h"

void setup() {
    i2c_start(I2C_SDA_PIN, I2C_SDL_PIN);
}

void loop() {
    int error = 0;
    float value = i2c_request_float_from_address(ATTINY_I2C_ADDRESS, error);

    if(connect_wifi(SCJ_WIFI_SSID, SCJ_WIFI_PASSWORD)) {
        if (error != 0) {
            send_int_to_mqtt(SCJ_MQTT_SERVER, SCJ_MQTT_PORT, SCJ_MQTT_ID, SCJ_MQTT_USER, SCJ_MQTT_PASSWORD, "error", error);
        } else {
            send_float_to_mqtt(SCJ_MQTT_SERVER, SCJ_MQTT_PORT, SCJ_MQTT_ID, SCJ_MQTT_USER, SCJ_MQTT_PASSWORD, "weight", value);
        }
    }

    delay(100);
    disconnect_wifi();
    delay(100);
    i2c_send(ATTINY_I2C_ADDRESS, 1);
    delay(100);
    ESP.deepSleep(0, RF_DEFAULT);
}