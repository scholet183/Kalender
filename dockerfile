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
 
# Abhängigkeiten installieren (z.B. für Node, falls benötigt)

RUN set -e && npm install
 
# Start-Skript kopieren und ausführbar machen

COPY start.sh /Kalender/start.sh

RUN chmod +x /Kalender/start.sh
 
# Ports freigeben (je nach Bedarf)

EXPOSE 4000 8080 8081 3000
 
# Start-Skript als Container-Befehl festlegen

CMD ["/Kalender/start.sh"]

 
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
 
# Abhängigkeiten installieren (z.B. für Node, falls benötigt)

RUN set -e && npm install
 
# Start-Skript kopieren und ausführbar machen

COPY start.sh /Kalender/start.sh

RUN chmod +x /Kalender/start.sh
 
# Ports freigeben (je nach Bedarf)

EXPOSE 4000 8080 8081 3000
 
# Start-Skript als Container-Befehl festlegen

CMD ["/Kalender/start.sh"]

 