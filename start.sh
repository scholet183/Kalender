#!/bin/bash

set -e
 
echo "Starte User Microservice..."

cd /Kalender/Website/user_microservice

mvn clean spring-boot:run &
 
echo "Starte Kalender Microservice..."

cd /Kalender/Website/calendar_microservice

mvn clean spring-boot:run &
 
# Warten, bis alle gestarteten Prozesse beendet sind

wait