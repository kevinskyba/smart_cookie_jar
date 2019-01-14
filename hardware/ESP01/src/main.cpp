#include <Arduino.h>
#include "i2c.h"
#include "config.h"
#include "wifi.h"
#include "mqtt.h"

int i = 0;

void setup() {
    delay(1000);
    i2c_start(I2C_SDA_PIN, I2C_SDL_PIN);
}

void loop() {
    int error = 0;
    int retries = I2C_RETRIES;
    float value = 0;

    while(retries > 0) {
        value = i2c_request_float_from_address(ATTINY_I2C_ADDRESS, error);
        if (error == 0 && !isnan(value)) break;
        value = 0;
        error = 0;
        retries--;
        if (retries == 0) {
            error = 40;
        }
    }

        
    if(connect_wifi(SCJ_WIFI_SSID, SCJ_WIFI_PASSWORD)) {
        if (error != 0) {
            send_int_to_mqtt(SCJ_MQTT_SERVER, SCJ_MQTT_PORT, SCJ_MQTT_ID, SCJ_MQTT_USER, SCJ_MQTT_PASSWORD, "error", error);
        } else if (isnan(value)) {
            send_string_to_mqtt(SCJ_MQTT_SERVER, SCJ_MQTT_PORT, SCJ_MQTT_ID, SCJ_MQTT_USER, SCJ_MQTT_PASSWORD, "error", (String("nan: ") + String(value, 1)).c_str());
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