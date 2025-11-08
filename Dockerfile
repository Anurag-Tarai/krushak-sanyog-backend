# Use a working JDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy your Spring Boot jar into the container
COPY target/farmerconnect-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app uses
EXPOSE 8443

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
