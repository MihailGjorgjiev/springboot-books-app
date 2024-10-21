FROM openjdk:17-jdk-alpine
LABEL authors="Mihail"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]