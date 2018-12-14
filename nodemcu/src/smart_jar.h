#ifndef SMART_JAR_H
#define SMART_JAR_H

#include "Arduino.h"
#include <HX711.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "config.h"

bool connect_wifi() {

    // Start by connecting to a WiFi network
    printf("Connecting to: %s\n", SCJ_WIFI_SSID);

    WiFi.begin(SCJ_WIFI_SSID, SCJ_WIFI_PASSWORD);

    unsigned long wifiConnectStart = millis();

    // Do connect
    while (WiFi.status() != WL_CONNECTED) {
        // Check to see if
        if (WiFi.status() == WL_CONNECT_FAILED) {
            printf("Failed to connect to WiFi. Please verify credentials: \n");
            delay(4000);
        }

        delay(500);
        printf("...\n");

        // Only try for X seconds.
        if (millis() - wifiConnectStart > WIFI_TIME_TO_CONNECT) {
            printf("Failed to connect to WiFi\n");
            return false;
        }
    }

    printf("Successfully connected\n");

    return true;
}

float read_scale_weight() {
    HX711 scale(DATAOUT, CLK);
    scale.set_scale(SCALE_CALIBRATION_FACTOR);

    while(true) {
        int measurements[SCALE_WEIGHT_REPETITIONS];
        for (int i = 0; i < SCALE_WEIGHT_REPETITIONS; i++) {
            scale.power_up();
            measurements[i] = scale.get_units(5);
            scale.power_down();
            printf("%i / %i Read scale weight: %i\n", i + 1, SCALE_WEIGHT_REPETITIONS, measurements[i]);
            if (i != SCALE_WEIGHT_REPETITIONS - 1) {
                delay(SCALE_MEASUREMENT_DELAY);
            }
        }  

        int firstMeasurement = measurements[0];
        int average = firstMeasurement;
        bool error = false;
        for (int i = 1; i < SCALE_WEIGHT_REPETITIONS; i++) {
            if (abs(firstMeasurement - measurements[i]) > SCALE_MAX_OFFSET) {
                printf("One measurement is beyond maximum offset: %i\nTrying again...\n", measurements[i]);
                error = true;
                break;
            } else {
                average += measurements[i];
            }
        }

        if (error) continue;
        average = average / SCALE_WEIGHT_REPETITIONS;
        return average;
    }
}

void send_data(float scaleWeight) {
    WiFiClient wifiClient;
    PubSubClient mqttClient(wifiClient);
    mqttClient.setServer(SCJ_MQTT_SERVER, (int)SCJ_MQTT_PORT);
    if(mqttClient.connect("CookieJar", SCJ_MQTT_USER, SCJ_MQTT_PASSWORD)) {
        printf("Connected to MQTT server\n");

        String payload = String(scaleWeight, 1);
        printf("Trying to send payload: %s\n", payload.c_str());
        if(mqttClient.publish("weight", (char*)payload.c_str())) {
            printf("Successfully sent data: ");
        } else {
            printf("Error sending data: ");
        }
    } else {
        printf("Error connecting to MQTT server: ");
    }
    printf("%i", mqttClient.state());
    printf("\n");
    delay(1000);
}

void go_sleep() {
    printf("Going to sleep for seconds: %i\n",  (int)(SLEEP_TIME * 1000000LL));
    ESP.deepSleep(SLEEP_TIME * 1000000LL);
}

static float previousWeight = 0;

bool significant_weight_change(float scaleWeight) {
    return abs(scaleWeight - previousWeight) > WEIGHT_DIFFERENCE_TO_SEND;
}

void setup() {
    Serial.begin(9600);

    float weight = read_scale_weight();

    if (significant_weight_change(weight)) {
        previousWeight = weight;
        if (connect_wifi()) {
            send_data(weight);
            WiFi.disconnect();
            WiFi.mode(WIFI_OFF);
        } else {
            printf("Could not connect to wifi\n");
        }
    } else {
        printf("No significant weight change\n");
    }

    go_sleep();
}

void loop() {

}

#endif