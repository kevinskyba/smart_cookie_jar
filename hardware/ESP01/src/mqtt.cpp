#include "mqtt.h"
#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>
#include <Esp.h>

bool send_payload(const char* server, int port, const char* id, const uint8_t* root_ca, const uint8_t* cert_pem, const uint8_t* private_key, const char* topic, String payload) {
    ESP.wdtFeed();
    WiFiClientSecure wifiClient;
    wifiClient.setCACert(root_ca, 1188);
    wifiClient.setCertificate(cert_pem, 1224);
    wifiClient.setPrivateKey(private_key, 1679);

    wifiClient.setTimeout(MQTT_SOCKET_TIMEOUT * 1000);
    PubSubClient mqttClient(wifiClient);
    mqttClient.setServer(server, port);

    if(mqttClient.connect(id)) {
        ESP.wdtFeed();
        if (mqttClient.publish(topic, (char*)payload.c_str())) {
            ESP.wdtFeed();
            delay(100);
            mqttClient.disconnect();
            return true;
        }
    }
    ESP.wdtFeed();

    mqttClient.disconnect();

    return false;
}

bool send_string_to_mqtt(const char* server, int port, const char* id, const uint8_t* root_ca, const uint8_t* cert_pem, const uint8_t* private_key, const char* topic, const char* value) {
    return send_payload(server, port, id, root_ca, cert_pem, private_key, topic, value);
}