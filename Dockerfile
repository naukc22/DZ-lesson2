FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /app

# Копируем все файлы проекта и устанавливаем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Финальная сборка для запуска
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
