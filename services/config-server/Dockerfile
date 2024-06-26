# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Copy the JAR file to the container
ADD target/config-server-0.0.1-SNAPSHOT.jar config-server-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT [ "java","-jar","config-server-0.0.1-SNAPSHOT.jar" ]