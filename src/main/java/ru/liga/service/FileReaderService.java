package ru.liga.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderService {

    public static List<int[][]> readPackagesFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<int[][]> packages = new ArrayList<>();
        String line;
        List<String> packageLines = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                if (!packageLines.isEmpty()) {
                    packages.add(convertFromStringsToPackages(packageLines));
                    packageLines.clear();
                }
            } else {
                packageLines.add(line);
            }
        }
        // Добавляем последнюю посылку
        if (!packageLines.isEmpty()) {
            packages.add(convertFromStringsToPackages(packageLines));
        }

        return packages;
    }

    private static int[][] convertFromStringsToPackages(List<String> packageLines) {
        int[][] pack = new int[packageLines.size()][];

        for (int i = 0; i < packageLines.size(); i++) {
            String line = packageLines.get(i);
            pack[i] = new int[line.length()];
            for (int j = 0; j < line.length(); j++) {
                pack[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        return pack;
    }

}
