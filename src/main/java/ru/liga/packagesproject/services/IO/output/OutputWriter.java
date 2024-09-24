package ru.liga.packagesproject.services.IO.output;

public interface OutputWriter<T> {
    void write(T data, String destination);
}

