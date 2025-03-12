FROM openjdk:21-bullseye

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} myApp.jar

EXPOSE 8181
ENTRYPOINT ["java","-jar","/myApp.jar"]