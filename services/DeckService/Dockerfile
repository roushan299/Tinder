# Stage 1: Build the application using Gradle
FROM gradle:8.4-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

# Stage 2: Run the application using a lightweight JDK
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
