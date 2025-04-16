######## STAGE 1: BUILD ########
FROM alpine AS build

ARG NG_CLI_VERSION=19.1.6

RUN apk add \
    npm \
		nginx \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
WORKDIR /Kalender
COPY . .
WORKDIR /Kalender/Website/frontend

# Installiere notwendige Tools
RUN npm install && \
		npm install -g @angular/cli@${NG_CLI_VERSION}

# Baue die Angular-Anwendung
RUN ng build

# Bereite die Dateien für den nächsten Stage vor
RUN mkdir -p /app && \
    mv dist/frontend/ /app/
##################################

######## STAGE 2: RUNTIME ########
FROM nginx:alpine AS prod

# Kopiere Nginx-Konfiguration
COPY kalender_app.conf /etc/nginx/conf.d/kalender_app.conf

# Kopiere die gebaute Anwendung aus dem Build-Stage
WORKDIR /app
COPY --from=build /app/ .

# Starte Nginx im Vordergrund
CMD ["nginx", "-g", "daemon off;"]
##################################
