FROM openjdk:21-slim
 
# Installiere Maven, npm, curl

RUN apt update && apt install -y \
    maven \
    npm \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
 
WORKDIR /Kalender
 
# Anwendungscode kopieren

COPY . .
 
# Abhängigkeiten installieren (z.B. für Node, falls benötigt)

RUN set -e && npm install
 
# Start-Skript kopieren und ausführbar machen

COPY start.sh /Kalender/start.sh

RUN chmod +x /Kalender/start.sh
 
# Ports freigeben (je nach Bedarf)

EXPOSE 4200 8080 8081 3000
 
# Start-Skript als Container-Befehl festlegen

CMD ["/Kalender/start.sh"]