#!/bin/bash

set -e
 
echo "Starte User Microservice..."
echo "Starte Kalender Microservice..."

cd /app/
java -jar calendar*.jar &
java -jar user*.jar &
 
# Warten, bis alle gestarteten Prozesse beendet sind

echo "Starte Angular-Frontend auf Port 4200..."
sudo systemctl start nginx

wait
