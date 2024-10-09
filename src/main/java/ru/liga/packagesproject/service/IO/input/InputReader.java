package ru.liga.packagesproject.service.IO.input;

import java.util.List;

/**
 * Интерфейс, представляющий классы для чтения данных идущих на вход программы
 */
public interface InputReader<T> {
    List<T> read(String filePath);
}
