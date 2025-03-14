############ STAGE 1: Basiscontainer ############ 
FROM maven:3.9.6-eclipse-temurin-21-alpine AS base
WORKDIR /calendar_microservice
COPY . .
EXPOSE 8081
#################################################

############ STAGE 2: Microservice build ohne Tests ############ 
FROM base AS build
RUN mvn clean package -DskipTests
################################################################

############ STAGE 3: Container für Nutzende mit laufendem Microservice ############ 
FROM eclipse-temurin:21-jre-alpine AS prod
WORKDIR /app
COPY --from=build /calendar_microservice/target/calendar-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "calendar-0.0.1-SNAPSHOT.jar"]
####################################################################################


############ STAGE 4: Testcontainer ############ 
FROM base AS test
CMD ["mvn", "clean", "test" ]
################################################
