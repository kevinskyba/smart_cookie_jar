#ifndef SECRETS_H
#define SECRETS_H

#define SCJ_WIFI_SSID           ""
#define SCJ_WIFI_PASSWORD       ""

#define SCJ_MQTT_SERVER         ""
#define SCJ_MQTT_PORT           0

#define SCJ_MQTT_ID             ""

static const uint8_t* SCJ_MQTT_ROOT_CA = (uint8_t*) "-----BEGIN CERTIFICATE-----\n" \ 
"...\n" \
"-----END CERTIFICATE-----\n";

static const uint8_t* SCJ_MQTT_CERT_PEM = (uint8_t*) "-----BEGIN CERTIFICATE-----\n" \
"...\n" \
"-----END CERTIFICATE-----\n";

static const uint8_t* SCJ_MQTT_PRIVATE_KEY = (uint8_t*) "-----BEGIN RSA PRIVATE KEY-----\n" \
"...\n" \
"-----END RSA PRIVATE KEY-----\n";

#endif