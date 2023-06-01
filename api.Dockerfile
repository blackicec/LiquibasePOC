FROM eclipse-temurin:17-jdk-focal

ADD api/target/api-3.0.1.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]