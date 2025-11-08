# ---------- Stage 1: Build ----------
FROM maven:3.9.1-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy all source code
COPY src ./src

# Package the application (skip tests if needed)
RUN mvn package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (8443 in your current Dockerfile)
EXPOSE 8443

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
