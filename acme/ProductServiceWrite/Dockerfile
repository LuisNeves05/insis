# Use the latest OpenJDK image as the base image
FROM openjdk:17-jdk-alpine3.14

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot app to the container
COPY target/ProductServiceWrite-1.0-SNAPSHOT.jar /app/app.jar

# Expose the port that the app listens on
EXPOSE 8083

# Run the Spring Boot app when the container starts
CMD ["java", "-jar", "app.jar"]
