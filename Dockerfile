# Use an official OpenJDK 21 runtime as a parent image
FROM eclipse-temurin:21-jdk

ADD target/spotify-youtube-sync-github-action.war spotify-youtube-sync-github-action.war

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the WAR file using Tomcat
ENTRYPOINT ["java", "-jar", "/spotify-youtube-sync-github-action.war"]