'''
/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
 '''

from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient
import logging
import time
import json
import ssl
import pandas as pd

print(ssl.OPENSSL_VERSION)

host = "a2it3lo4guail0-ats.iot.us-west-2.amazonaws.com"
rootCAPath = "cert/aws.root.pem"
certificatePath = "cert/scj.cert.pem"
privateKeyPath = "cert/scj.private.key"
clientId = "demo_client"
port = 8883

weight_data = pd.read_csv("data/weight_0.csv", header=None)
battery_data = pd.read_csv("data/battery_0.csv", header=None)

# Configure logging
logger = logging.getLogger("AWSIoTPythonSDK.core")
logger.setLevel(logging.ERROR)
streamHandler = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
streamHandler.setFormatter(formatter)
logger.addHandler(streamHandler)

# Init AWSIoTMQTTClient
myAWSIoTMQTTClient = AWSIoTMQTTClient(clientId)
myAWSIoTMQTTClient.configureEndpoint(host, port)
myAWSIoTMQTTClient.configureCredentials(rootCAPath, privateKeyPath, certificatePath)

# Connect and subscribe to AWS IoT
myAWSIoTMQTTClient.connect()
time.sleep(2)

# Publish to the same topic in a loop forever
index = 0
while True:
    if len(battery_data.index) < index or len(weight_data.index) < index:
        print("No more data")
        exit()

    message = {}
    message['battery'] = float(battery_data[1].iloc[index])
    messageJson = json.dumps(message)
    myAWSIoTMQTTClient.publish("battery", messageJson, 1)
    print("Published: " + messageJson)

    message = {}
    message['weight'] = float(weight_data[1].iloc[index])
    messageJson = json.dumps(message)
    myAWSIoTMQTTClient.publish("weight", messageJson, 1)
    print("Published: " + messageJson)

    index = index + 1
    time.sleep(8)
