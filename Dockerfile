FROM openjdk:17

WORKDIR /app

COPY target/WebClient_Factory-0.0.1-SNAPSHOT.jar WebClient_Factory-0.0.1-SNAPSHOT.jar

EXPOSE 8080
CMD ["java", "-jar", "WebClient_Factory-0.0.1-SNAPSHOT.jar"]