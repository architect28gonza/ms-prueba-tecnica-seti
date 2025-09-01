FROM openjdk:21-ea-1-jdk-slim

VOLUME /tmp

COPY applications/app-service/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]