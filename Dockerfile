# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/ms-books-catalogue-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5227
ENTRYPOINT ["java", "-jar", "app.jar"]
