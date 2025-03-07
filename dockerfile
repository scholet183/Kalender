FROM openjdk:21-slim
 
# Installiere Maven, npm, curl

RUN apt update && apt install -y \
    npm \
		nginx \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
 
WORKDIR /Kalender
 
# Anwendungscode kopieren

COPY . .
 
# Start-Skript kopieren und ausführbar machen

COPY start.sh /

RUN chmod +x /start.sh
 
# Ports freigeben (je nach Bedarf)

EXPOSE 4200 

# build application

RUN apt update -y
RUN mkdir /app
WORKDIR /Kalender/Website/frontend
RUN npm install
RUN npm install -g\
		npm@10.9.2\
	  yarn@1.13.0\
		node@18.19.1\
		@angular/cli@19.1.6
RUN ng build
RUN cp -r dist/frontend/ /app/
RUN ls /Kalender/Website/frontend/dist/frontend
RUN cp /Kalender/kalender_app /etc/nginx/sites-enabled/
WORKDIR /
RUN rm -rf /Kalender/
# Start-Skript als Container-Befehl festlegen

CMD ["/start.sh"]
