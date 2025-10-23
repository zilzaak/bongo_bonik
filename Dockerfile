FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/bazarSodai-1.0-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]