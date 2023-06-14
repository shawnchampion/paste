FROM openjdk:17-jdk
COPY target/paste-0.0.1.jar /
CMD ["java", "-jar", "/paste-0.0.1.jar"]