FROM eclipse-temurin:17-jdk-focal

ADD out/artifacts/app_jar/app.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]