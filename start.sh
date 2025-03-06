#!/bin/bash

set -e
 
echo "Starte User Microservice..."
echo "Starte Kalender Microservice..."

cd /Kalender/app

java -jar *.jar &
 
# Warten, bis alle gestarteten Prozesse beendet sind

echo "Starte Angular-Frontend auf Port 4200..."
sudo systemctl start nginx

wait
