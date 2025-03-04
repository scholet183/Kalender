# Kalender
DevOps-Projekt: CRUD-Webapplikation


# CRUD-Methoden
Appointments: /api/calendar

POST /addAppointment → Neues Event erstellen

GET /getAppointments/{id} → Alle Events eines bestimmten Nutzers abrufen

(Not yet implemented)
DELETE /events/{id} → Event löschen

User: /api/users

POST /save → Neuen Benutzer erstellen

GET /all → Alle Benutzer erhalten

GET /{id} → ein bestimmten Benutzer erhalten

PUT /{id} → ein bestimmten Benutzer aktualisieren

DELETE /{id} → ein bestimmten Benutzer löschen

# Setup Dev-Environment
- npm v10.9.0
- nodejs v21.6.0
- java: openjdk-21
- maven: Apache Maven 3.6.3
- mariadb  Ver 15.1 Distrib 10.6.18-MariaDB
- docker
- docker-compose
- DBeaver (if gui is needed)
- IntelliJ (as IDE)

## Setup MariaDB
- MariaDB should run on port 3306
- username: root
- password: verysecret
- create two databases manually:
-- userdatabase
-- calendardatabase


## Set-Up Backend-Microservices
- both Spring boot Microservices should run at the same time
```
$ cd Website/user_microservice (or open as new IntelliJ project)
$ mvn spring-boot:run (or click green Start-Button in IntelliJ)

$ cd Website/calendar_microservice (or open as new IntelliJ project)
$ mvn spring-boot:run (or click green Start-Button in IntelliJ)
```

## Set-Up Frontend
```
$ cd Website/frontend
$ npm install
$ ng serve
```
