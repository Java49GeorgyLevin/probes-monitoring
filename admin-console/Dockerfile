FROM eclipse-temurin:17-jdk-alpine
COPY ./target/admin-console-0.0.1.jar app.jar
EXPOSE 8484
ENV CONFIG_SERVER_HOST=config-server
ENV ACCOUNT_PROVIDER_HOST=account-provider
ENTRYPOINT [ "java", "-jar", "app.jar" ]