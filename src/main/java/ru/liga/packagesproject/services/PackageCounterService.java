package ru.liga.packagesproject.services;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Truck;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для подсчета количества посылок, загруженных в грузовик.
 */
@Slf4j
public class PackageCounterService {

    /**
     * Подсчитывает количество посылок в заданном грузовике.
     *
     * @param truck объект {@link Truck}, в котором производится подсчет
     * @return мапа, где ключом является символ, обозначающий тип посылки, а значением — количество посылок этого типа
     */
    public static Map<Character, Integer> countPackages(Truck truck) {
        log.info("Начинаем подсчёт посылок в траке.");

        Map<Character, Integer> packageCounts = countCellsForEveryPackage(truck);

        Map<Character, Integer> actualPackageCounts = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : packageCounts.entrySet()) {
            char packageType = entry.getKey();
            int totalCells = entry.getValue();

            int area = Character.getNumericValue(packageType);

            int count = totalCells / area;
            actualPackageCounts.put(packageType, count);

            log.debug("Подсчитаны посылки: тип = {}, количество = {}", packageType, count);
        }
        log.info("Закончили подсчет посылок в траке");
        return actualPackageCounts;
    }

    private static Map<Character, Integer> countCellsForEveryPackage(Truck truck) {
        Map<Character, Integer> packageCounts = new HashMap<>();
        char[][] body = truck.getBody();

        for (char[] row : body) {
            for (char cell : row) {
                if (cell == '+' || cell == ' ') {
                    continue;
                }

                packageCounts.put(cell, packageCounts.getOrDefault(cell, 0) + 1);
            }
        }

        log.debug("Количество ячеек для каждой посылки: {}", packageCounts);
        return packageCounts;
    }

    /**
     * Выводит количество посылок каждого типа на консоль.
     *
     * @param packageCounts мапа с количеством посылок для каждого типа
     */
    public static void printPackageCounts(Map<Character, Integer> packageCounts) {
        for (Map.Entry<Character, Integer> entry : packageCounts.entrySet()) {
            System.out.println("Посылка типа " + entry.getKey() + " - " + entry.getValue() + " шт.");
        }
    }

}
