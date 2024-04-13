FROM eclipse-temurin:17-jdk-alpine as builder
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
