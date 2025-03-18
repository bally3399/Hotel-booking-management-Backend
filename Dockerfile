FROM maven:3-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-alpine
COPY --from=build /target/bimber_user_service-0.0.1-SNAPSHOT.jar bimber_user_service.jar
ENTRYPOINT ["java","-Dspring.profiles.active=render","-jar","bimber_user_service.jar"]
EXPOSE 9191