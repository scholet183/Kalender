FROM openjdk:21-slim

# Installiere Maven, npm, curl

RUN apt update && apt install -y \
    maven \
    curl && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
 
WORKDIR /calendar_microservice
 
# Anwendungscode kopieren

COPY . .
 
EXPOSE 8081

RUN mvn clean package -DskipTests
RUN cp target/calendar-0.0.1-SNAPSHOT.jar /

WORKDIR /
CMD ["java", "-jar", "calendar-0.0.1-SNAPSHOT.jar"]


