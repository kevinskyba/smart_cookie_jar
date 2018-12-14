#ifndef MQTT_H
#define MQTT_H

bool send_float_to_mqtt(const char* server, int port, const char* id, const char* user, const char* password, const char* topic, float value);
bool send_int_to_mqtt(const char* server, int port, const char* id, const char* user, const char* password, const char* topic, int value);

#endif