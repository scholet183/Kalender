#!/bin/bash

set -e
 
echo "Starte User Microservice..."

cd /Kalender/Website/user_microservice

mvn clean spring-boot:run &
 
echo "Starte Kalender Microservice..."

cd /Kalender/Website/calendar_microservice

mvn clean spring-boot:run &
 
# Warten, bis alle gestarteten Prozesse beendet sind

echo "Aktualisiere Paketlisten..."
apt update -y

cd /Kalender/Website/frontend

echo "Installiere Abhängigkeiten..."
npm install
npm install -g npm@10.9.2
npm install -g yarn@1.13.0
npm install -g node@18.19.1

echo "Installiere Angular CLI..."
npm install -g @angular/cli@19.1.6

echo "Starte Angular-Frontend auf Port 4200..."
ng serve --host 0.0.0.0 --port 4200

wait