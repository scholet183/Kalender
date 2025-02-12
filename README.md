# Kalender
DevOps-Projekt: CRUD-Webapplikation


# CRUD-Methoden
Events:

POST /events → Neues Event erstellen

GET /events → Alle Events abrufen

GET /events/{id} → Einzelnes Event abrufen

PUT /events/{id} → Event aktualisieren

DELETE /events/{id} → Event löschen

User:

POST /users → Neuen Benutzer erstellen

PUT /users/{id} → Benutzer aktualisieren

DELETE /users/{id} → Benutzer löschen

# Setup Dev-Environment
- npm v10.9.0
- nodejs v21.6.0
- java: openjdk-21
- maven: Apache Maven 3.6.3
- mariadb  Ver 15.1 Distrib 10.6.18-MariaDB
- docker
- docker-compose

```
$ cd backend/
$ mvn spring-boot:run
``` 

