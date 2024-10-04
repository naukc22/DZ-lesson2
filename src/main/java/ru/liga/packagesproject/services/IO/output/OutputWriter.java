package ru.liga.packagesproject.services.IO.output;

import java.util.List;

public interface OutputWriter<T> {
    void write(List<T> data, String destination);
}

