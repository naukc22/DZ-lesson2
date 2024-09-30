package ru.liga.packagesproject.services.IO.input;

import java.util.List;

public interface InputReader<T> {
    List<?> read(String filePath);
}
