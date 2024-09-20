package ru.liga.packagesproject.util;

import lombok.extern.log4j.Log4j2;
import ru.liga.packagesproject.model.Package;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PackageFileReader {

    public List<Package> readPackagesFromFile(String filePath) throws IOException {
        log.info("Начинаем чтение посылок из файла: {}", filePath);
        BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath));
        List<Package> packages = new ArrayList<>();
        String line;
        List<String> packageLines = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                if (!packageLines.isEmpty()) {
                    packages.add(convertFromStringsToPackage(packageLines));
                    log.debug("Добавлена посылка из {} строк", packageLines.size());
                    packageLines.clear();
                }
            } else {
                packageLines.add(line);
            }
        }

        if (!packageLines.isEmpty()) {
            packages.add(convertFromStringsToPackage(packageLines));
            log.debug("Добавлена последняя посылка из {} строк", packageLines.size());
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
