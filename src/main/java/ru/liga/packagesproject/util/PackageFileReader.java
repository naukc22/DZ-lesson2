package ru.liga.packagesproject.util;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.model.Package;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PackageFileReader {

    public List<Package> readPackagesFromFile(String filePath) {
        log.info("Начинаем чтение посылок из файла: {}", filePath);
        List<Package> packages = new ArrayList<>();
        List<String> packageLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (!packageLines.isEmpty()) {
                        packages.add(convertFromStringsToPackage(packageLines));
                        packageLines.clear();
                    }
                } else {
                    packageLines.add(line);
                }
            }

            if (!packageLines.isEmpty()) {
                try {
                    packages.add(convertFromStringsToPackage(packageLines));
                    log.debug("Добавлена последняя посылка из {} строк", packageLines.size());
                } catch (Exception e) {
                    log.error("Ошибка при конвертации последней строки в посылку: {}", packageLines, e);
                }
            }

        } catch (FileNotFoundException e) {
            log.error("Файл не найден: {}", filePath, e);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при чтении файла: {}", filePath, e);
        }

        log.info("Чтение посылок завершено. Всего посылок: {}", packages.size());
        return packages;
    }


    private static Package convertFromStringsToPackage(List<String> packageLines) {
        int[][] shape = new int[packageLines.size()][];
        for (int i = 0; i < packageLines.size(); i++) {
            String line = packageLines.get(i);
            shape[i] = new int[line.length()];
            for (int j = 0; j < line.length(); j++) {
                shape[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        return new Package(shape);
    }


}
