# Use an OpenJDK base image
FROM eclipse-temurin:23-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the fat JAR into the container
COPY build/libs/pcd-example-0.0.2-SNAPSHOT.jar /app/main.jar

# Expose the application port (if applicable)
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "main.jar"]
