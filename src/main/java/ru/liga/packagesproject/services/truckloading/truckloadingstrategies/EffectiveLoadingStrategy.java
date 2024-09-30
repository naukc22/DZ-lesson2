package ru.liga.packagesproject.services.truckloading.truckloadingstrategies;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.List;

/**
 * EffectiveLoadingStrategy реализует стратегию эффективной загрузки грузовиков.
 * Посылки загружаются как можно плотнее, занимая наименьшее возможное количество грузовиков.
 */
@Slf4j
public class EffectiveLoadingStrategy implements LoadingStrategy {

    /**
     * Метод реализует стратегию эффективной загрузки посылок в грузовики.
     *
     * @param packages список посылок, которые нужно загрузить
     * @param trucks   список пустых траков, которые нужно загрузить
     * @return список грузовиков с загруженными посылками
     */
    @Override
    public List<Truck> loadPackages(List<Package> packages, List<Truck> trucks) {
        // Сортируем посылки по площади в порядке убывания для максимального заполнения грузовика
        sortPackagesByAreaInDescendingOrder(packages);
        sortTrucksByAreaInDescendingOrder(trucks);
        log.debug("Посылки отсортированы по размеру: {}", packages);

        int currentTruckIndex = 0;

        while (!packages.isEmpty() && currentTruckIndex < trucks.size()) {
            Truck currentTruck = trucks.get(currentTruckIndex);
            log.debug("Попытка загрузить посылки в грузовик {}", currentTruckIndex + 1);

            for (int i = 0; i < packages.size(); ) {
                Package currentPackage = packages.get(i);
                boolean isLoaded = tryToLoadPackageIntoTruck(currentTruck, currentPackage);

                if (isLoaded) {
                    // Убираем загруженную посылку из списка
                    log.debug("Посылка '{}' загружена в грузовик {}.", currentPackage.getName(), currentTruckIndex + 1);
                    packages.remove(i);
                } else {
                    // Если посылка не поместилась, переходим к следующей
                    i++;
                }
            }

            // Если текущий грузовик полностью заполнен, переходим к следующему
            currentTruckIndex++;
        }

        // Если остались посылки, но грузовики закончились
        if (!packages.isEmpty()) {
            log.error("Ошибка: Посылки не поместились во все доступные грузовики.");
            throw new RuntimeException("Ошибка: Посылки не поместились во все доступные грузовики.");
        }

        return trucks;
    }

    /**
     * Попытка загрузить посылку в текущий грузовик.
     *
     * @param truck текущий грузовик
     * @param pack  посылка, которую нужно загрузить
     * @return true, если посылка успешно загружена, иначе false
     */
    private boolean tryToLoadPackageIntoTruck(Truck truck, Package pack) {
        // Пытаемся найти место в грузовике, начиная с нижнего левого угла
        for (int row = truck.getHeight() - 1; row >= 0; row--) {
            for (int column = 0; column < truck.getWidth(); column++) {
                if (!truck.isCellOccupied(row, column)) {
                    if (truck.tryLoadPackage(pack, row, column)) {
                        log.debug("Посылка '{}' загружена в грузовик на позицию ({}, {})", pack.getName(), row, column);
                        return true; // Посылка успешно загружена
                    }
                }
            }
        }
        return false; // Если посылка не поместилась в грузовик
    }

    private void sortTrucksByAreaInDescendingOrder(List<Truck> trucks) {
        trucks.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }


}
