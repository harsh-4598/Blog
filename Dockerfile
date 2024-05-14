# Use the official OpenJDK image as a base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/blog.jar /app/blog.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Specify the command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "blog.jar"]