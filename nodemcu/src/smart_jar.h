#ifndef SMART_JAR_H
#define SMART_JAR_H

#include "Arduino.h"
#include <HX711.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <PubSubClient.h>
#include "config.h"

bool connect_wifi() {

    // Start by connecting to a WiFi network
    Serial.println();
    Serial.println();
    Serial.print("Connecting to: ");
    Serial.println(SCJ_WIFI_SSID);

    WiFi.begin(SCJ_WIFI_SSID, SCJ_WIFI_PASSWORD);

    unsigned long wifiConnectStart = millis();

    // Do connect
    while (WiFi.status() != WL_CONNECTED) {
        // Check to see if
        if (WiFi.status() == WL_CONNECT_FAILED) {
            Serial.println("Failed to connect to WiFi. Please verify credentials: ");
            delay(4000);
        }

        delay(500);
        Serial.println("...");
        // Only try for X seconds.
        if (millis() - wifiConnectStart > WIFI_TIME_TO_CONNECT) {
            Serial.println("Failed to connect to WiFi");
            return false;
        }
    }

    Serial.println("Successfully connected");

    return true;
}

float read_scale_weight() {
    HX711 scale(DATAOUT, CLK);
    scale.power_up();
    scale.set_scale(SCALE_CALIBRATION_FACTOR);

    while(true) {
        int measurements[SCALE_WEIGHT_REPETITIONS];
        for (int i = 0; i < SCALE_WEIGHT_REPETITIONS; i++) {
            delay(SCALE_MEASUREMENT_DELAY);
            measurements[i] = scale.get_units(5);
            Serial.print(i + 1);
            Serial.print(" / ");
            Serial.print(SCALE_WEIGHT_REPETITIONS);
            Serial.print(" Read scale weight: ");
            Serial.println(measurements[i]);
        }  

        int firstMeasurement = measurements[0];
        int average = firstMeasurement;
        bool error = false;
        for (int i = 1; i < SCALE_WEIGHT_REPETITIONS; i++) {
            if (abs(firstMeasurement - measurements[i]) > SCALE_MAX_OFFSET) {
                Serial.print("One measurement is beyond maximum offset: ");
                Serial.println(measurements[i]);
                Serial.println("Trying again...");
                error = true;
                break;
            } else {
                average += measurements[i];
            }
        }

        if (error) continue;

        scale.power_down();
        average = average / SCALE_WEIGHT_REPETITIONS;
        return average;
    }
}

void send_data(float scaleWeight) {
    WiFiClient wifiClient;
    PubSubClient mqttClient(wifiClient);
    mqttClient.setServer(SCJ_MQTT_SERVER, (int)SCJ_MQTT_PORT);
    if(mqttClient.connect("CookieJar", SCJ_MQTT_USER, SCJ_MQTT_PASSWORD)) {
        Serial.println("Connected to MQTT server");

        String payload = String(scaleWeight, 1);
        Serial.print("Trying to send payload: ");
        Serial.println(payload);
        if(mqttClient.publish("weight", (char*)payload.c_str())) {
            Serial.print("Successfully sent data: ");
        } else {
            Serial.print("Error sending data: ");
        }
    } else {
        Serial.print("Error connecting to MQTT server: ");
    }
    Serial.println(mqttClient.state());
    delay(1000);
}

void go_sleep() {
    Serial.print("Going to sleep for ");
    Serial.println(SLEEP_TIME);
    ESP.deepSleep(SLEEP_TIME, WAKE_RF_DEFAULT);
}

void setup() {
    Serial.begin(9600);

    // Wait for serial to initialize.
    while (!Serial) { }

    if (!connect_wifi()) {
        go_sleep();
        return;
    }

    float weight = read_scale_weight();
    send_data(weight);

    WiFi.disconnect();
    WiFi.mode(WIFI_OFF);
    
    go_sleep();
}

void loop() {

}

#endif