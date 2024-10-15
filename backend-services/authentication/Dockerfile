# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app/trello_clone

# Add the JAR file from your build (replace `your-app.jar` with the actual file name)
COPY target/authentication-trello-0.0.1-SNAPSHOT.jar /app/trello_clone/authentication-trello-0.0.1-SNAPSHOT.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "authentication-trello-0.0.1-SNAPSHOT.jar.jar"]
