# STAGE 1: BUILD 
FROM alpine AS build
 
# Installiere Maven, npm, curl

RUN apk add \
    npm \
		nginx \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
 
WORKDIR /Kalender
 
# Anwendungscode kopieren

COPY . .
 
# Ports freigeben (je nach Bedarf)

EXPOSE 4200 

# build application

RUN mkdir /app
WORKDIR /Kalender/Website/frontend
RUN npm install && \
npm install -g\
		npm@10.9.2\
	  yarn@1.13.0\
		node@18.19.1\
		@angular/cli@19.1.6
RUN ng build  
RUN mv dist/frontend/ /app/ && ls /app/
#WORKDIR /
#RUN rm -rf /Kalender/
# Start-Skript als Container-Befehl festlegen

# STAGE 2: RUNTIME
FROM nginx:alpine AS prod
COPY kalender_app /etc/nginx/http.d/kalender_app
WORKDIR /app
COPY --from=build /app/ .
CMD ["nginx", "-g", "daemon off;"]
