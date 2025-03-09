#!/bin/bash

set -e
# Warten, bis alle gestarteten Prozesse beendet sind

echo "Starte Angular-Frontend auf Port 4200..."
service nginx stop
service nginx start

wait
