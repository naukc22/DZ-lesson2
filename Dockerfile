# Используем официальный образ OpenJDK 17 для запуска Java-приложений
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл jar в контейнер
COPY build/libs/your-app-name.jar app.jar

# Экспонируем порт 8080 (порт вашего приложения)
EXPOSE 8080

# Запуск приложения
CMD ["java", "-jar", "app.jar"]
