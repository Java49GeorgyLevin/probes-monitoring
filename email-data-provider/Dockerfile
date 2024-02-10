FROM eclipse-temurin:17-jdk-alpine
COPY ./target/email-data-provider-0.0.1.jar app.jar
EXPOSE 8585
ENV CONFIG_SERVER_HOST=config-server

ENTRYPOINT [ "java", "-jar", "app.jar" ]