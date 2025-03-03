# Basis-Image (Ubuntu)
FROM ubuntu:latest

# Umgebungsvariablen setzen, damit npm nicht nach Region fragt
ENV DEBIAN_FRONTEND=noninteractive

# Arbeitsverzeichnis setzen
WORKDIR /Kalender

# System-Updates und Installationen
RUN apt update && apt install -y \
    maven \
    npm \
    mariadb-server \
    mariadb-client \
    curl

# Node.js & Java prüfen (falls nicht installiert, kannst du hier nachinstallieren)
RUN node -v && java -version

# MariaDB Benutzer erstellen und Rechte setzen
RUN mkdir -p /var/run/mysqld && \
    chown -R mysql:mysql /var/run/mysqld /var/lib/mysql && \
    chmod 755 /var/run/mysqld

# Anwendungscode kopieren
COPY . .

# Abhängigkeiten installieren (falls erforderlich)
RUN npm install

# MariaDB Initialisierungsskript kopieren und ausführbar machen
COPY init-db.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh

# Ports freigeben
EXPOSE 3306 4000 8081 3000 8080

# Container-Eingangspunkt (MariaDB als mysql-Benutzer starten)
CMD ["su", "-s", "/bin/bash", "mysql", "-c", "mariadbd"]