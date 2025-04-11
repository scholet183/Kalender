######## STAGE 1: BUILD ########
FROM alpine AS build
RUN apk add \
    npm \
		nginx \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
WORKDIR /Kalender
COPY . .
EXPOSE 4200 
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
##################################

######## STAGE 2: RUNTIME ########
FROM nginx:alpine AS prod
COPY kalender_app.conf /etc/nginx/conf.d/kalender_app.conf
WORKDIR /app
COPY --from=build /app/ .
CMD ["nginx", "-g", "daemon off;"]
##################################
