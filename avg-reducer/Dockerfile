FROM eclipse-temurin:17-jdk-alpine
COPY ./target/avg-reducer-0.0.1.jar app.jar
ENV CONFIG_SERVER_HOST=config-server
ENV app.average.reducing.size=5

ENTRYPOINT [ "java", "-jar", "app.jar" ]