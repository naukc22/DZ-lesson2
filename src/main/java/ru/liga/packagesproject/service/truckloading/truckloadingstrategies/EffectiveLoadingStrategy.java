package ru.liga.packagesproject.service.truckloading.truckloadingstrategies;

import lombok.extern.slf4j.Slf4j;
import ru.liga.packagesproject.models.Package;
import ru.liga.packagesproject.models.Truck;

import java.util.List;

/**
 * EffectiveLoadingStrategy реализует стратегию эффективной загрузки грузовиков.
 * Посылки загружаются как можно плотнее, занимая наименьшее возможное количество грузовиков.
 */
@Slf4j
public class EffectiveLoadingStrategy extends LoadingStrategy {

    /**
     * Метод реализует стратегию эффективной загрузки посылок в грузовики.
     *
     * @param packages список посылок, которые нужно загрузить
     * @param trucks   список пустых траков, которые нужно загрузить
     * @return список грузовиков с загруженными посылками
     */
    @Override
    public List<Truck> loadPackages(List<Package> packages, List<Truck> trucks) {

        sortPackagesByAreaInDescendingOrder(packages);
        sortTrucksByAreaInDescendingOrder(trucks);
        log.debug("Посылки отсортированы по размеру: {}", packages);

        int currentTruckIndex = 0;

        while (!packages.isEmpty() && currentTruckIndex < trucks.size()) {
            Truck currentTruck = trucks.get(currentTruckIndex);
            log.debug("Попытка загрузить посылки в грузовик {}", currentTruckIndex + 1);

            for (int i = 0; i < packages.size(); ) {
                Package currentPackage = packages.get(i);
                boolean isLoaded = findSpaceForLoadingPackageIntoTruckAndTryToLoad(currentTruck, currentPackage);

                if (isLoaded) {
                    log.debug("Посылка '{}' загружена в грузовик {}.", currentPackage.getName(), currentTruckIndex + 1);
                    packages.remove(i);
                } else {
                    i++;
                }
            }

            currentTruckIndex++;
        }

        if (!packages.isEmpty()) {
            log.error("Ошибка: Посылки не поместились во все доступные грузовики. Список не поместившихся посылок: {}", packages);
            throw new RuntimeException();
        }

        return trucks;
    }


}
