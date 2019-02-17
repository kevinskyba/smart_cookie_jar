#include "wifi.h"
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include "config.h"
#include <Esp.h>

bool connect_wifi(const char* ssid, const char* password) {

    ESP.wdtFeed();

    // Start by connecting to WiFi network
    WiFi.begin(ssid, password);

    unsigned long wifiConnectStart = millis();

    // Do connect
    while (WiFi.status() != WL_CONNECTED) {
        // Check to see if connection failed
        if (WiFi.status() == WL_CONNECT_FAILED) {
            return false;
        }
        delay(500);
        ESP.wdtFeed();
        // Only try for X seconds.
        if (millis() - wifiConnectStart > WIFI_TIME_TO_CONNECT) {
            return false;
        }
    }

    return true;
}

void disconnect_wifi() {
    WiFi.disconnect();
}
