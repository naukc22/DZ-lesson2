-- Создание схемы, если она не существует
CREATE SCHEMA IF NOT EXISTS loading;

-- Удаление таблицы, если она существует
DROP TABLE IF EXISTS loading.package;

-- Создание таблицы с проверкой на существование
CREATE TABLE loading.package
(
    name VARCHAR(255) PRIMARY KEY,
    symbol CHAR(1) NOT NULL,
    form TEXT NOT NULL
);
