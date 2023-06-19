# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app
RUN apk --no-cache add postgresql-client postgresql bash locales

RUN echo "en_US.UTF-8 UTF-8" > /etc/locale.gen && \
    locale-gen

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
# Copy the JAR file to the container
COPY target/ReviewServiceWrite-1.0-SNAPSHOT.jar .

# Install PostgreSQL client
RUN apk add --no-cache postgresql-client

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/servicewrite
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SERVER_PORT=8080

# Expose the port
EXPOSE 8080

# Start the PostgreSQL container
RUN apk add --no-cache postgresql
RUN mkdir -p /var/lib/postgresql/data
RUN chown -R postgres:postgres /var/lib/postgresql/data
USER postgres
RUN initdb -D /var/lib/postgresql/data
RUN pg_ctl start -D /var/lib/postgresql/data -l logfile
RUN createdb -U postgres servicewrite

# Start the microservice container
USER root
CMD ["java", "-jar", "microservice.jar"]
