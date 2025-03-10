############ STAGE 1 ############ 
FROM maven:3.9.6-eclipse-temurin-21-alpine AS base
WORKDIR /calendar_microservice
COPY . .
EXPOSE 8081
#################################

############ STAGE 2 ############ 
FROM base AS build
RUN mvn clean package -DskipTests
#################################

############ STAGE 3 ############ 
FROM eclipse-temurin:21-jre-alpine AS prod
WORKDIR /app
COPY --from=build /calendar_microservice/target/calendar-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "calendar-0.0.1-SNAPSHOT.jar"]
#################################


############ STAGE 4 ############ 
FROM base AS test
CMD ["mvn", "clean", "test" ]
#################################
