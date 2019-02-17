#ifndef MQTT_H
#define MQTT_H

#include <Arduino.h>

bool send_string_to_mqtt(const char* server, int port, const char* id, const uint8_t* root_ca, const uint8_t* cert_pem, const uint8_t* private_key, const char* topic, const char* value);

#endif