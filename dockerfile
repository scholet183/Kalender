# Basis-Image (Ubuntu)
FROM ubuntu:latest

# Umgebungsvariablen setzen
ENV DEBIAN_FRONTEND=noninteractive

# System-Updates und Installationen
RUN apt update && apt install -y \
    maven \
    npm \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Arbeitsverzeichnis setzen
WORKDIR /Kalender

# Anwendungscode kopieren
COPY . .

# Abhängigkeiten installieren
RUN set -e && npm install

# Ports freigeben (falls deine App Web-Zugriff benötigt)
EXPOSE 4000 8080 8081 3000

# Container im interaktiven Modus starten (kein Skript wird automatisch ausgeführt)
CMD ["/bin/bash"]