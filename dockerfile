FROM openjdk:21-slim
 
# Installiere Maven, npm, curl

RUN apt update && apt install -y \
    maven \
    npm \
		nginx \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
 
WORKDIR /Kalender
 
# Anwendungscode kopieren

COPY . .
 
# Abhängigkeiten installieren (z.B. für Node, falls benötigt)

RUN set -e && npm install
 
# Start-Skript kopieren und ausführbar machen

COPY start.sh /

RUN chmod +x /start.sh
 
# Ports freigeben (je nach Bedarf)

EXPOSE 4200 8080 8081 3000

# build application
RUN mkdir app
RUN set -e
WORKDIR /Kalender/Website/user_microservice
RUN mvn clean package -DskipTests
WORKDIR /Kalender/Website/user_microservice/target
RUN cp user_microservice-0.0.1-SNAPSHOT.jar /Kalender/app/
WORKDIR /Kalender/Website/calendar_microservice
RUN	mvn clean package -DskipTests
WORKDIR /Kalender/Website/calendar_microservice/target
RUN cp calendar-0.0.1-SNAPSHOT.jar /Kalender/app/

RUN apt update -y
WORKDIR /Kalender/Website/frontend
RUN npm install
RUN npm install -g\
		npm@10.9.2\
	  yarn@1.13.0\
		node@18.19.1\
		@angular/cli@19.1.6
RUN ng build
RUN cp -r dist/frontend/ /Kalender/app/
RUN ls /Kalender/Website/frontend/dist/frontend
RUN cp /Kalender/kalender_app /etc/nginx/sites-enabled/
WORKDIR /
RUN mv /Kalender/app/ /
RUN rm -rf /Kalender/
# Start-Skript als Container-Befehl festlegen

CMD ["/start.sh"]
