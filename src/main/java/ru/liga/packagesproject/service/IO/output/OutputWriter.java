package ru.liga.packagesproject.service.IO.output;

import java.util.List;

/**
 * Интерфейс, представляющий классы для записи данных идущих на выход программы
 */
public interface OutputWriter<T> {
    void write(List<T> data, String destination);
}

