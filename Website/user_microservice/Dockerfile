############ STAGE 1 ############ 
FROM maven:3.9.6-eclipse-temurin-21-alpine AS base
WORKDIR /user_microservice
COPY . .
EXPOSE 8080
#################################

############ STAGE 2 ############ 
FROM base AS build
RUN mvn clean package -DskipTests
#################################

############ STAGE 3 ############ 
FROM maven:3.9.6-eclipse-temurin-21-alpine AS prod
WORKDIR /app
COPY --from=build /user_microservice/target/user_microservice-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "user_microservice-0.0.1-SNAPSHOT.jar"]
#################################

############ STAGE 4 ############ 
FROM base AS test
CMD ["mvn", "clean", "test" ]
#################################
