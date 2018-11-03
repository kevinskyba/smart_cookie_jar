#ifndef CALIBRATION_H
#define CALIBRATION_H

#include "Arduino.h"
#include <HX711.h>
#include "config.h"

HX711 scale(DATAOUT, CLK);

float calibration_factor = SCALE_CALIBRATION_FACTOR;

void setup() {
    Serial.begin(9600);

    Serial.println("HX711 calibration sketch");
    Serial.println("Remove all weight from scale");
    Serial.println("After readings begin, place known weight on scale");
    Serial.println("Press + or a to increase calibration factor");
    Serial.println("Press - or z to decrease calibration factor");

    scale.set_scale();
    scale.tare();	//Reset the scale to 0

    long zero_factor = scale.read_average(); //Get a baseline reading
    Serial.print("Zero factor: "); //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
    Serial.println(zero_factor);
}

void loop() {
    scale.set_scale(calibration_factor); //Adjust to this calibration factor

    Serial.print("Reading: ");

    float units = scale.get_units();
    Serial.print(units, 5);
    Serial.print("   |   Factor: ");
    Serial.println(calibration_factor);

    if(Serial.available())
    {
        char temp = Serial.read();
        if(temp == '+' || temp == 'a')
            calibration_factor += 10;
        else if(temp == '-' || temp == 'z')
            calibration_factor -= 10;
    }
}


#endif