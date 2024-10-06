package ru.liga.packagesproject.service.truckunloading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagesproject.dto.TruckBodyDto;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;
import ru.liga.packagesproject.service.DefaultPackageService;

import java.util.HashMap;
import java.util.Map;

@Service
public class TruckUnloader {

    private final DefaultPackageService defaultPackageService;

    @Autowired
    public TruckUnloader(DefaultPackageService defaultPackageService) {
        this.defaultPackageService = defaultPackageService;
    }

    /**
     * Метод для разгрузки трака из json файла. Вызывает методы парсинга и подсчета посылок.
     *
     * @param truckBody тело трака
     * @return трак, заполненный посылками.
     */
    public Truck unloadTruck(TruckBodyDto truckBody) {
        Map<Character, Integer> symbolCounts = countSymbols(truckBody.getBody());
        Map<Package, Integer> packageCounts = parsePackagesInTruckBody(truckBody, symbolCounts);

        return new Truck(truckBody.getBody(), packageCounts);
    }

    private Map<Package, Integer> parsePackagesInTruckBody(TruckBodyDto truckBody, Map<Character, Integer> symbolCounts) {
        Map<Package, Integer> parsedPackages = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : symbolCounts.entrySet()) {
            char symbol = entry.getKey();
            int totalSymbols = entry.getValue();

            Iterable<Package> possiblePackages = defaultPackageService.findPackagesBySymbol(symbol);

            for (Package pkg : possiblePackages) {
                int packageArea = pkg.getArea();
                int count = totalSymbols / packageArea;

                if (count > 0 && matchesForm(truckBody.getBody(), pkg.getFormAsCharArray())) {
                    parsedPackages.put(pkg, count);
                }
            }
        }
        return parsedPackages;
    }

    private Map<Character, Integer> countSymbols(char[][] truckBody) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char[] row : truckBody) {
            for (char cell : row) {
                if (cell != ' ') {
                    counts.put(cell, counts.getOrDefault(cell, 0) + 1);
                }
            }
        }
        return counts;
    }

    private boolean matchesForm(char[][] truckBody, char[][] form) {
        int truckRows = truckBody.length;
        int truckCols = truckBody[0].length;
        int formRows = form.length;
        int formCols = form[0].length;

        for (int i = 0; i <= truckRows - formRows; i++) {
            for (int j = 0; j <= truckCols - formCols; j++) {
                if (matchesAtPosition(truckBody, form, i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Проверяет, соответствует ли форма посылки участку грузовика начиная с позиции (startRow, startCol)
     */
    private boolean matchesAtPosition(char[][] truckBody, char[][] form, int startRow, int startCol) {
        int formRows = form.length;
        int formCols = form[0].length;

        for (int i = 0; i < formRows; i++) {
            for (int j = 0; j < formCols; j++) {
                char formCell = form[i][j];
                char truckCell = truckBody[startRow + i][startCol + j];

                if (formCell != ' ' && formCell != truckCell) {
                    return false;
                }

            }
        }

        return true;
    }
}

