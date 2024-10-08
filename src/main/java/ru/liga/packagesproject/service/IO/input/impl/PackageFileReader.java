package ru.liga.packagesproject.service.IO.input.impl;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.service.IO.input.InputReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения информации о посылках из текстового файла.
 */
@Slf4j
public class PackageFileReader implements InputReader<List<String>> {

    /**
     * Читает посылки из указанного текстового файла.
     * Каждая посылка представлена последовательностью строк, разделённых пустыми строками.
     *
     * @param filePath путь к файлу с данными о посылках
     * @return список списков строк, представляющих каждую посылку
     * @throws RuntimeException если возникла ошибка ввода/вывода
     */
    public List<List<String>> read(String filePath) {
        log.info("Начинаем чтение посылок из файла: {}", filePath);
        List<List<String>> packagesData = new ArrayList<>();
        List<String> packageLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (!packageLines.isEmpty()) {
                        packagesData.add(new ArrayList<>(packageLines));
                        packageLines.clear();
                    }
                } else {
                    packageLines.add(line);
                }
            }

            if (!packageLines.isEmpty()) {
                packagesData.add(new ArrayList<>(packageLines));
                log.debug("Добавлена последняя посылка из {} строк", packageLines.size());
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла с посылками: " + filePath, e);
        }

        log.info("Чтение посылок завершено. Всего посылок: {}", packagesData.size());
        return packagesData;
    }

}

