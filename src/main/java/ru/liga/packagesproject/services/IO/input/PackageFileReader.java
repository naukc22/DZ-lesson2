package ru.liga.packagesproject.services.IO.input;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для чтения информации о посылках из текстового файла.
 */
@Slf4j
public class PackageFileReader implements InputReader<Package> {

    /**
     * Читает посылки из указанного текстового файла.
     * Каждая посылка представлена последовательностью строк, разделённых пустыми строками.
     *
     * @param filePath путь к файлу с данными о посылках
     * @return список объектов {@link Package}, прочитанных из файла
     * @throws RuntimeException если возникла ошибка ввода/вывода
     */
    @Override
    public List<Package> read(String filePath) {
        log.info("Начинаем чтение посылок из файла: {}", filePath);
        List<Package> packages = new ArrayList<>();
        List<String> packageLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (!packageLines.isEmpty()) {
                        packages.add(new Package(packageLines));
                        packageLines.clear();
                    }
                } else {
                    packageLines.add(line);
                }
            }

            if (!packageLines.isEmpty()) {
                try {
                    packages.add(new Package(packageLines));
                    log.debug("Добавлена последняя посылка из {} строк", packageLines.size());
                } catch (Exception e) {
                    log.error("Ошибка при конвертации последней строки в посылку: {}", packageLines, e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Чтение посылок завершено. Всего посылок: {}", packages.size());
        return packages;
    }

}

