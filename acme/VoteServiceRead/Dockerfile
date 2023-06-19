# Start with the official JDK 17 image
FROM openjdk:17-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file into the container
COPY target/ReviewServiceRead-1.0-SNAPSHOT.jar reviewServiceWrite.jar

# Install PostgreSQL client tools
RUN apk add --no-cache postgresql-client

# Set the service index as a build-time argument
ARG SERVICE_INDEX

# Create the database and user
RUN apk add --no-cache postgresql && \
    pg_ctl -D /var/lib/postgresql/data start && \
    psql -c "CREATE DATABASE reviewServiceRead;" && \
    psql -c "CREATE USER my_user WITH ENCRYPTED PASSWORD 'my_password';" && \
    psql -c "GRANT ALL PRIVILEGES ON DATABASE reviewServiceWrite1 TO my_user;"

# Set the database connection properties as environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost/reviewServiceRead
ENV SPRING_DATASOURCE_USERNAME=my_user
ENV SPRING_DATASOURCE_PASSWORD=my_password

# Set the base port number as an environment variable
ENV BASE_PORT=8080



# Set the server port using a shell script
RUN echo "export SERVER_PORT=$((BASE_PORT + SERVICE_INDEX))" > /etc/profile.d/server_port.sh
ENV ENV="/etc/profile"

# Expose the microservice's port
EXPOSE $SERVER_PORT

# Start the microservice
CMD ["java", "-jar", "reviewServiceWrite.jar"]
