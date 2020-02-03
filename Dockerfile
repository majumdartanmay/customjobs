FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="tanmay.majumdar@hotmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp
# Make port 5000 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/customjobs_networking-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY ./target/customjobs_networking-0.0.1-SNAPSHOT.jar ${JAR_FILE}
WORKDIR target
RUN ls
# Run the jar file
ENTRYPOINT ["java","-jar","customjobs_networking-0.0.1-SNAPSHOT.jar"]