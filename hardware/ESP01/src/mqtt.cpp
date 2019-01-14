#include "mqtt.h"
#include "wifi.h"
#include <Arduino.h>
#include <ESP8266HTTPClient.h>
#include <PubSubClient.h>
#include "config.h"

bool send_payload(const char* server, int port, const char* id, const char* user, const char* password, const char* topic, String payload) {
    WiFiClient wifiClient;
    PubSubClient mqttClient(wifiClient);
    mqttClient.setServer(server, port);

    if(mqttClient.connect(id, user, password)) {
        if (mqttClient.publish(topic, (char*)payload.c_str())) {
            delay(100);
            mqttClient.disconnect();
            return true;
        }
    }

    mqttClient.disconnect();

    return false;
}

bool send_float_to_mqtt(const char* server, int port, const char* id, const char* user, const char* password, const char* topic, float value) {
    return send_payload(server, port, id, user, password, topic, String(value, 1));
}

bool send_int_to_mqtt(const char* server, int port, const char* id, const char* user, const char* password, const char* topic, int value) {
    return send_payload(server, port, id, user, password, topic, String(value));
}

bool send_string_to_mqtt(const char* server, int port, const char* id, const char* user, const char* password, const char* topic, const char* value) {
    return send_payload(server, port, id, user, password, topic, value);
}